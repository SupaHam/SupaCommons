package com.supaham.commons.bukkit.area;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.serializers.VectorSerializer;
import com.supaham.commons.bukkit.utils.VectorUtils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializableAs;
import pluginbase.config.annotation.SerializeWith;

/**
 * Represents a serializable cuboid object with a minimum and maximum {@link Vector} each
 * representing the minimum point of the cuboid and the maximum point of the cuboid respectively.
 *
 * @see #CuboidRegion(Location, Location)
 * @see #CuboidRegion(Vector, Vector)
 * @since 0.2.3
 */
@SerializableAs("CuboidRegion")
public class CuboidRegion implements Region {

  @SerializeWith(VectorSerializer.class)
  private Vector min;
  @SerializeWith(VectorSerializer.class)
  private Vector max;

  protected CuboidRegion() {}

  /**
   * Constructs a cuboid region out of two {@link Location}s. During construction, both are checked
   * for the minimum and maximum locations and write to the instance's {@link
   * CuboidRegion#getMinimumPoint()} and {@link CuboidRegion#getMaximumPoint()}
   * respectively.
   *
   * @param point1 first point
   * @param point2 second point
   */
  public CuboidRegion(@Nonnull Location point1, @Nonnull Location point2) {
    this(Preconditions.checkNotNull(point1, "first point cannot be null.").toVector(),
         Preconditions.checkNotNull(point2, "second point cannot be null.").toVector());
  }

  /**
   * Constructs a cuboid region out of two {@link Vector}s. During construction, both are checked
   * for the minimum and maximum vectors and write to the instance's {@link
   * CuboidRegion#getMinimumPoint()} and {@link CuboidRegion#getMaximumPoint()}
   * respectively.
   *
   * @param point1 first point
   * @param point2 second point
   */
  public CuboidRegion(@Nonnull Vector point1, @Nonnull Vector point2) {
    Preconditions.checkNotNull(point1, "first point cannot be null.");
    Preconditions.checkNotNull(point2, "second point cannot be null.");
    this.min = Vector.getMinimum(point1, point2);
    this.max = Vector.getMaximum(point1, point2);
  }

  @Override public Vector getMinimumPoint() {
    return this.min;
  }

  @Override public Vector getMaximumPoint() {
    return this.max;
  }

  @Override public boolean contains(@Nonnull Vector vector) {
    return VectorUtils.isWithin(vector, this.min, this.max);
  }
}
