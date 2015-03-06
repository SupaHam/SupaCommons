package com.supaham.commons.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.math.LongMath;

import com.supaham.commons.exceptions.DurationParseException;

import org.joda.time.Duration;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Duration} instances. This class contains methods such as
 * {@link #parseDuration(CharSequence)}, {@link #toString(Duration, boolean)} and more.
 *
 * @since 0.1
 */
public class DurationUtils {

  public static final Pattern PATTERN =
      Pattern.compile("(?=\\b\\d+[DHMS])(?:([0-9]+)D)?(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)S)?",
                      Pattern.CASE_INSENSITIVE);

  /**
   * Parses a {@link CharSequence} into a {@link Duration}. The pattern used to parse the duration
   * is {@link DurationUtils#PATTERN}. In short, 1d2h3m4s is valid, whereas 1x is not.
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
   */
  public static Duration parseDuration(@Nonnull CharSequence text) throws DurationParseException {
    checkNotNull(text, "text cannot be null.");
    checkArgument(text.length() > 0, "text cannot be empty.");

    Matcher matcher = PATTERN.matcher(text);
    if (matcher.matches()) {
      String dayMatch = matcher.group(1);
      String hourMatch = matcher.group(2);
      String minuteMatch = matcher.group(3);
      String secondMatch = matcher.group(4);
      if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
        long daysAsSecs = parseNumber(dayMatch, TimeUtils.SECONDS_PER_DAY, "days");
        long hoursAsSecs = parseNumber(hourMatch, TimeUtils.SECONDS_PER_HOUR, "hours");
        long minsAsSecs = parseNumber(minuteMatch, TimeUtils.SECONDS_PER_MINUTE, "minutes");
        long seconds = parseNumber(secondMatch, 1, "seconds");
        try {
          long secs = daysAsSecs + hoursAsSecs + minsAsSecs + seconds;
          return Duration.standardSeconds(secs);
        } catch (ArithmeticException ex) {
          throw (DurationParseException) new DurationParseException(
              "Text cannot be parsed to a Duration: overflow").initCause(ex);
        }
      }
    }
    throw new DurationParseException("Text cannot be parsed to a Duration");
  }

  private static long parseNumber(String parsed, int multiplier, String errorText) {
    // regex limits to [-+]?[0-9]+
    if (parsed == null) {
      return 0;
    }
    try {
      long val = Long.parseLong(parsed);
      return LongMath.checkedMultiply(val, multiplier);
    } catch (NumberFormatException | ArithmeticException ex) {
      throw (DurationParseException)
          new DurationParseException("Text cannot be parsed to a Duration: " + errorText)
              .initCause(ex);
    }
  }

  /**
   * Converts a {@link Duration} into a {@link String}. <br /> The following examples are
   * demonstrated for a Duration with 3725 seconds.
   * <pre>
   *   DurationUtils.toString(duration, true) = 1h2m5s
   *   DurationUtils.toString(duration, false) = 1 hour 2 minutes 5 seconds
   * </pre>
   *
   * @param duration duration to convert
   * @param simple whether the string should be simple or pretty. If true, simple, the string can be
   * passed later to {@link #parseDuration(CharSequence)} for parsing into a {@link Duration} once
   * again.
   *
   * @return the string of the {@code duration}
   */
  public static String toString(@Nonnull Duration duration, boolean simple) {

    long seconds = duration.getStandardSeconds();
    long days = seconds / TimeUtils.SECONDS_PER_DAY;
    long hours = (seconds % TimeUtils.SECONDS_PER_DAY) / TimeUtils.SECONDS_PER_HOUR;
    int minutes = (int) ((seconds % TimeUtils.SECONDS_PER_HOUR) / TimeUtils.SECONDS_PER_MINUTE);
    int secs = (int) (seconds % TimeUtils.SECONDS_PER_MINUTE);
    StringBuilder buf = new StringBuilder(24);
    if (days != 0) {
      buf.append(days).append(simple ? "d" : " day" + (days != 1 ? "s" : ""));
    }
    if (hours != 0) {
      if (!simple && buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(hours).append(simple ? "h" : " hour" + (hours != 1 ? "s" : ""));
    }
    if (minutes != 0) {
      if (!simple && buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(minutes).append(simple ? "m" : " minute" + (minutes != 1 ? "s" : ""));
    }
    if (secs != 0) {
      if (!simple && buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(secs).append(simple ? "s" : " second" + (secs != 1 ? "s" : ""));
    }
    return buf.toString();
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
    checkNotNull(d1, "first duration cannot be null");
    checkNotNull(d2, "second duration cannot be null");
    long diff = Math.abs(d1.getMillis() - d2.getMillis());
    return new Duration(RandomUtils.nextLong(random, diff));
  }

  private DurationUtils() {}
}
