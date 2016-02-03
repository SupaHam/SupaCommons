package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.utils.RelativeVector;
import com.supaham.commons.bukkit.utils.VectorUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public class RelativeVectorSerializer implements Serializer<RelativeVector> {

  @Nullable @Override public Object serialize(@Nullable RelativeVector object, @NotNull SerializerSet serializerSet)
      throws IllegalArgumentException {
    return VectorUtils.serializeRelative(object);
  }

  @Nullable @Override public RelativeVector deserialize(@Nullable Object serialized, @NotNull Class wantedType,
                                                        @NotNull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    return VectorUtils.deserializeRelative(serialized.toString());
  }
}
