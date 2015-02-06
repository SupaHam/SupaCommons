package com.supaham.commons.jdbc.sql;

import java.util.Map;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.Name;

/**
 * Represents a MySQL configuration data class.
 */
@Name("MySQLConfig")
public class MySQLConfig extends SQLConfig {
  private transient String file;
  private String ip;
  private int port;
  private String username;
  private String password;
  private String database;
  
  public MySQLConfig(@Nonnull String ip, int port, @Nonnull String username,
                     @Nonnull String password, @Nonnull String database,
                     @Nonnull Map<String, String> tables) {
    super(null, tables);
    this.ip = ip;
    this.port = port;
    this.username = username;
    this.password = password;
    this.database = database;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDatabase() {
    return database;
  }
}
