package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.supaham.commons.utils.NumberUtils.roundExact;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;
import static java.lang.Double.parseDouble;

import com.google.common.base.Preconditions;

import com.supaham.commons.utils.RandomUtils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link Vector} instances. This class contains methods such as
 * {@link #deserialize(String)}, {@link #serialize(Vector)}, and more.
 *
 * @since 0.1
 */
public class VectorUtils {

  private static final Pattern DESERIALIZE = Pattern.compile("\\s*,\\s*");

  /**
   * Deserializes a {@link String} to represent a {@link Vector}. <p>
   * VectorUtils.deserialize("123.0,64.0,124.5") = {@link Vector}(123.0D, 64.0D, 124.5D) <br />
   *
   * VectorUtils.deserialize("123.0,64.0") = {@link IllegalArgumentException} too few args
   * <br />
   *
   * VectorUtils.deserialize("123.0,64.0,124.5") = {@link IllegalArgumentException} too many args <br /> </p>
   *
   * @param string string representing to deserialize
   *
   * @return returns the deserialized {@link Location}
   *
   * @throws NullPointerException thrown if the world in the {@code string} is null
   * @throws IllegalArgumentException thrown if the {@code string} is in the incorrect format
   */
  @Nonnull
  public static Vector deserialize(@Nonnull String string) throws NullPointerException {
    checkNotNullOrEmpty(string);
    String[] split = DESERIALIZE.split(string);
    checkArgument(split.length == 3, string + " is in an invalid format.");
    return new Vector(parseDouble(split[0]), parseDouble(split[1]), parseDouble(split[2]));
  }

  /**
   * Serializes a {@link Vector} in the form of 'x,y,z'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places.
   *
   * @param vector vector to serialize
   *
   * @return serialized {@code vector}
   */
  public static String serialize(Vector vector) {
    if (vector == null) {
      return null;
    }
    return roundExact(2, vector.getX()) + ","
           + roundExact(2, vector.getY()) + ","
           + roundExact(2, vector.getZ());
  }

  public static Vector getRandomVectorWithin(Vector min, Vector max) {
    return new Vector(RandomUtils.nextInt(min.getBlockX(), max.getBlockX()),
                      RandomUtils.nextInt(min.getBlockY(), max.getBlockY()),
                      RandomUtils.nextInt(min.getBlockY(), max.getBlockY()));
  }

  /**
   * Checks if a {@link Vector} is within two other {@link Vector}s.
   *
   * @param test vector to test.
   * @param min minimum point of a cuboid region.
   * @param max maximum point of a cuboid region.
   *
   * @return whether the {@code test} vector is within the {@code min} and {@code max} vectors.
   */
  public static boolean isWithin(Vector test, Vector min, Vector max) {
    Preconditions.checkNotNull(test);
    Preconditions.checkNotNull(min);
    Preconditions.checkNotNull(max);

    double x = test.getX();
    double y = test.getY();
    double z = test.getZ();

    return x >= min.getBlockX() && x < max.getBlockX() + 1 &&
           y >= min.getBlockY() && y < max.getBlockY() + 1 &&
           z >= min.getBlockZ() && z < max.getBlockZ() + 1;
  }

  /**
   * Checks if two {@link Vector} instances are within the same block. If both of them are null,
   * true is returned.
   *
   * @param o first {@link Vector} to check
   * @param o2 second {@link Vector} to check
   *
   * @return true if {@code o} and {@code o2} are the same block
   */
  public static boolean isSameBlock(@Nullable Vector o, @Nullable Vector o2) {
    return o == null && o2 == null ||
           (o != null && o2 != null) && (o.getBlockX() == o2.getBlockX()) && (o.getBlockY() == o2
               .getBlockY()) &&
           (o.getBlockZ() == o2.getBlockZ());
  }
  
  /* ================================
   * >> Relative Vectors
   * ================================ */

  /**
   * Deserializes a {@link String} to represent a {@link RelativeVector}. <p>
   * VectorUtils.deserializeRelative("123.0, 64.0, 124.5") = {@link RelativeVector}(123.0D, 64.0D, 124.5D, false, false, false)
   * <br />
   * VectorUtils.deserializeRelative("~123.0, 64.0, 124.5") = {@link RelativeVector}(123.0D, 64.0D, 124.5D, true, false, false)
   * <br />
   * VectorUtils.deserializeRelative("~123.0, ~64.0, 124.5") = {@link RelativeVector}(123.0D, 64.0D, 124.5D, true, true, false)
   * <br />
   * VectorUtils.deserializeRelative("~123.0, ~64.0, ~124.5") = {@link RelativeVector}(123.0D, 64.0D, 124.5D, true, true, true)
   * <br />
   *
   * VectorUtils.deserializeRelative("123.0,64.0") = {@link IllegalArgumentException} too few args
   * <br />
   *
   * VectorUtils.deserializeRelative("123.0,64.0,124.5") = {@link IllegalArgumentException} too many args <br /> </p>
   *
   * @param string string representing to deserialize
   *
   * @return returns the deserialized {@link Location}
   *
   * @throws NullPointerException thrown if the world in the {@code string} is null
   * @throws IllegalArgumentException thrown if the {@code string} is in the incorrect format
   * @see #deserialize(String)
   */
  @Nonnull
  public static RelativeVector deserializeRelative(@Nonnull String string) throws NullPointerException {
    checkNotNullOrEmpty(string);
    String[] split = DESERIALIZE.split(string);
    checkArgument(split.length == 3, string + " is in an invalid format.");

    boolean xRel = split[0].startsWith("~");
    boolean yRel = split[1].startsWith("~");
    boolean zRel = split[2].startsWith("~");
    double x = parseDouble(split[0].substring(xRel ? 1 : 0));
    double y = parseDouble(split[1].substring(yRel ? 1 : 0));
    double z = parseDouble(split[2].substring(zRel ? 1 : 0));
    return new RelativeVector(x, y, z, xRel, yRel, zRel);
  }

  /**
   * Serializes a {@link RelativeVector} in the form of '~x,~y,~z'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places. The tildes are only inserted if the component is relative.
   *
   * @param vector vector to serialize
   *
   * @return serialized {@code vector}
   */
  public static String serializeRelative(RelativeVector vector) {
    if (vector == null) {
      return null;
    }
    return (vector.isXRelative() ? "~" : "") + roundExact(2, vector.getX()) + ","
           + (vector.isYRelative() ? "~" : "") + roundExact(2, vector.getY()) + ","
           + (vector.isZRelative() ? "~" : "") + roundExact(2, vector.getZ());
  }
}
