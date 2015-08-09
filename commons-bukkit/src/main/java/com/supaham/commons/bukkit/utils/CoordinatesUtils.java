package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.utils.NumberUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.annotation.NoTypeKey;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.Serializer;
import pluginbase.minecraft.location.Coordinates;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

/**
 * Utility methods for working with {@link Coordinates} instances. This class contains methods such
 * as {@link #coordsToLocation(Coordinates, World)}, {@link #locationToCoords(Location)}, and more.
 *
 * @since 0.2.9.1
 */
public class CoordinatesUtils {
  
  @Nonnull
  public static Object serialize(Coordinates coordinates) {
    if (coordinates == null) {
      return Locations.NULL_FACING;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(NumberUtils.roundExact(3, coordinates.getX())).append(" ");
    sb.append(NumberUtils.roundExact(3, coordinates.getY())).append(" ");
    sb.append(NumberUtils.roundExact(3, coordinates.getZ()));
    if (coordinates instanceof FacingCoordinates) {
      FacingCoordinates coords = (FacingCoordinates) coordinates;
      if (coords.getYaw() != 0 || coords.getPitch() != 0) {
        sb.append(" ").append(NumberUtils.roundExact(3, coords.getYaw())).append(" ");
        sb.append(NumberUtils.roundExact(3, coords.getPitch()));
      }
    }
    return sb.toString();
  }
  
  public static Coordinates deserialize(@Nullable Object serialized) throws 
                                                                     IllegalArgumentException {
    if (serialized instanceof String) {
      String[] split = serialized.toString().split(" ");
      double x = Double.valueOf(split[0]);
      double y = Double.valueOf(split[1]);
      double z = Double.valueOf(split[2]);
      if (split.length > 3) {
        float yaw = Float.valueOf(split[3]);
        float pitch = Float.valueOf(split[4]);
        return Locations.getFacingCoordinates(x, y, z, pitch, yaw);
      }
      return Locations.getCoordinates(x, y, z);
    } else {
      throw new IllegalArgumentException("Cannot deserialize coordinates from data: " + serialized);
    }
  }

  /**
   * Converts a {@link Coordinates} to a {@link Location}.
   *
   * @param coords coordinates to convert
   * @param world world to construct the location in.
   *
   * @return {@link Location}
   */
  public static Location coordsToLocation(@Nonnull Coordinates coords, @Nonnull World world) {
    Preconditions.checkNotNull(coords, "coordinates cannot be null.");
    Preconditions.checkNotNull(world, "world cannot be null.");
    float yaw = 0F;
    float pitch = 0F;
    if (coords instanceof FacingCoordinates) {
      yaw = ((FacingCoordinates) coords).getYaw();
      pitch = ((FacingCoordinates) coords).getPitch();
    }
    return new Location(world, coords.getX(), coords.getY(), coords.getZ(), yaw, pitch);
  }

  /**
   * Converts a {@link Coordinates} to a {@link Location}.
   *
   * @param coords coordinates to convert
   * @param world world to construct the location in.
   *
   * @return {@link Location}
   */
  public static Location facingCoordsToLocation(@Nonnull FacingCoordinates coords,
                                                @Nonnull World world) {
    Preconditions.checkNotNull(coords, "coordinates cannot be null.");
    Preconditions.checkNotNull(world, "world cannot be null.");
    return new Location(world, coords.getX(), coords.getY(), coords.getZ(), coords.getYaw(),
                        coords.getPitch());
  }

  public static FacingCoordinates locationToFacingCoords(@Nonnull Location loc) {
    Preconditions.checkNotNull(loc, "location cannot be null.");
    return Locations.getFacingCoordinates(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(),
                                          loc.getYaw());
  }

  public static Coordinates locationToCoords(@Nonnull Location loc) {
    Preconditions.checkNotNull(loc, "location cannot be null.");
    return Locations.getCoordinates(loc.getX(), loc.getY(), loc.getZ());
  }

  /**
   * Converts a {@link Vector} to a {@link Coordinates}.
   *
   * @param vector vector to convert
   *
   * @return converted {@link Coordinates}
   */
  public static Coordinates vectorToCoords(@Nonnull Vector vector) {
    Preconditions.checkNotNull(vector, "vector cannot be null.");
    return Locations.getCoordinates(vector.getX(), vector.getY(), vector.getZ());
  }

  private CoordinatesUtils() {
    throw new AssertionError("Are my static methods not good enough for you? Huh?");
  }

  @NoTypeKey
  @SerializeWith(CommonLocationSerializer.class)
  public static final class CommonLocation {

    private Coordinates coordinates;
    private transient Location location;

    protected CommonLocation() {} // Used by the serializer

    public CommonLocation(@Nonnull Coordinates coordinates) {
      this.coordinates = Preconditions.checkNotNull(coordinates, "coordinates cannot be null.");
    }

    public CommonLocation(@Nonnull Location location) {
      this.location = Preconditions.checkNotNull(location, "location cannot be null.");
      this.coordinates = locationToCoords(location);
    }

    public Location getLocation() {
      return this.location;
    }

    public Location getLocation(@Nonnull World world) {
      if (this.location == null) {
        Preconditions.checkNotNull(world, "world cannot be null.");
        this.location = coordsToLocation(this.coordinates, world);
      }
      return this.location;
    }

    public Coordinates getCoordinates() {
      return coordinates;
    }
  }

  public static class CommonLocationSerializer implements Serializer<CommonLocation> {

    @Nullable @Override public Object serialize(CommonLocation object)
        throws IllegalArgumentException {
      return object == null ? null : CoordinatesUtils.serialize(object.getCoordinates());
    }

    @Nullable @Override
    public CommonLocation deserialize(@Nullable Object serialized, @Nonnull Class wantedType)
        throws IllegalArgumentException {
      if (serialized == null) {
        return null;
      }
      return new CommonLocation(CoordinatesUtils.deserialize(serialized));
    }
  }
}
