package com.supaham.commons;

/**
 * Represents an interface for objects that can be named.
 * 
 * @since 0.1
 */
public interface Nameable {

  /**
   * Gets the name of this {@link Nameable} object.
   *
   * @return name of this object
   */
  String getName();

  /**
   * Sets the name of this {@link Nameable} object.
   *
   * @param name name to set
   *
   * @throws UnsupportedOperationException thrown if this object cannot be renamed
   */
  void setName(String name) throws UnsupportedOperationException;
}
