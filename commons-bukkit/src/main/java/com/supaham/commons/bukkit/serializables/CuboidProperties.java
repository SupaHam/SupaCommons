package com.supaham.commons.bukkit.serializables;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.serializers.VectorSerializer;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

import lombok.Getter;
import pluginbase.config.annotation.Name;
import pluginbase.config.annotation.SerializeWith;

/**
 * Represents a serializable cuboid object with a minimum and maximum {@link Vector} each
 * representing the minimum point of the cuboid and the maximum point of the cuboid respectively.
 *
 * @see #CuboidProperties(Location, Location)
 * @see #CuboidProperties(Vector, Vector)
 * @since 0.2.3
 */
@Name("CuboidRegion")
@Getter
public class CuboidProperties {

  @SerializeWith(VectorSerializer.class)
  private Vector min;
  @SerializeWith(VectorSerializer.class)
  private Vector max;

  protected CuboidProperties() {}

  /**
   * Constructs a cuboid region out of two {@link Location}s. During construction, both are checked
   * for the minimum and maximum locations and write to the instance's {@link
   * CuboidProperties#getMin()} and {@link CuboidProperties#getMax()} respectively.
   *
   * @param point1 first point
   * @param point2 second point
   */
  public CuboidProperties(@Nonnull Location point1, @Nonnull Location point2) {
    this(Preconditions.checkNotNull(point1, "first point cannot be null.").toVector(),
         Preconditions.checkNotNull(point2, "second point cannot be null.").toVector());
  }

  /**
   * Constructs a cuboid region out of two {@link Vector}s. During construction, both are checked
   * for the minimum and maximum vectors and write to the instance's {@link
   * CuboidProperties#getMin()} and {@link CuboidProperties#getMax()} respectively.
   *
   * @param point1 first point
   * @param point2 second point
   */
  public CuboidProperties(@Nonnull Vector point1, @Nonnull Vector point2) {
    Preconditions.checkNotNull(point1, "first point cannot be null.");
    Preconditions.checkNotNull(point2, "second point cannot be null.");
    this.min = Vector.getMinimum(point1, point2);
    this.max = Vector.getMaximum(point1, point2);
  }
}
