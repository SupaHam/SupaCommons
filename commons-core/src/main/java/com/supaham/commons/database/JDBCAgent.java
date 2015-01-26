package com.supaham.commons.database;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

/**
 * Represents a JDBC agent that can provide a {@link DataSource}.
 */
public interface JDBCAgent {
  @NotNull
  DataSource getDataSource();
}
