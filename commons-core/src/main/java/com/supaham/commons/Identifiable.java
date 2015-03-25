package com.supaham.commons;

/**
 * Represents a class that is identifiable.
 *
 * @since 0.1
 */
public interface Identifiable<T> {

  /**
   * Gets the id of this {@link Identifiable} object.
   *
   * @return id of this object
   */
  T getId();
}
