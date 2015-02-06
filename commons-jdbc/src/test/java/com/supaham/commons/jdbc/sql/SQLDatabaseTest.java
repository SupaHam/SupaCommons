package com.supaham.commons.jdbc.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Ali on 02/02/2015.
 */
public class SQLDatabaseTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private SQLDatabase database;

  @Before
  public void setUp() throws Exception {
    try {
      File file = folder.newFile("test.sqlite");
      System.out.println(file.getAbsolutePath());
      SQLConfig config = new SQLConfig(file.getAbsolutePath(), new HashMap<String, String>());
      SpringJDBCAgent agent = SpringJDBCAgent.createAgent(config);
      database = new SQLDatabase(Logger.getLogger("test"), agent);
      Table table = new Table("myTable", "CREATE SOME TABLE KTHX;");
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
            System.out.println(rs.getString(1));
          }
        });
  }
}
