package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.utils.NumberUtils.roundExact;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;
import static java.lang.Double.parseDouble;

import com.google.common.base.Function;

import com.supaham.commons.utils.RandomUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    String[] split = string.split("\\s+|,");
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
   * <em>three</em> decimal places. If the yaw and/or pitch are true but their values are ZERO,
   * they
   * will be set to false, removing it from the serialized string that is returned. <p/>
   * <b>NOTE</b>: This is equivalent to calling {@code serialize(Location, true)}
   *
   * @param location location to serialize
   *
   * @return serialized {@code location}
   *
   * @see #serialize(Location, boolean)
   */
  public static String serialize(Location location) {
    return serialize(location, true);
  }

  /**
   * Serializes a {@link Location} in the form of 'world x y z yaw pitch'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places. The yaw and the pitch are rounded to
   * <em>three</em> decimal places. If the yaw and/or pitch are true but their values are ZERO,
   * they
   * will be set to false, removing it from the serialized string that is returned. <p/>
   * <b>NOTE</b>: This is equivalent to calling {@code serialize(Location, direction, direction)}
   *
   * @param location location to serialize
   * @param direction whether to store the direction (yaw and pitch)
   *
   * @return serialized {@code location}
   *
   * @see #serialize(Location, boolean, boolean)
   */
  public static String serialize(Location location, boolean direction) {
    return serialize(location, direction, direction);
  }

  /**
   * Serializes a {@link Location} in the form of 'world x y z yaw pitch'. The x, y, and z
   * coordinates are rounded to <em>two</em> decimal places. The yaw and the pitch are rounded to
   * <em>three</em> decimal places. If the yaw and/or pitch are true but their values are ZERO,
   * they
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
    return location.getWorld().getName() + ","
           + VectorUtils.serialize(location.toVector()) // x y z
           + (yaw ?   "," + roundExact(3, location.getYaw()) : "")
           + (pitch ? "," + roundExact(3, location.getPitch()) : "");
  }

  /**
   * Returns a new random {@link Location} that is within two given Locations. This is equivalent
   * to
   * calling #getRandomLocationWithin(Location, Location, boolean) with the boolean as false.
   *
   * @param min minimum location of a cuboid region
   * @param max maximum location of a cuboid region
   *
   * @return a pseudorandom location
   *
   * @see #getRandomLocationWithin(Location, Location, boolean)
   */
  public static Location getRandomLocationWithin(Location min, Location max) {
    return getRandomLocationWithin(min, max, false);
  }

  /**
   * Returns a new random {@link Location} that is within two given Locations. This is equivalent
   * to
   * calling #getRandomLocationWithin(Random, Location, Location, boolean) using {@link
   * RandomUtils#getRandom()} and the boolean as false.
   *
   * @param min minimum location of a cuboid region
   * @param max maximum location of a cuboid region
   * @param highestBlock whether to immediately call {@link World#getHighestBlockAt(Location)} to
   * attach the pseudorandom location to ground
   *
   * @return a pseudorandom location
   *
   * @see #getRandomLocationWithin(Random, Location, Location, boolean)
   */
  public static Location getRandomLocationWithin(@Nonnull Location min, @Nonnull Location max,
                                                 boolean highestBlock) {
    return getRandomLocationWithin(RandomUtils.getRandom(), min, max, highestBlock);
  }

  /**
   * Returns a new random {@link Location} that is within two given Locations. This is equivalent
   * to
   * calling #getRandomLocationWithin(Random, Location, Location, boolean) with the boolean as
   * false.
   *
   * @param random random instance to use
   * @param min minimum location of a cuboid region
   * @param max maximum location of a cuboid region
   *
   * @return a pseudorandom location
   *
   * @see #getRandomLocationWithin(Random, Location, Location, boolean)
   */
  public static Location getRandomLocationWithin(@Nonnull Random random, @Nonnull Location min,
                                                 @Nonnull Location max) {
    return getRandomLocationWithin(random, min, max, false);
  }

  /**
   * Returns a new random {@link Location} that is within two given Locations.
   *
   * @param random random instance to use
   * @param min minimum location of a cuboid region
   * @param max maximum location of a cuboid region
   * @param highestBlock whether to immediately call {@link World#getHighestBlockAt(Location)} to
   * attach the pseudorandom location to ground
   *
   * @return a pseudorandom location
   */
  public static Location getRandomLocationWithin(@Nonnull Random random, @Nonnull Location min,
                                                 @Nonnull Location max, boolean highestBlock) {
    checkNotNull(random, "random cannot be null.");
    checkNotNull(min, "min cannot be null.");
    checkNotNull(max, "max cannot be null.");

    checkArgument(min.getWorld().equals(max.getWorld()), "min and max worlds don't match.");
    Location loc = new Location(min.getWorld(),
                                RandomUtils.nextInt(min.getBlockX(), max.getBlockX()),
                                RandomUtils.nextInt(min.getBlockY(), max.getBlockY()),
                                RandomUtils.nextInt(min.getBlockZ(), max.getBlockZ()));
    return !highestBlock ? loc : loc.getWorld().getHighestBlockAt(loc).getLocation();
  }

  /**
   * Returns a new {@link Location} where at least the specified required amount of space is
   * available above it. This method works its way from bottom to top, starting from the given
   * location's y coordinate and all the way to the location's world's max height value.
   *
   * @param location location to start from
   * @param required required amount of space
   *
   * @return a location with the required free space
   */
  public static Location getFreeLocation(@Nonnull Location location, double required) {
    checkNotNull(location, "location cannot be null.");
    checkArgument(required > 0, "required space must be larger than 0.");
    location = location.clone();
    if (location.getBlockY() >= location.getWorld().getMaxHeight()) {
      return location;
    }
    World world = location.getWorld();
    int y = location.getBlockY();
    int free = 0;
    while (y < world.getMaxHeight()) {
      if (!world.getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType().isSolid()) {
        free++;
      } else {
        free = 0;
      }
      if (free >= required) {
        break;
      }
      y++;
    }
    location.setY((double) y - required);
    return location;
  }

  /**
   * Checks if two {@link Location} instances are within the same block. If both of them are null,
   * true is returned. If either one is null, false is returned.
   *
   * @param l1 first location to match with {@code l2}
   * @param l2 second location to match with {@code l1}
   *
   * @return whether both locations are within the same block
   *
   * @see VectorUtils#isSameBlock(Vector, Vector)
   */
  public static boolean isSameBlock(@Nullable Location l1, @Nullable Location l2) {
    return VectorUtils.isSameBlock(l1 != null ? l1.toVector() : null,
                                   l2 != null ? l2.toVector() : null);
  }

  /**
   * Checks if two {@link Location} instances are the same coordinates. If both of them are null,
   * true is returned. If either one is null, false is returned.
   *
   * @param l1 first location to match with {@code l2}, nullable
   * @param l2 second location to match with {@code l1}, nullable
   *
   * @return whether both locations are the same coordinates
   */
  public static boolean isSameCoordinates(@Nullable Location l1, @Nullable Location l2) {
    if (l1 == null && l2 == null) {
      return true;
    } else if (l1 == null || l2 == null) {
      return false;
    }
    return l1.getX() == l2.getX() && l1.getY() == l2.getY() && l1.getZ() == l2.getZ();
  }

  /**
   * Gets a {@link Function} which converts a {@link Location} into {@link Vector} by calling
   * {@link Location#toVector()}.
   *
   * @return function
   */
  public static Function<Location, Vector> toVectorFunction() {
    return ToVectorFunction.INSTANCE;
  }

  private static final class ToVectorFunction implements Function<Location, Vector> {

    public static final ToVectorFunction INSTANCE = new ToVectorFunction();

    @Nullable
    @Override
    public Vector apply(@Nullable Location input) {
      return input == null ? null : input.toVector();
    }
  }
}
