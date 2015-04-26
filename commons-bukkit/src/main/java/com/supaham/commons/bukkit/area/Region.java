package com.supaham.commons.bukkit.area;

import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * Represents an object with a predefined volume using two {@link Vector}s.
 * @since 0.2.3
 */
public interface Region {

  /**
   * Returns the minimum point of this extent.
   *
   * @return minimum point
   */
  Vector getMinimumPoint();

  /**
   * Returns the maximum point of this extent.
   *
   * @return maximum point
   */
  Vector getMaximumPoint();

  /**
   * Returns whether a {@link Vector} is within this extent's boundaries.
   *
   * @param vector vector to test
   *
   * @return whether the vector is within this extent
   */
  boolean contains(@Nonnull Vector vector);
}
