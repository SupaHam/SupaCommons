package com.supaham.commons.database;

import com.supaham.commons.Identifiable;

/**
 * Represents a class that is identifiable by a database id. This interface extends
 * {@link Identifiable}.
 * 
 * @since 0.1
 */
public interface DBID<T> {

  /**
   * Unique auto incremental id supplied through the database.
   */
  T getDatabaseId();
}
