package com.supaham.commons.serializers;

import com.supaham.commons.utils.DurationUtils;

import java.time.Duration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * A {@link Duration} serializer that simply calls {@link DurationUtils#toString(Duration,
 * boolean)}, with boolean as true, in return.
 *
 * @since 0.1
 */
public class DurationSerializer implements Serializer<Duration> {

  @Override
  @Nullable
  public Object serialize(@Nullable final Duration duration, @Nonnull SerializerSet serializerSet) {
    if (duration == null) {
      return null;
    }
    return DurationUtils.toString(duration, true);
  }

  @Override
  @Nullable
  public Duration deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                              @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    return DurationUtils.parseDuration(serialized.toString());
  }
}
