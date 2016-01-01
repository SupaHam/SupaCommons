package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.exceptions.DurationParseException;

import java.time.Duration;
import java.util.Random;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Duration} instances. This class contains methods such as
 * {@link #parseDuration(CharSequence)}, {@link #toString(Duration, boolean)} and more.
 *
 * @since 0.1
 */
public class DurationUtils {

  /**
   * Parses a {@link CharSequence} into a {@link Duration} using {@link
   * TimeUtils#parseDurationMs(CharSequence)}. In short, 1d2h3m4s is valid, whereas 1x is not.
   * <pre>
   *   DurationUtils.parseDuration("1s") is <b>valid</b>
   *   DurationUtils.parseDuration("1m1s") is <b>valid</b>
   *   DurationUtils.parseDuration("1h1m1s") is <b>valid</b>
   *   DurationUtils.parseDuration("1d1h1m1s") is <b>valid</b>
   *   DurationUtils.parseDuration("1d1h1m1s1x") is <b>valid</b>
   *   DurationUtils.parseDuration("1x") is <b>invalid</b>
   * </pre>
   *
   * @param text text to parse
   *
   * @return the {@link Duration}
   *
   * @throws DurationParseException thrown if the text failed to parse
   * @see TimeUtils#parseDuration(CharSequence)
   */
  public static Duration parseDuration(@Nonnull CharSequence text) throws DurationParseException {
    return Duration.ofMillis(TimeUtils.parseDurationMs(text));
  }

  /**
   * Converts a {@link Duration} into a {@link String}. <br /> The following examples are
   * demonstrated for a Duration with 3725 seconds:
   * <pre>
   *   DurationUtils.toString(duration, true) = 1h2m5s
   *   DurationUtils.toString(duration, false) = 1 hour 2 minutes 5 seconds
   * </pre>
   *
   * @param duration duration to convert
   * @param simple whether the string should be simple or pretty. If true, simple, the string can
   * be passed later to {@link #parseDuration(CharSequence)} for parsing into a {@link Duration}
   * once again.
   *
   * @return the string of the {@code duration}
   *
   * @see TimeUtils#toString(long, boolean)
   */
  public static String toString(@Nonnull Duration duration, boolean simple) {
    return TimeUtils.toString(duration.getSeconds(), simple);
  }

  /**
   * Returns a random duration between two given {@link Duration} instances.
   *
   * @param d1 first duration
   * @param d2 second duration
   *
   * @return a pseudorandom duration
   *
   * @see #randomDuration(Random, Duration, Duration)
   */
  public static Duration randomDuration(@Nonnull Duration d1, @Nonnull Duration d2) {
    return randomDuration(RandomUtils.getRandom(), d1, d2);
  }

  /**
   * Returns a random duration between two given {@link Duration} instances.
   *
   * @param random random instance to use
   * @param d1 first duration
   * @param d2 second duration
   *
   * @return a pseudorandom duration
   */
  public static Duration randomDuration(@Nonnull Random random, @Nonnull Duration d1,
                                        @Nonnull Duration d2) {
    Preconditions.checkNotNull(d1, "first duration cannot be null");
    Preconditions.checkNotNull(d2, "second duration cannot be null");
    long diff = Math.abs(d1.toMillis() - d2.toMillis());
    return Duration.ofMillis(RandomUtils.nextLong(random, diff));
  }

  private DurationUtils() {}
}
