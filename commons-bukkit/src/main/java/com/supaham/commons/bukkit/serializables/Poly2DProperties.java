package com.supaham.commons.bukkit.serializables;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import com.supaham.commons.bukkit.serializers.CBSerializers.ListVectorSerializer;
import com.supaham.commons.bukkit.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Collection;

import javax.annotation.Nonnull;

import lombok.Getter;
import pluginbase.config.annotation.Name;
import pluginbase.config.annotation.SerializeWith;

/**
 * Represents a serializable 2D polygonal object using a collection of points to define the region
 * followed by a min & max y coordinate to define height.
 *
 * @see #fromLocations(Collection, int, int)
 * @see #Poly2DProperties(Collection, int, int)
 * @since 0.2.3
 */
@Name("Poly2DRegion")
@Getter
public class Poly2DProperties {

  @SerializeWith(ListVectorSerializer.class)
  private Collection<Vector> points;
  private int minY;
  private int maxY;
  
  protected Poly2DProperties() {}

  /**
   * Constructs a 2D polygonal region out of a collection of {@link Location}s and a min & max y
   * coordinate. This is equivalent to calling {@link #Poly2DProperties(Collection, int, int)} with
   * the collection transformed to {@link LocationUtils#toVectorFunction()}.
   *
   * @param points points to define the polygonal region
   * @param minY minimum level on the y axis
   * @param maxY maximum level on the y axis
   *
   * @return 2D polygonal region
   *
   * @see #Poly2DProperties(Collection, int, int)
   */
  public static Poly2DProperties fromLocations(@Nonnull Collection<Location> points, int minY,
                                               int maxY) {
    Preconditions.checkNotNull(points, "first point cannot be null.");
    Preconditions.checkArgument(minY >= 0, "minY cannot be smaller than 0.");
    Preconditions.checkArgument(maxY >= 0, "maxY cannot be smaller than 0.");
    return new Poly2DProperties(Collections2.transform(points, LocationUtils.toVectorFunction()),
                                minY, maxY);
  }

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
  public Poly2DProperties(@Nonnull Collection<Vector> points, int minY, int maxY) {
    Preconditions.checkNotNull(points, "points cannot be null.");
    Preconditions.checkArgument(minY >= 0, "minY cannot be smaller than 0.");
    Preconditions.checkArgument(maxY >= 0, "maxY cannot be smaller than 0.");
    this.points = points;
    this.minY = minY;
    this.maxY = maxY;
  }
}
