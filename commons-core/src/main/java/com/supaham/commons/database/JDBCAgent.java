package com.supaham.commons.database;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

/**
 * Represents a JDBC agent that can provide a {@link DataSource}.
 */
public interface JDBCAgent {

  /**
   * Gets this {@link JDBCAgent}'s {@link DataSource}.
   *
   * @return {@link DataSource}
   */
  @Nonnull
  DataSource getDataSource();
}
