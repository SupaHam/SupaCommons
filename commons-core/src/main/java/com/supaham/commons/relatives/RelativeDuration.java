package com.supaham.commons.relatives;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import com.supaham.commons.utils.DurationUtils;
import com.supaham.commons.utils.TimeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializeWith;

/**
 * Represents a class for relativity with {@link Duration}s. This feature and class should not be used in a
 * predictable environment. To clarify, this class is only meant to be used when some maths is to be done by the work
 * of some external data, such as a configuration file. If your program functionality is strictly kept inside the
 * program please resort to {@link Duration}'s arithmetic methods for your operations as it may be more convenient for
 * both the programmer and the JVM.
 *
 * <p />
 * The {@link #isRelative()} feature is provided for ease of use in general cases. If the user wishes to have true
 * relativity they will have to check the boolean themselves and handle it from there. For more information see {@link
 * #isRelative()}.
 *
 * @see ArithmeticOperator
 * @see RelativeNumberSerializer
 */
@SerializeWith(RelativeDurationSerializer.class)
public final class RelativeDuration implements Function<Duration, Duration> {

  private static final Pattern NO_SPECIFIED_UNIT = Pattern.compile(TimeUtils.PATTERN.pattern() + "?");
  public static final RelativeDuration ZERO = from(ArithmeticOperator.ADDITION, Duration.ZERO);

  private final ArithmeticOperator operator;
  private final Duration duration;
  private final boolean relative;

  /**
   * Deserialization in the form of {@link #toString()}. It is important to note that the duration must be compatible
   * with SupaCommon's format in {@link TimeUtils#parseDurationMs(CharSequence)}. The following table shows the valid
   * and invalid serialized forms:
   * <table>
   * <thead>
   * <tr>
   * <th>Valid</th>
   * <th>Invalid</th>
   * </tr>
   * </thead>
   * <tbody>
   * <tr>
   * <td>1d</td>
   * <td>1dasd</td>
   * </tr>
   *
   * <tr>
   * <td>-1d</td>
   * <td>-1dasd</td>
   * </tr>
   *
   * <tr>
   * <td>~1d</td>
   * <td>~1dasd</td>
   * </tr>
   *
   * <tr>
   * <td>~+1d</td>
   * <td>~1d+</td>
   * </tr>
   *
   * <tr>
   * <td>~-1d</td>
   * <td>~1d-</td>
   * </tr>
   *
   * <tr>
   * <td>~*1d</td>
   * <td>~1d*</td>
   * </tr>
   *
   * <tr>
   * <td>~/1d</td>
   * <td>~1d/</td>
   * </tr>
   *
   * <tr>
   * <td>~%1d</td>
   * <td>~1d%</td>
   * </tr>
   *
   * <tr>
   * <td>~^1d</td>
   * <td>~1d^</td>
   * </tr>
   * </tbody>
   * </table>
   *
   * @param string string to deserialize
   *
   * @return deserialized string in the form of {@link RelativeDuration}
   */
  public static RelativeDuration fromString(@Nonnull String string) {
    Preconditions.checkNotNull(string, "string cannot be null.");
    boolean relative = false;
    Duration duration = null;
    ArithmeticOperator operator;

    string = string.trim();
    // Make sure the string isn't just "" or "~".
    if (string.isEmpty() || string.equals("~")) {
      return ZERO;
    }

    if (string.startsWith("~")) {
      relative = true;
      string = string.substring(1).trim();
    }

    if (Character.isDigit(string.charAt(0))) {
      operator = ArithmeticOperator.ADDITION;
    } else {
      operator = ArithmeticOperator.fromChar(string.charAt(0));
      // If the string is something like -123, make sure we pass the number as -123 and not 123 with subtraction
      // operator. This makes Relativity with deserialized objects work as expected.
      if (operator == ArithmeticOperator.SUBTRACTION && string.charAt(1) != '-') {
        operator = ArithmeticOperator.ADDITION;
      } else {
        string = string.substring(1).trim();
      }
    }
    Matcher matcher = NO_SPECIFIED_UNIT.matcher(string);
    if (matcher.find()) {
      if (matcher.group(2) == null) {
        duration = Duration.ofSeconds((long) Double.parseDouble(matcher.group(1)));
      }
    }
    if (duration == null) {
      duration = DurationUtils.parseDuration(string);
    }
    Preconditions.checkNotNull(operator, "operator cannot be null.");
    return new RelativeDuration(operator, duration, relative);
  }

  public static RelativeDuration from(@Nonnull ArithmeticOperator operator, @Nonnull Duration duration) {
    Preconditions.checkNotNull(duration, "duration cannot be null.");
    return new RelativeDuration(operator, duration);
  }

  /**
   * Applies a {@link Duration} to this {@link RelativeDuration} for mathematical operation based on {@link
   * #getOperator()}. If the given {@code duration} is null, {@link #getDuration()} is returned.
   *
   * @param duration duration to apply to the operator, nullable
   *
   * @return result of the operation
   */
  @Override public Duration apply(Duration duration) throws ArithmeticException {
    if (duration == null) {
      return this.duration;
    }
    double d1 = BigDecimal.valueOf(this.duration.getSeconds())
        .add(BigDecimal.valueOf(this.duration.getNano(), 9)).doubleValue();
    double d2 = BigDecimal.valueOf(duration.getSeconds())
        .add(BigDecimal.valueOf(duration.getNano(), 9)).doubleValue();

    double result = this.operator.applyToDouble(d1, d2);
    if (result > Long.MAX_VALUE) {
      throw new ArithmeticException("duration of " + result + " is too large.");
    }

    BigDecimal bigDecimal = BigDecimal.valueOf(result);
    BigInteger nanos = bigDecimal.movePointRight(9).toBigInteger();
    BigInteger[] divRem = nanos.divideAndRemainder(TimeUtils.BI_NANOS_PER_SECOND);
    return Duration.ofSeconds(divRem[0].longValue(), divRem[1].intValue());
  }

  private RelativeDuration(ArithmeticOperator operator, Duration duration) {
    this(operator, duration, true);
  }

  private RelativeDuration(ArithmeticOperator operator, Duration duration, boolean relative) {
    this.operator = operator;
    this.duration = duration;
    this.relative = relative;
  }

  @Override public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RelativeDuration that = (RelativeDuration) object;
    return relative == that.relative &&
           operator == that.operator &&
           Objects.equal(duration, that.duration);
  }

  @Override public int hashCode() {
    return Objects.hashCode(operator, duration, relative);
  }

  /**
   * Serializes this object as "~[op]duration". Where ~ is provided if {@link #isRelative()}, [op] is only provided if
   * the {@link #getOperator()} is not {@link ArithmeticOperator#ADDITION}, duration is provided at all times.
   *
   * @return serialized relative duration
   */
  @Override public String toString() {
    StringBuilder sb = new StringBuilder();
    if (this.relative) {
      sb.append("~");
    }
    // We automatically interpret number only as ADDITION in deserialization, so omit it from serialization.
    if (this.operator != ArithmeticOperator.ADDITION) {
      sb.append(this.operator.getChar());
    }

    sb.append(DurationUtils.toString(this.duration, true));
    return sb.toString();
  }

  public ArithmeticOperator getOperator() {
    return operator;
  }

  public Duration getDuration() {
    return duration;
  }

  /**
   * Returns whether this {@link RelativeDuration} is truly of relative nature. The only case where this returns false
   * is if this instance was deserialized as a whole number via {@link #fromString(String)}, e.g. "123".
   */
  public boolean isRelative() {
    return relative;
  }
}
