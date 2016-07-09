package com.supaham.commons.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;

import com.supaham.commons.exceptions.DurationParseException;
import com.supaham.commons.exceptions.TimeParseException;

import java.math.BigInteger;
import java.time.LocalTime;
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

  public static final long NANOS_PER_SECOND = 1000_000_000L;
  public static final BigInteger BI_NANOS_PER_SECOND = BigInteger.valueOf(NANOS_PER_SECOND);

  public static final Pattern PATTERN = Pattern.compile("(-?\\d*\\.?\\d*)(ms|[dhms])");

  public static final Pattern TIME_PATTERN = Pattern
      .compile("(?<hours>\\d?\\d)(?::(?<minutes>\\d\\d))?(?::(?<seconds>\\d\\d))?\\s*(?<ampm>[aApP]\\.?[mM]\\.?)*");

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
    if (text.equals("0")) {
      return 0;
    }

    Matcher matcher = PATTERN.matcher(text);
    long sum = 0;
    boolean foundUnit = false;
    while (matcher.find()) {
      String d = matcher.group(1);
      String u = StringUtils.lowerCase(matcher.group(2));
      int multiplier;
      String unitString;
      foundUnit = true;
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
            "Text cannot be parsed as milliseconds: overflow").initCause(ex);
      }
    }
    if (sum == 0 && !foundUnit) {
      throw new DurationParseException("Text cannot be parsed as milliseconds " + text);
    }
    return sum;
  }

  private static long parseNumber(String parsed, int multiplier, String errorText) {
    // regex limits to [-+]?[0-9]+
    if (parsed == null) {
      return 0;
    }
    try {
      double val = Double.parseDouble(parsed);
      return (long) (val * multiplier);
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
    long days = seconds / TimeUtils.SECONDS_PER_DAY;
    long hours = (seconds % TimeUtils.SECONDS_PER_DAY) / TimeUtils.SECONDS_PER_HOUR;
    int minutes = (int) ((seconds % TimeUtils.SECONDS_PER_HOUR) / TimeUtils.SECONDS_PER_MINUTE);
    int secs = (int) (seconds % TimeUtils.SECONDS_PER_MINUTE);
    StringBuilder buf = new StringBuilder(24);
    if (days != 0) {
      buf.append(days).append(simple ? "d" : " day" + (days != 1 && days != -1 ? "s" : ""));
    }
    if (hours != 0) {
      if (!simple && buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(hours).append(simple ? "h" : " hour" + (hours != 1 && hours != -1 ? "s" : ""));
    }
    if (minutes != 0) {
      if (!simple && buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(minutes).append(simple ? "m" : " minute" + (minutes != 1 && minutes != -1 ? "s" : ""));
    }
    if (secs != 0) {
      if (!simple && buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(secs).append(simple ? "s" : " second" + (secs != 1 && secs != -1 ? "s" : ""));
    }
    return buf.toString();
  }

  /**
   * Parses a {@link LocalTime} in the form of a string. The {@link Pattern} used to match the {@code timeString} is
   * {@link #TIME_PATTERN}. A combination of hours, minutes, and seconds may be present to form a full 0-23 hour point
   * in time. This method supports <b>12-hour time format</b>, where a string can be 12AM, 12:00PM, etc.
   * <pre><code>
   * TimeUtils.parse(null) = {@link NullPointerException}
   * TimeUtils.parse("") = {@link IllegalArgumentException}
   * TimeUtils.parse("asd") = {@link TimeParseException}
   * TimeUtils.parse("24") = {@link TimeParseException}
   * TimeUtils.parse("23") = {@link TimeParseException}
   * TimeUtils.parse("0") = LocalTime{hours=0}
   * TimeUtils.parse("1") = LocalTime{hours=1}
   * TimeUtils.parse("01:00") = LocalTime{hours=1}
   * TimeUtils.parse("1:00") = LocalTime{hours=1}
   * TimeUtils.parse("01:23") = LocalTime{hours=1,minutes=23}
   * TimeUtils.parse("01:23:45") = LocalTime{hours=1,minutes=23,seconds=45}
   * TimeUtils.parse("13:23:45") = LocalTime{hours=13,minutes=23,seconds=45}
   * TimeUtils.parse("24:23:45") = {@link TimeParseException}
   *
   * TimeUtils.parse("12AM") = LocalTime{hours=0}
   * TimeUtils.parse("00AM") = {@link TimeParseException}
   * TimeUtils.parse("1AM") = LocalTime{hours=1}
   * TimeUtils.parse("01AM") = LocalTime{hours=1}
   * TimeUtils.parse("01:23AM") = LocalTime{hours=1,minutes=23}
   * TimeUtils.parse("01:23:45AM") = LocalTime{hours=1,minutes=23,seconds=45}
   * TimeUtils.parse("12A.M.") = LocalTime{hours=0}
   * TimeUtils.parse("12am") = LocalTime{hours=0}
   * TimeUtils.parse("12a.m.") = LocalTime{hours=0}
   *
   * TimeUtils.parse("12PM") = LocalTime{hours=12}
   * TimeUtils.parse("00PM") = {@link TimeParseException}
   * TimeUtils.parse("1PM") = LocalTime{hours=1}
   * TimeUtils.parse("01PM") = LocalTime{hours=1}
   * TimeUtils.parse("01:23PM") = LocalTime{hours=1,minutes=23}
   * TimeUtils.parse("01:23:45PM") = LocalTime{hours=1,minutes=23,seconds=45}
   * TimeUtils.parse("12P.M.") = LocalTime{hours=12}
   * TimeUtils.parse("12pm") = LocalTime{hours=12}
   * TimeUtils.parse("12p.m.") = LocalTime{hours=12}
   * </code></pre>
   *
   * @param timeString string in time format
   *
   * @return parsed {@code timeString} in a LocalTime instance
   *
   * @throws TimeParseException thrown if the timeString is in an invalid format or includes invalid time units
   */
  @Nonnull
  public static LocalTime parseTime(@Nonnull String timeString) throws TimeParseException {
    StringUtils.checkNotNullOrEmpty(timeString, "time string");

    Matcher matcher = TIME_PATTERN.matcher(timeString);
    checkTime(timeString, matcher.matches(), timeString + " is not a valid time.");

    int hours = Integer.parseInt(matcher.group("hours"));
    int minutes = 0;
    int seconds = 0;
    Boolean isAM = null;

    try {
      minutes = Integer.parseInt(matcher.group("minutes"));
    } catch (IllegalArgumentException ignored) { // ignores NumberFormatException as well
    }

    try {
      seconds = Integer.parseInt(matcher.group("seconds"));
    } catch (IllegalArgumentException ignored) { // ignores NumberFormatException as well
    }

    if (matcher.group("ampm") != null) {
      isAM = matcher.group("ampm").toLowerCase()
          .replaceAll("\\.", "") // 12-hour time might be A.M or P.M.
          .equals("am");
    }

    checkTime(timeString, hours >= 0 && hours <= 23, "hours cannot be less than 0 or greater than 23.");
    checkTime(timeString, minutes >= 0 && minutes <= 60, "minutes cannot be less than 0 or greater than 60.");
    checkTime(timeString, seconds >= 0 && seconds <= 60, "seconds cannot be less than 0 or greater than 60.");

    // 12-hour format
    if (isAM != null) {
      checkTime(timeString, hours >= 1 && hours <= 12,
                "hours cannot be less than 1 or greater than 12 in 12-hour format.");

      // And the problem universally.
      // Explained here: https://en.wikipedia.org/wiki/12-hour_clock#Confusion_at_noon_and_midnight
      if (hours == 12) {
        hours = 0;
      }

      if (!isAM) { // PM
        hours += 12;
      }
    }
    return LocalTime.of(hours, minutes, seconds);
  }

  private static void checkTime(String timeString, boolean b, String message) throws TimeParseException {
    if (!b) {
      throw new TimeParseException(timeString, message);
    }
  }
  
  public static String localTimeToString(LocalTime localTime) {
    return localTime == null ? null : localTime.toString();
  }

  private TimeUtils() {
    throw new AssertionError("Did I say you can instantiate me? HUH?");
  }
}
