package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.utils.NumberUtils.roundExact;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;
import static java.lang.Double.parseDouble;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Location} instances. This class contains methods such as
 * {@link #deserialize(String)}, {@link #serialize(Location)}, and more.
 *
 * @since 0.1
 */
public class LocationUtils {

  /**
   * Deserializes a {@link String} to represent a {@link Location}. <p>
   * LocationUtils.deserialize("world 123.0 64.0 124.5") = {@link Location}(world, 123.0D, 64.0D,
   * 124.5D) <br />
   *
   * LocationUtils.deserialize("world 123.0 64.0 124.5 1.2") = {@link Location}(world, 123.0D,
   * 64.0D, 124.5D, 1.2F)<br />
   *
   * LocationUtils.deserialize("world 123.0 64.0 124.5 1.2 20") = {@link Location}(world, 123.0D,
   * 64.0D, 124.5D, 1.2F, 20F)<br />
   *
   * LocationUtils.deserialize("world 123.0 64.0") = {@link IllegalArgumentException} too few args
   * <br />
   *
   * LocationUtils.deserialize("world 123.0 64.0 124.5 1.2 20 66") = {@link
   * IllegalArgumentException} too many args <br /> </p>
   *
   * @param string string representing to deserialize
   *
   * @return returns the deserialized {@link Location}
   *
   * @throws NullPointerException thrown if the world in the {@code string} is null
   * @throws IllegalArgumentException thrown if the {@code string} is in the incorrect format
   */
  @Nonnull
  public static Location deserialize(@Nonnull String string) throws NullPointerException {
    checkNotNullOrEmpty(string);
    String[] split = string.split("\\s+");
    checkArgument(split.length >= 4 && split.length <= 6, "string is in an invalid format.");
    World world = Bukkit.getWorld(split[0]);
    checkNotNull(world, "world '" + split[0] + "' doesn't exist.");
    Location loc = new Location(world, parseDouble(split[1]), parseDouble(split[2]),
                                parseDouble(split[3]));
    if (split.length > 4) {
      loc.setYaw(Float.parseFloat(split[4]));
    }
    if (split.length > 5) {
      loc.setPitch(Float.parseFloat(split[5]));
    }
    return loc;
  }

  /**
   * Serializes a {@link Location} in the form of 'world x y z yaw pitch'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places. The yaw and the pitch are rounded to
   * <em>three</em> decimal places. If the yaw and/or pitch are true but their values are ZERO, they
   * will be set to false, removing it from the serialized string that is returned.
   * <p/>
   * <b>NOTE</b>: This is equivalent to calling {@code serialize(Location, true)}
   *
   * @param location location to serialize
   *
   * @return serialized {@code location}
   * @see #serialize(Location, boolean)
   */
  public static String serialize(Location location) {
    return serialize(location, true);
  }

  /**
   * Serializes a {@link Location} in the form of 'world x y z yaw pitch'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places. The yaw and the pitch are rounded to
   * <em>three</em> decimal places. If the yaw and/or pitch are true but their values are ZERO, they
   * will be set to false, removing it from the serialized string that is returned.
   * <p/>
   * <b>NOTE</b>: This is equivalent to calling {@code serialize(Location, direction, direction)}
   *
   * @param location location to serialize
   * @param direction whether to store the direction (yaw and pitch)
   *
   * @return serialized {@code location}
   * @see #serialize(Location, boolean, boolean)
   */
  public static String serialize(Location location, boolean direction) {
    return serialize(location, direction, direction);
  }

  /**
   * Serializes a {@link Location} in the form of 'world x y z yaw pitch'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places. The yaw and the pitch are rounded to
   * <em>three</em> decimal places. If the yaw and/or pitch are true but their values are ZERO, they
   * will be set to false, removing it from the serialized string that is returned.
   *
   * @param location location to serialize
   * @param yaw whether to store the yaw, keep in mind this will be forcefully set to true if the
   * {@code pitch} is true
   * @param pitch whether to store the pitch
   *
   * @return serialized {@code location}
   */
  public static String serialize(Location location, boolean yaw, boolean pitch) {
    pitch = pitch && location.getPitch() > 0;
    yaw = pitch || (yaw && location.getYaw() > 0);
    return location.getWorld().getName() + " "
           + VectorUtils.serialize(location.toVector()) // x y z
           + (yaw ? " " + roundExact(3, location.getYaw()) : "")
           + (pitch ? " " + roundExact(3, location.getPitch()) : "");
  }
}
