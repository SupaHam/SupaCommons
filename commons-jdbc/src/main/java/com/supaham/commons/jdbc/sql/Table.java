package com.supaham.commons.jdbc.sql;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import javax.annotation.Nonnull;

/**
* Represents a SQL table with a name and a schema as a {@link String}.
*/
public class Table {

  public static final String NO_SCHEMA = "NO_SCHEMA_PROVIDED";
  private final String name;
  private final String schema;

  public Table(@Nonnull String name, @Nonnull String schema) {
    checkNotNullOrEmpty(name, "name");
    checkNotNullOrEmpty(schema, "schema");
    this.name = name;
    this.schema = schema;
  }

  public String getName() {
    return name;
  }

  public String getSchema() {
    return schema;
  }
}
