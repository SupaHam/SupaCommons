package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.utils.VectorUtils;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * A {@link Vector} serializer that simply calls {@link VectorUtils#serialize(Vector)} in return.
 *
 * @since 0.1
 */
public class VectorSerializer implements Serializer<Vector> {

  @Nullable
  @Override
  public Object serialize(@Nullable Vector object, @Nonnull SerializerSet serializerSet) {
    return object == null ? null : VectorUtils.serialize(object);
  }

  @Nullable
  @Override
  public Vector deserialize(@Nullable Object serialized, @NotNull Class wantedType,
                            @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    return serialized == null ? null : VectorUtils.deserialize(serialized.toString());
  }
}
