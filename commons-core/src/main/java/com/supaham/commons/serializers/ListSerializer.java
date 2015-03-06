package com.supaham.commons.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;

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
  public abstract Class<? extends Serializer<T>> getSerializerClass();

  @Nullable
  @Override
  public Object serialize(List<T> list) throws IllegalArgumentException {
    if (list == null) {
      return null;
    }
    Serializer<T> ser = Serializers.getSerializer(getSerializerClass());
    ArrayList<Object> result = new ArrayList<>();
    for (T t : list) {
      result.add(ser.serialize(t));
    }
    return result;
  }

  @Nullable
  @Override
  public List<T> deserialize(@Nullable Object serialized, @NotNull Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }

    Serializer<T> ser = Serializers.getSerializer(getSerializerClass());
    List<T> result = new ArrayList<>();
    for (String s : (List<String>) serialized) {
      result.add(ser.deserialize(s, wantedType));
    }
    return result;
  }
}
