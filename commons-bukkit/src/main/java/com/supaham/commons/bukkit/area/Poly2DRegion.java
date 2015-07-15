package com.supaham.commons.bukkit.area;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import com.supaham.commons.bukkit.serializers.CBSerializers.ListVectorSerializer;
import com.supaham.commons.bukkit.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializableAs;
import pluginbase.config.annotation.SerializeWith;

/**
 * Represents a serializable 2D polygonal object using a collection of points to define the region
 * followed by a min & max y coordinate to define height.
 *
 * @see #fromLocations(Collection, int, int)
 * @see #Poly2DRegion(Collection, int, int)
 * @since 0.2.3
 */
@SerializableAs("Poly2DRegion")
public class Poly2DRegion implements Region {

  @SerializeWith(ListVectorSerializer.class)
  private Collection<Vector> points;
  private Vector min = new Vector(0, 0, 0);
  private Vector max = new Vector(0, 0, 0);

  /**
   * Constructs a 2D polygonal region out of a collection of {@link Location}s and a min & max y
   * coordinate. This is equivalent to calling {@link #Poly2DRegion(Collection, int, int)} with
   * the collection transformed to {@link LocationUtils#toVectorFunction()}.
   *
   * @param points points to define the polygonal region
   * @param minY minimum level on the y axis
   * @param maxY maximum level on the y axis
   *
   * @return 2D polygonal region
   *
   * @see #Poly2DRegion(Collection, int, int)
   */
  public static Poly2DRegion fromLocations(@Nonnull Collection<Location> points, int minY,
                                           int maxY) {
    Preconditions.checkNotNull(points, "first point cannot be null.");
    Preconditions.checkArgument(minY >= 0, "minY cannot be smaller than 0.");
    Preconditions.checkArgument(maxY >= 0, "maxY cannot be smaller than 0.");
    return new Poly2DRegion(Collections2.transform(points, LocationUtils.toVectorFunction()),
                            minY, maxY);
  }

  protected Poly2DRegion() {}

  /**
   * Constructs a 2D polygonal region out of a collection of {@link Vector}s and a min & max y
   * coordinate.
   *
   * @param points points to define the polygonal region
   * @param minY minimum level on the y axis
   * @param maxY maximum level on the y axis
   *
   * @see #fromLocations(Collection, int, int)
   */
  public Poly2DRegion(@Nonnull Collection<Vector> points, int minY, int maxY) {
    Preconditions.checkNotNull(points, "points cannot be null.");
    Preconditions.checkArgument(minY >= 0, "minY cannot be smaller than 0.");
    Preconditions.checkArgument(maxY >= 0, "maxY cannot be smaller than 0.");
    this.points = points;
    Iterator<Vector> it = points.iterator();
    int minX = Iterables.get(points, 0).getBlockX();
    int minZ = Iterables.get(points, 0).getBlockZ();
    int maxX = minX;
    int maxZ = minZ;

    while (it.hasNext()) {
      Vector next = it.next();
      int x = next.getBlockX();
      int z = next.getBlockZ();
      if (x < minX) {
        minX = x;
      } else if (x > maxX) {
        maxX = x;
      }
      if (z < minZ) {
        minZ = z;
      } else if (z > maxZ) {
        maxZ = z;
      }
    }
  }

  @Override public Vector getMinimumPoint() {
    return this.min;
  }

  @Override public Vector getMaximumPoint() {
    return this.max;
  }

  /*
   * This piece of code was taken from Sk89q's WorldEdit project licensed under GNU GPL v3, and
   * slightly modified.
   * https://github.com/sk89q/WorldEdit/blob/master/worldedit-core/src/main/java/com/sk89q/worldedit/regions/Polygonal2DRegion.java
   */
  @Override public boolean contains(@Nonnull Vector vector) {
    if (this.points.size() < 3) {
      return false;
    }
    int targetX = vector.getBlockX(); //wide
    int targetY = vector.getBlockY(); //height
    int targetZ = vector.getBlockZ(); //depth

    if (targetY < this.min.getBlockY() || targetY > this.max.getBlockY()) {
      return false;
    }

    boolean inside = false;
    int npoints = this.points.size();
    int xNew, zNew;
    int xOld, zOld;
    int x1, z1;
    int x2, z2;
    long crossproduct;
    int i;

    xOld = Iterables.get(this.points, npoints - 1).getBlockX();
    zOld = Iterables.get(this.points, npoints - 1).getBlockZ();

    for (Vector point : this.points) {
      xNew = point.getBlockX();
      zNew = point.getBlockZ();
      //Check for corner
      if (xNew == targetX && zNew == targetZ) {
        return true;
      }
      if (xNew > xOld) {
        x1 = xOld;
        x2 = xNew;
        z1 = zOld;
        z2 = zNew;
      } else {
        x1 = xNew;
        x2 = xOld;
        z1 = zNew;
        z2 = zOld;
      }
      if (x1 <= targetX && targetX <= x2) {
        crossproduct = ((long) targetZ - (long) z1) * (long) (x2 - x1)
                       - ((long) z2 - (long) z1) * (long) (targetX - x1);
        if (crossproduct == 0) {
          if ((z1 <= targetZ) == (targetZ <= z2)) {
            return true; //on edge
          }
        } else if (crossproduct < 0 && (x1 != targetX)) {
          inside = !inside;
        }
      }
      xOld = xNew;
      zOld = zNew;
    }
    return inside;
  }
}
