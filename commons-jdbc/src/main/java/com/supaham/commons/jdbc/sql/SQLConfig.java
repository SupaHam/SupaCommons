package com.supaham.commons.jdbc.sql;

import java.util.Map;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializableAs;
import pluginbase.config.properties.PropertiesWrapper;

/**
 * Represents a MySQL configuration data class.
 */
@SerializableAs("SQLConfig")
public class SQLConfig extends PropertiesWrapper {

  private String file;
  private Map<String, String> tables;

  public SQLConfig() {}

  public SQLConfig(@Nonnull String file, @Nonnull Map<String, String> tables) {
    this.file = file;
    this.tables = tables;
  }

  public String getFile() {
    return file;
  }

  public Map<String, String> getTables() {
    return tables;
  }
}
