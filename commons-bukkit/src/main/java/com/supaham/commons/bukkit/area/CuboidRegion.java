package com.supaham.commons.bukkit.area;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.serializers.VectorSerializer;
import com.supaham.commons.bukkit.utils.VectorUtils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

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

  @Override public Iterator<Vector> iterator() {
    return new Iterator<Vector>() {
      private Vector min = CuboidRegion.this.getMinimumPoint();
      private Vector max = CuboidRegion.this.getMaximumPoint();
      private int nextX;
      private int nextY;
      private int nextZ;

      {
        this.nextX = this.min.getBlockX();
        this.nextY = this.min.getBlockY();
        this.nextZ = this.min.getBlockZ();
      }

      public boolean hasNext() {
        return this.nextX != -2147483648;
      }

      public Vector next() {
        if(!this.hasNext()) {
          throw new NoSuchElementException();
        } else {
          Vector answer = new Vector(this.nextX, this.nextY, this.nextZ);
          if(++this.nextX > this.max.getBlockX()) {
            this.nextX = this.min.getBlockX();
            if(++this.nextY > this.max.getBlockY()) {
              this.nextY = this.min.getBlockY();
              if(++this.nextZ > this.max.getBlockZ()) {
                this.nextX = -2147483648;
              }
            }
          }

          return answer;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
