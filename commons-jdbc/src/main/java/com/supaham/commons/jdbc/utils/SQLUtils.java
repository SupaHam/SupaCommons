package com.supaham.commons.jdbc.utils;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import com.google.common.base.Preconditions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

/**
 * Utility methods for working with an SQL database. This class contains methods such as
 * {@link #hasTable(Connection, String)}.
 *
 * @since 0.1
 */
public class SQLUtils {

  /**
   * Checks if a {@link DataSource}'s {@link Connection} to a database has a table. This method
   * calls {@link #hasTable(Connection, String)} and passes it {@link DataSource#getConnection()},
   * it also handles closing the given {@link Connection}.
   *
   * @param dataSource {@link DataSource} to get {@link Connection} to use from
   * @param namePattern namePattern to check for
   *
   * @return whether the {@code namePattern} exists, will also return false if an error occurs
   *
   * @see #hasTable(Connection, String)
   */
  public static boolean hasTable(@Nonnull DataSource dataSource, @Nonnull String namePattern) {
    try (Connection conn = dataSource.getConnection()) {
      return hasTable(conn, namePattern);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Checks if a {@link Connection} has a table. This method does not close the given
   * {@link Connection}. Please refer to {@link #hasTable(DataSource, String)} for an auto close.
   *
   * @param connection connection to use
   * @param namePattern namePattern to check for
   *
   * @return whether the {@code namePattern} exists.
   */
  public static boolean hasTable(@Nonnull Connection connection, @Nonnull String namePattern) {
    checkNotNullOrEmpty(namePattern, "namePattern");
    try (ResultSet rs = connection.getMetaData().getTables(null, null, namePattern, null)) {
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Returns whether a {@link ResultSet} contains a column by label.
   *
   * @param resultSet result set to check
   * @param columnLabel label to check
   *
   * @return whether the {@code resultSet} contains the {@code columnLabel}
   */
  public static boolean hasColumn(@Nonnull ResultSet resultSet, @Nonnull String columnLabel) {
    Preconditions.checkNotNull(resultSet, "result set cannot be null.");
    Preconditions.checkNotNull(columnLabel, "column label cannot be null.");
    try {
      ResultSetMetaData md = resultSet.getMetaData();
      for (int i = 1; i < md.getColumnCount() + 1; i++) {
        if (md.getColumnLabel(i).equalsIgnoreCase(columnLabel)) {
          return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
