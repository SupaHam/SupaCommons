package com.supaham.commons.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Utility methods for working with {@link Number} instances. This class contains methods such as
 * {@link #roundExact(double)}, and more.
 *
 * @since 0.1
 */
public final class NumberUtils {

  /**
   * Rounds a double value to the exact value to one decimal point.
   *
   * <pre>
   *   NumberUtils.roundExact(1.234D)               = 1.2D
   * </pre>
   *
   * @param value double value to round
   *
   * @return rounded {@code value} based on the {@code format}
   */
  public static String roundExact(double value) {
    return roundExact("#.#", value);
  }

  /**
   * Rounds a float value to the exact value to one decimal point.
   *
   * <pre>
   *   NumberUtils.roundExact(1.234F)               = 1.2F
   * </pre>
   *
   * @param value float value to round
   *
   * @return rounded {@code value} based on the {@code format}
   */
  public static String roundExact(float value) {
    return roundExact("#.#", value);
  }

  /**
   * Rounds a double value to the exact value with the given format.
   *
   * <pre>
   *   NumberUtils.roundExact(-1, 1.234D)               = 1.D
   *   NumberUtils.roundExact(0, 1.234D)                = 1.D
   *   NumberUtils.roundExact(1, 1.234D)                = 1.2D
   *   NumberUtils.roundExact(2, 1.234D)                = 1.23D
   *   NumberUtils.roundExact(3, 1.234D)                = 1.234D
   * </pre>
   *
   * @param decimalPlaces decimal places to round to
   * @param value double value to round
   *
   * @return rounded {@code value} based on the {@code format}
   */
  public static String roundExact(int decimalPlaces, double value) {
    return roundExact("#." + StringUtils.repeat("#", decimalPlaces), value);
  }

  /**
   * Rounds a float value to the exact value with the given format.
   *
   * <pre>
   *   NumberUtils.roundExact(-1, 1.234F)               = 1.F
   *   NumberUtils.roundExact(0, 1.234F)                = 1.F
   *   NumberUtils.roundExact(1, 1.234F)                = 1.2F
   *   NumberUtils.roundExact(2, 1.234F)                = 1.23F
   *   NumberUtils.roundExact(3, 1.234F)                = 1.234F
   * </pre>
   *
   * @param decimalPlaces decimal places to round to
   * @param value float value to round
   *
   * @return rounded {@code value} based on the {@code format}
   */
  public static String roundExact(int decimalPlaces, float value) {
    return roundExact("#." + StringUtils.repeat("#", decimalPlaces), value);
  }

  /**
   * Rounds a double value to the exact value with the given format.
   *
   * <pre>
   *   NumberUtils.roundExact("#.", 1.234D)               = 1.D
   *   NumberUtils.roundExact("#.#", 1.234D)              = 1.2D
   *   NumberUtils.roundExact("#.##", 1.234D)             = 1.23D
   *   NumberUtils.roundExact("#.###", 1.234D)            = 1.234D
   * </pre>
   *
   * @param format format to display {@code value} in, see {@link DecimalFormat}
   * @param value double value to round
   *
   * @return rounded {@code value} based on the {@code format}
   */
  public static String roundExact(String format, double value) {
    DecimalFormat df = new DecimalFormat(format);
    df.setRoundingMode(RoundingMode.DOWN);
    return df.format(value);
  }

  /**
   * Rounds a float value to the exact value with the given format.
   *
   * <pre>
   *   NumberUtils.roundExact("#.", 1.234F)               = 1.F
   *   NumberUtils.roundExact("#.#", 1.234F)              = 1.2F
   *   NumberUtils.roundExact("#.##", 1.234F)             = 1.23F
   *   NumberUtils.roundExact("#.###", 1.234F)            = 1.234F
   * </pre>
   *
   * @param format format to display {@code value} in, see {@link DecimalFormat}
   * @param value float value to round
   *
   * @return rounded {@code value} based on the {@code format}
   */
  public static String roundExact(String format, float value) {
    DecimalFormat df = new DecimalFormat(format);
    df.setRoundingMode(RoundingMode.DOWN);
    return df.format(value);
  }

  private NumberUtils() {
  }
}
