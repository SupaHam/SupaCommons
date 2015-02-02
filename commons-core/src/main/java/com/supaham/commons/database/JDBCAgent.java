package com.supaham.commons.database;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

/**
 * Represents a JDBC agent that can provide a {@link DataSource}.
 */
public interface JDBCAgent {
  @Nonnull
  DataSource getDataSource();
}
