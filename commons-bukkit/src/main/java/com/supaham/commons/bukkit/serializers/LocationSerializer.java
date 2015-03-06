package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.utils.LocationUtils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pluginbase.config.serializers.Serializer;

/**
 * A {@link Location} serializer that simply calls {@link LocationUtils#serialize(Location)} in
 * return.
 *
 * @since 0.1
 */
public class LocationSerializer implements Serializer<Location> {

  @Nullable
  @Override
  public Object serialize(@Nullable Location object) {
    if (object == null) {
      return null;
    }
    return LocationUtils.serialize(object);
  }

  @Nullable
  @Override
  public Location deserialize(@Nullable Object serialized, @NotNull Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }
    return LocationUtils.deserialize(serialized.toString());
  }
}
