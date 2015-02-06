package com.supaham.commons.jdbc.sql;

import static org.junit.Assert.assertTrue;

import com.supaham.commons.placeholders.Placeholder;
import com.supaham.commons.placeholders.PlaceholderData;
import com.supaham.commons.placeholders.PlaceholderSet;
import com.supaham.commons.placeholders.SimplePlaceholder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.annotation.Nullable;

/**
 * Created by Ali on 02/02/2015.
 */
public class MySQLDatabaseTest {

  private SQLDatabase database;

  @Before
  public void setUp() throws Exception {
    System.out.println("Setting up MySQL test...");
    try {
      MySQLConfig config = new MySQLConfig("localhost", 3306, "root", "", "test",
                                           new HashMap<String, String>());
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

    JdbcTemplate template = database.getJdbcAgent().createJdbcTemplate();

    result = ((boolean) template.query("SHOW TABLES", new ResultSetExtractor<Object>() {
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
    assertTrue("Table wasn't actually created", result); // Our players table exists!
  }
}
