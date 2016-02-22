package com.supaham.commons.relatives;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a class for relativity with {@link Number} types. This feature and class should not be used in a
 * predictable environment. To clarify, this class is only meant to be used when some maths is to be done by the work
 * of some external data, such as a configuration file. If your program functionality is strictly kept inside the
 * program please resort to classes such as {@link Math} or direct arithmetic operators for your operations as it may
 * be more convenient both for the programmer and the JVM.
 *
 * <p />
 * Although {@link RelativeNumber}'s {@code from} methods take all six primitive types, it is important to note that it
 * eventually stores it as a double for the largest number possible. To be able to distinguish whether the initial
 * number was whole or not, use {@link #isWholeNumber()}.
 *
 * <p />
 * The {@link #isRelative()} feature is provided for ease of use in general cases. If the user wishes to have true
 * relativity they will have to check the boolean themselves and handle it from there. For more information see {@link
 * #isRelative()}.
 *
 * @see ArithmeticOperator
 * @see RelativeNumberSerializer
 */
public final class RelativeNumber implements Function<Number, Double> {

  /**
   * Represents a {@code ZERO} {@code relative} number with the {@link ArithmeticOperator#ADDITION} operator and {@link
   * #isWholeNumber()} as true.
   */
  public static final RelativeNumber ZERO = from(ArithmeticOperator.ADDITION, 0);

  private final ArithmeticOperator operator;
  private final double number;
  private final boolean wholeNumber;
  private final boolean relative;

  public static RelativeNumber fromString(@Nonnull String string) {
    boolean relative = false;
    double number;
    boolean wholeNumber;
    ArithmeticOperator operator;

    // Make sure the string isn't just "" or "~".
    if (string.isEmpty() || string.equals("~")) {
      return ZERO;
    }

    if (string.startsWith("~")) {
      relative = true;
      string = string.substring(1);
    }

    if (Character.isDigit(string.charAt(0))) {
      operator = ArithmeticOperator.ADDITION;
    } else {
      operator = ArithmeticOperator.fromChar(string.charAt(0));
      string = string.substring(1);
    }
    try {
      number = Integer.parseInt(string);
      wholeNumber = true;
    } catch (NumberFormatException e) {
      try {
        number = Double.parseDouble(string);
        wholeNumber = false;
      } catch (NumberFormatException e2) {
        throw new NumberFormatException(string + " must consists of only numbers and at most one decimal point.");
      }
    }

    return new RelativeNumber(operator, number, wholeNumber, relative);
  }

  public static RelativeNumber from(@Nonnull ArithmeticOperator operator, byte b) {
    return new RelativeNumber(operator, b, true);
  }

  public static RelativeNumber from(@Nonnull ArithmeticOperator operator, short s) {
    return new RelativeNumber(operator, s, true);
  }

  public static RelativeNumber from(@Nonnull ArithmeticOperator operator, float f) {
    return new RelativeNumber(operator, f, false);
  }

  public static RelativeNumber from(@Nonnull ArithmeticOperator operator, int i) {
    return new RelativeNumber(operator, i, true);
  }

  public static RelativeNumber from(@Nonnull ArithmeticOperator operator, long l) {
    return new RelativeNumber(operator, l, true);
  }

  public static RelativeNumber from(@Nonnull ArithmeticOperator operator, double d) {
    return new RelativeNumber(operator, d, false);
  }

  private RelativeNumber(ArithmeticOperator operator, Number number, boolean wholeNumber) {
    this(operator, number, wholeNumber, true);
  }

  protected RelativeNumber(ArithmeticOperator operator, Number number, boolean wholeNumber, boolean relative) {
    Preconditions.checkNotNull(operator, "operator cannot be null.");
    Preconditions.checkNotNull(number, "number cannot be null.");
    this.operator = operator;
    this.number = number.doubleValue();
    this.wholeNumber = wholeNumber;
    this.relative = relative;
  }

  /**
   * Applies a {@link Number} to this {@link RelativeNumber} for mathematical operation. If the given {@code number} is
   * null, {@link #getNumber()} is returned.
   *
   * @param number number to apply to the operator, nullable
   *
   * @return result of the operation
   */
  @Override public Double apply(@Nullable Number number) {
    return number == null || !this.relative ? this.number
                                            : this.operator.applyToDouble(this.number, number.doubleValue());
  }

  @Override public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RelativeNumber that = (RelativeNumber) object;
    return Double.compare(that.number, number) == 0 &&
           relative == that.relative &&
           operator == that.operator;
  }

  @Override public int hashCode() {
    return Objects.hashCode(operator, number, relative);
  }

  /**
   * Serializes this object as "~[op]number". Where ~ is provided if {@link #isRelative()}, [op] is only provided if
   * the {@link #getOperator()} is not {@link ArithmeticOperator#ADDITION}, number is provided at all times.
   *
   * @return serialized relative number
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

    if (wholeNumber) {
      sb.append((int) this.number);
    } else {
      sb.append(this.number);
    }
    return sb.toString();
  }

  @Nonnull
  public ArithmeticOperator getOperator() {
    return operator;
  }

  public double getNumber() {
    return number;
  }

  /**
   * Returns whether this {@link RelativeNumber} is truly of relative nature. The only case where this returns false is
   * if this instance was deserialized as a whole number via {@link #fromString(String)}, e.g. "123".
   */
  public boolean isRelative() {
    return relative;
  }

  /**
   * Returns whether {@link RelativeNumber#getNumber()} was initially created with a whole number.
   *
   * @return whether this relative number uses a whole number
   */
  public boolean isWholeNumber() {
    return wholeNumber;
  }
}
