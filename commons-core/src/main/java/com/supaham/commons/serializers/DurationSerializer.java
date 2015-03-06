package com.supaham.commons.serializers;

import com.supaham.commons.utils.DurationUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;

import pluginbase.config.serializers.Serializer;

/**
 * A {@link Duration} serializer that simply calls {@link DurationUtils#toString(Duration,
 * boolean)}, with boolean as true, in return.
 * 
 * @since 0.1
 */
public class DurationSerializer implements Serializer<Duration> {

  @Override
  @Nullable
  public Object serialize(@Nullable final Duration duration) {
    if (duration == null) {
      return null;
    }
    return DurationUtils.toString(duration, true);
  }

  @Override
  @Nullable
  public Duration deserialize(@Nullable Object serialized, @NotNull Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    return DurationUtils.parseDuration(serialized.toString());
  }
}
