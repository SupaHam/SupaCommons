package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.utils.VectorUtils;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pluginbase.config.serializers.Serializer;

/**
 * A {@link Vector} serializer that simply calls {@link VectorUtils#serialize(Vector)} in return.
 *
 * @since 0.1
 */
public class VectorSerializer implements Serializer<Vector> {

  @Nullable
  @Override
  public Object serialize(@Nullable Vector object) {
    if (object == null) {
      return null;
    }
    return VectorUtils.serialize(object);
  }

  @Nullable
  @Override
  public Vector deserialize(@Nullable Object serialized, @NotNull Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }
    return VectorUtils.deserialize(serialized.toString());
  }
}
