package com.supaham.commons.database;

/**
 * Represents an {@link Integer} type extension of the {@link DBID} interface.
 *
 * @since 0.1
 */
public interface DBIntegerID extends DBID<Integer> {

  /**
   * {@inheritDoc}
   */
  @Override
  Integer getDatabaseId();
}
