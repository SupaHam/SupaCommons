package com.supaham.commons.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;

import com.supaham.commons.exceptions.DurationParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with time. This class contains methods such as {@link #elapsed(long,
 * long)} and more.
 *
 * @since 0.1
 */
public class TimeUtils {

  public static final int EPOCH_YEAR = 1970;
  /**
   * Hours per day.
   */
  public static final int HOURS_PER_DAY = 24;
  /**
   * Minutes per hour.
   */
  public static final int MINUTES_PER_HOUR = 60;
  /**
   * Seconds per minute.
   */
  public static final int SECONDS_PER_MINUTE = 60;
  /**
   * Seconds per hour.
   */
  public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
  /**
   * Seconds per day.
   */
  public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;

  public static final Pattern PATTERN = Pattern.compile("(\\d+)(ms|[dhms])");

  /**
   * Checks whether a certain amount of milliseconds have elapsed a given time in  milliseconds.
   * This method simply subtracts the given duration from the current time in milliseconds and
   * checks if the result is greater than the required amount of milliseconds.
   * <pre>
   *   TimeUtils.elapsed(now - 1000, 1000) = true
   *   TimeUtils.elapsed(now - 999, 1000) = true
   *   TimeUtils.elapsed(now, 1000) = false
   * </pre>
   *
   * @param duration the real time milliseconds.
   * @param required the amount of milliseconds that is required to have passed the {@code
   * duration} in order to succeed
   *
   * @return whether the {@code required} milliseconds have elapsed the {@code duration}
   */
  public static boolean elapsed(long duration, long required) {
    return System.currentTimeMillis() - duration >= required;
  }

  /**
   * Gets the needed duration format based on the given seconds.
   * <pre>
   *   TimeUtils.getNeededDurationFormat(60) = "ss";
   *   TimeUtils.getNeededDurationFormat(90) = "mm:ss";
   *   TimeUtils.getNeededDurationFormat(4000) = "HH:mm:ss";
   *   TimeUtils.getNeededDurationFormat(100000) = "dd:HH:mm:ss";
   * </pre>
   *
   * @param seconds seconds to base duration format off
   *
   * @return the duration format
   */
  public static String getNeededDurationFormat(int seconds) {
    Preconditions.checkArgument(seconds > 0, "seconds must be larger than 0.");
    String format = "";
    if (seconds > 86400) {
      format = "dd"; // more than 24 hours
    }
    if (seconds > 3600) {
      format += (!format.isEmpty() ? ":" : "") + "HH"; // more than 60 minutes
    }
    if (seconds > 60) {
      format += (!format.isEmpty() ? ":" : "") + "mm"; // more than 60 seconds
    }
    format += (!format.isEmpty() ? ":" : "") + "ss";
    return format;
  }

  /**
   * Parses a {@link CharSequence} into a {@code long} duration represented as seconds.
   * The pattern used to parse the duration is {@link TimeUtils#PATTERN}. In short, 1d2h3m4s is
   * valid, whereas 1x is not.
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
   * @return the duration
   *
   * @throws DurationParseException thrown if the text failed to parse
   */
  public static long parseDuration(@Nonnull CharSequence text) throws DurationParseException {
    return parseDurationMs(text) / 1000;
  }

  /**
   * Parses a {@link CharSequence} into a {@code long} duration represented as milliseconds.
   * The pattern used to parse the duration is {@link TimeUtils#PATTERN}. In short, 1d2h3m4s5ms is
   * valid, whereas 1x is not.
   * <pre>
   *   DurationUtils.parseDuration("1s1ms") is <b>valid</b>
   *   DurationUtils.parseDuration("1m1s2ms") is <b>valid</b>
   *   DurationUtils.parseDuration("1h1m1s3ms") is <b>valid</b>
   *   DurationUtils.parseDuration("1d1h1m1s4ms") is <b>valid</b>
   *   DurationUtils.parseDuration("1d1h1m1s1x5ms") is <b>valid</b>
   *   DurationUtils.parseDuration("1x") is <b>invalid</b>
   * </pre>
   *
   * @param text text to parse
   *
   * @return the milliseconds duration
   *
   * @throws DurationParseException thrown if the text failed to parse
   */
  public static long parseDurationMs(@Nonnull CharSequence text) throws DurationParseException {
    checkNotNull(text, "text cannot be null.");
    checkArgument(text.length() > 0, "text cannot be empty.");

    Matcher matcher = PATTERN.matcher(text);
    long sum = -1;
    while (matcher.find()) {
      if (sum < 0) {
        sum = 0;
      }
      String d = matcher.group(1);
      String u = StringUtils.lowerCase(matcher.group(2));
      int multiplier;
      String unitString;
      switch (u) {
        case "d":
          multiplier = TimeUtils.SECONDS_PER_DAY * 1000;
          unitString = "days";
          break;
        case "h":
          multiplier = TimeUtils.SECONDS_PER_HOUR * 1000;
          unitString = "hours";
          break;
        case "m":
          multiplier = TimeUtils.SECONDS_PER_MINUTE * 1000;
          unitString = "minutes";
          break;
        case "s":
          multiplier = 1000;
          unitString = "seconds";
          break;
        case "ms":
          multiplier = 1;
          unitString = "milliseconds";
          break;
        default:
          continue;
      }

      try {
        sum += parseNumber(d, multiplier, unitString);
      } catch (ArithmeticException ex) {
        throw (DurationParseException) new DurationParseException(
            "Text cannot be parsed to a Duration: overflow").initCause(ex);
      }
    }
    if (sum < 0) {
      throw new DurationParseException("Text cannot be parsed to a Duration");
    }
    return sum;
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
   * Converts a {@code long} duration of seconds into a {@link String}. <br /> The following
   * examples are demonstrated for a seconds with 3725 seconds:
   * <pre>
   *   DurationUtils.toString(seconds, true) = 1h2m5s
   *   DurationUtils.toString(seconds, false) = 1 hour 2 minutes 5 seconds
   * </pre>
   *
   * @param seconds seconds to convert
   * @param simple whether the string should be simple or pretty. If true, simple, the string can
   * be passed later to {@link #parseDuration(CharSequence)} for parsing into a long-duration
   * once again.
   *
   * @return the string of the {@code seconds}
   */
  public static String toString(long seconds, boolean simple) {
    Preconditions.checkArgument(seconds > -1, "seconds cannot be smaller than 0.");
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

  private TimeUtils() {
    throw new AssertionError("Did I say you can instantiate me? HUH?");
  }
}
