package com.supaham.commons.serializers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * Represents an abstract {@link Serializer} that takes a {@link List} of a specific type, used to
 * ease the process of storing types into lists.
 *
 * @param <T> type to serialize
 *
 * @since 0.1
 */
public abstract class ListSerializer<T> implements Serializer<List<T>> {

  /**
   * Gets the single {@link Serializer} of the given type implementation used by this {@link
   * ListSerializer} to serialize a list.
   *
   * @return
   */
  public abstract Class<T> getTypeClass();

  @Nullable
  @Override
  public Object serialize(List<T> list, @Nonnull SerializerSet serializerSet) {
    if (list == null) {
      return null;
    }
    Serializer<T> ser = serializerSet.getSerializer(getTypeClass());
    return list.stream().map(t -> ser.serialize(t, serializerSet)).collect(Collectors.toList());
  }

  @Nullable
  @Override
  public List<T> deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                             @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }

    Serializer<T> ser = serializerSet.getSerializer(getTypeClass());
    if (serialized instanceof List) {
      return ((List<T>) serialized).stream()
          .map(s -> ser.deserialize(s, wantedType, serializerSet)).collect(Collectors.toList());
    } else {
      return Arrays.asList(ser.deserialize(serialized, wantedType, serializerSet));
    }
  }
}
