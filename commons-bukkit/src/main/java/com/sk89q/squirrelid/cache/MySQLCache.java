package com.sk89q.squirrelid.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import com.sk89q.squirrelid.Profile;
import com.supaham.commons.CMain;
import com.supaham.commons.database.JDBCAgent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nonnull;

/**
 * Represents a MySQL implementation of {@link AbstractProfileCache}.
 *
 * @author SupaHam
 * @since 0.1
 */
public class MySQLCache extends AbstractProfileCache {

  private final JDBCAgent agent;
  private final String tableName;
  private final String queryString;

  public MySQLCache(@Nonnull JDBCAgent jdbcAgent, @Nonnull String tableName) throws SQLException {
    checkNotNull(jdbcAgent);
//    checkNotNullOrEmpty(tableName, "tableName");
    this.agent = jdbcAgent;
    this.tableName = tableName;
    this.queryString = "REPLACE INTO `" + tableName + "` (uuid, name) VALUES (?, ?)";
    createTable();
  }

  @Override
  public void putAll(Iterable<Profile> iterable) {
    try {
      executePut(iterable);
    } catch (SQLException e) {
      CMain.getLogger().log(Level.WARNING, "Failed to execute queries", e);
    }
  }

  @Override
  public ImmutableMap<UUID, Profile> getAllPresent(Iterable<UUID> iterable) {
    try {
      return executeGet(iterable);
    } catch (SQLException e) {
      CMain.getLogger().log(Level.WARNING, "Failed to execute queries", e);
    }

    return ImmutableMap.of();
  }

  /**
   * Create the necessary tables and indices if they do not exist yet.
   *
   * @throws SQLException thrown on error
   */
  private void createTable() throws SQLException {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS `" + this.tableName + "` ("
          + "`uuid` CHAR(36) PRIMARY KEY NOT NULL, "
          + "`name` VARCHAR(16) NOT NULL UNIQUE KEY)");
    } catch (SQLException e) {
      throw new SQLException("Failed to create table.", e);
    }
  }

  protected synchronized void executePut(Iterable<Profile> profiles) throws SQLException {
    PreparedStatement stmt = preparedStatement();
    for (Profile profile : profiles) {
      stmt.setString(1, profile.getUniqueId().toString());
      stmt.setString(2, profile.getName());
      stmt.addBatch();
    }
    stmt.executeBatch();
  }

  protected ImmutableMap<UUID, Profile> executeGet(Iterable<UUID> uuids) throws SQLException {
    Iterator<UUID> it = uuids.iterator();
    // It was an empty collection
    if (!it.hasNext()) {
      return ImmutableMap.of();
    }

    StringBuilder builder = new StringBuilder();
    // SELECT ... WHERE ... IN ('abc', 'def', 'ghi');
    builder.append("SELECT name, uuid FROM `").append(this.tableName).append("` WHERE uuid IN ('");
    Joiner.on("', '").skipNulls().appendTo(builder, uuids);
    builder.append("');");

    synchronized (this) {
      try (Connection conn = getConnection();
           Statement stmt = conn.createStatement()) {
        ResultSet rs = stmt.executeQuery(builder.toString());
        Map<UUID, Profile> map = new HashMap<>();

        while (rs.next()) {
          UUID uuid = UUID.fromString(rs.getString("uuid"));
          map.put(uuid, new Profile(uuid, rs.getString("name")));
        }

        return ImmutableMap.copyOf(map);
      }
    }
  }

  private PreparedStatement preparedStatement() throws SQLException {
    return getConnection().prepareStatement(queryString);
  }

  private Connection getConnection() throws SQLException {
    return this.agent.getDataSource().getConnection();
  }
}
