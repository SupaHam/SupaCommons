package com.supaham.commons;

/**
 * Represents a class that is identifiable by a {@link String}.
 *
 * @since 0.1
 */
public interface IdentifiableByString extends Identifiable<String> {

  /**
   * Gets the string id of this {@link IdentifiableByString} object.
   *
   * @return id of this object
   */
  String getId();
}
