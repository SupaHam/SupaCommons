package com.supaham.commons.relatives;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public class RelativeNumberSerializer implements Serializer<RelativeNumber> {

  @Nullable @Override public Object serialize(@Nullable RelativeNumber object, @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (object == null) {
      return null;
    }
    return object.toString();
  }

  @Nullable @Override public RelativeNumber deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                                                        @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }
    Preconditions.checkArgument(serialized instanceof String, "RelativeNumber may only be in the form of a String.");
    return RelativeNumber.fromString(serialized.toString());
  }
}
