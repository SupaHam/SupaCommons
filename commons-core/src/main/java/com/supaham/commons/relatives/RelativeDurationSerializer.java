package com.supaham.commons.relatives;

import com.google.common.base.Preconditions;

import com.supaham.commons.utils.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public class RelativeDurationSerializer implements Serializer<RelativeDuration> {

  @Nullable @Override public Object serialize(@Nullable RelativeDuration object, @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (object == null) {
      return null;
    }
    return object.toString();
  }

  @Nullable @Override public RelativeDuration deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                                                          @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }
    Preconditions.checkArgument(StringUtils.isStringOrNumber(serialized),
                                "RelativeDuration may only be in the form of a String.");
    return RelativeDuration.fromString(serialized.toString());
  }
}
