package com.supaham.commons.database;

/**
 * Represents a database object represented by an {@link Integer}.
 *
 * @since 0.1
 */
public interface DBIntegerID {

  /**
   * Unique auto incremental id supplied through the database.
   */
  int getDatabaseId();
}
