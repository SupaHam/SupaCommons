package com.supaham.commons.serializers;

import com.supaham.commons.utils.TimeUtils;

import java.time.Duration;
import java.time.LocalTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * A {@link Duration} serializer that simply calls {@link TimeUtils#localTimeToString(LocalTime)}, with boolean
 * as true, in return.
 *
 * @since 0.5.2
 */
public class LocalTimeSerializer implements Serializer<LocalTime> {

  @Override
  @Nullable
  public Object serialize(@Nullable final LocalTime localTime, @Nonnull SerializerSet serializerSet) {
    if (localTime == null) {
      return null;
    }
    return TimeUtils.localTimeToString(localTime);
  }

  @Override
  @Nullable
  public LocalTime deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                               @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    return TimeUtils.parseTime(serialized.toString());
  }
}
