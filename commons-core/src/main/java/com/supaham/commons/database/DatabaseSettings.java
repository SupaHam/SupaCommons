package com.supaham.commons.database;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import com.supaham.commons.utils.StringUtils;

import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializableAs;

@SerializableAs("DatabaseSettings")
public class DatabaseSettings {

  public static final String MYSQL = "mysql";
  public static final String REDIS = "redis";

  public static final String DATABASE = "database";
  public static final String ENABLED = "enabled";
  public static final String IP = "ip";
  public static final String MAX_REDIS_CONNECTIONS = "max-redis-connections";
  public static final String PASSWORD = "password";
  public static final String PORT = "port";
  public static final String URL = "url";
  public static final String USERNAME = "username";
  
  /* ================================
   * >> Config properties
   * ================================ */

  private Map<String, Map<String, Object>> databases = new HashMap<>();

  public static Entry<String, Map<String, Object>> getMysqlDefault() {
    return new SimpleEntry<>(MYSQL, ImmutableMap.<String, Object>builder()
        .put("enabled", false)
        .put("ip", "localhost")
        .put("port", 3306)
        .put("username", "user")
        .put("password", "password")
        .put("database", "test")
        .build());
  }

  public static Entry<String, Map<String, Object>> getRedisDefault() {
    return new SimpleImmutableEntry<>(REDIS, ImmutableMap.<String, Object>builder()
        .put("enabled", false)
        .put("ip", "localhost")
        .put("port", 6379)
        .put("password", "password")
        .put("max-redis-connections", "test")
        .build());
  }

  protected DatabaseSettings() {}

  public DatabaseSettings(@Nonnull Map<String, Map<String, Object>> databases) {
    checkNotNull(databases, "databases cannot be null.");
    checkArgument(!databases.isEmpty(), "databases cannot be empty.");
    this.databases = databases;
  }

  public Map<String, Map<String, Object>> getDatabases() {
    return Collections.unmodifiableMap(databases);
  }

  public void setDatabases(Map<String, Map<String, Object>> databases) {
    this.databases = databases;
  }

  protected Map<String, Object> getDatabaseMap(@Nonnull String database) {
    Preconditions.checkNotNull(database, "database cannot be null.");
    Map<String, Object> dbMap = this.databases.get(database);
    Preconditions.checkArgument(dbMap != null, database + " not defined.");
    return dbMap;
  }

  public Optional<Boolean> isEnabled(@Nonnull String database) throws IllegalArgumentException {
    return getString(database, ENABLED).flatMap(StringUtils::parseBoolean);
  }

  @Nonnull
  public Optional<String> getString(@Nonnull String database, @Nonnull String key) {
    Map<String, Object> dbMap = getDatabaseMap(database);
    Preconditions.checkNotNull(key, "key cannot be null.");
    Object foundKey = dbMap.get(key);
    return Optional.ofNullable(foundKey).map(Object::toString);
  }
}
