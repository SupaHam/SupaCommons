package com.supaham.commons.database;

/**
 * Represents a {@link Long} type extension of the {@link DBID} interface.
 *
 * @since 0.1
 */
public interface DBLongID extends DBID<Long> {

  /**
   * {@inheritDoc}
   */
  @Override
  Long getDatabaseId();
}
