package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

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
   * @param required the amount of milliseconds that is required to have passed the {@code duration}
   * in order to succeed
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
}
