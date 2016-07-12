package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.database.DatabaseSettings;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with databases, including support for {@link DatabaseSettings} serializable class.
 *
 * @since 0.5.2
 */
public class DatabaseUtils {

  public static final String MYSQL_CONN_STRING = "jdbc:mysql://%s:%s/%s";
  public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
  public static final int MYSQL_PORT = 3306;

  public static final int REDIS_PORT = 6379;

  @Nonnull
  public static String toMySQLConnectionString(@Nonnull DatabaseSettings config) {
    Preconditions.checkNotNull(config, "config cannot be null.");
    final String db = DatabaseSettings.MYSQL;

    String ip = config.getString(db, DatabaseSettings.IP)
        .orElseThrow(() -> new IllegalStateException("ip cannot be null."));
    String port = config.getString(db, DatabaseSettings.PORT).orElse(String.valueOf(MYSQL_PORT));
    String database = config.getString(db, DatabaseSettings.DATABASE)
        .orElseThrow(() -> new IllegalStateException("database cannot be null."));

    return String.format(MYSQL_CONN_STRING, ip, port, database);
  }
}
