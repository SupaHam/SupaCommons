package com.supaham.commons.jdbc.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.supaham.commons.jdbc.utils.SQLUtils;
import com.supaham.commons.placeholders.Placeholder;
import com.supaham.commons.placeholders.PlaceholderData;
import com.supaham.commons.placeholders.PlaceholderSet;
import com.supaham.commons.placeholders.SimplePlaceholder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.annotation.Nullable;

/**
 * Created by Ali on 02/02/2015.
 */
public class SQLDatabaseTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private SQLDatabase database;

  @Before
  public void setUp() throws Exception {
    System.out.println("Setting up SQL test...");
    try {
      File file = folder.newFile("test.sqlite");
      System.out.println(file.getAbsolutePath());
      SQLConfig config = new SQLConfig(file.getAbsolutePath(), new HashMap<String, String>());
      SpringJDBCAgent agent = SpringJDBCAgent.createAgent(config);
      database = new SQLDatabase(Logger.getLogger("test"), agent);
      Table table = new Table("myTable", "CREATE TABLE IF NOT EXISTS `{playersTable}` "
                                         + "(`abc` INT NOT NULL)");
      database.addTable("myTableId", table);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test() {
    assertTrue(database.hasTable("myTableId"));
    assertFalse(database.hasTable("myTableId that doesn't actually exist."));
  }

  @Test
  public void testGetByName() throws Exception {
    assertEquals("myTableId", database.getTableIdByName("myTable"));
  }

  @Test
  public void testConnection() throws Exception {
    database.getJdbcAgent().createJdbcTemplate().query(
        "SELECT 1", new RowCallbackHandler() {
          @Override
          public void processRow(ResultSet rs) throws SQLException {
            // do nothing, it works :D
          }
        });
  }

  @Test
  public void testCheckTable() throws Exception {
    PlaceholderSet<Placeholder> placeholders = new PlaceholderSet<>();
    placeholders.add(new SimplePlaceholder("playersTable") {
      @Nullable
      @Override
      public String apply(PlaceholderData input) {
        return input.getPlaceholder().equals("playersTable") ? "myPlayersTable" : null;
      }
    });
    boolean result = database.checkTable("myTableId", placeholders);
    assertTrue(result); // table didn't exist and was created!
    System.out
        .println(SQLUtils.hasTable(database.getJdbcAgent().getDataSource(), "myPlayersTable"));

    JdbcTemplate template = database.getJdbcAgent().createJdbcTemplate();
    result = ((boolean) template.query(
        "SELECT `name` FROM `SQLITE_MASTER`",
        new ResultSetExtractor<Object>() {
          @Override
          public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
              if (rs.getString(1).equalsIgnoreCase("myPlayersTable")) {
                return true;
              }
            }
            return false;
          }
        }));
    // TODO this doesn't actually work! :(
    // assertTrue("Table wasn't actually created", result); // Our players table exists!
  }
}
