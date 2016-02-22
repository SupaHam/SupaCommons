package com.supaham.commons.relatives;

/**
 * Represents mathematical arithmetical operators in the form of an {@link Enum} which supports all six primitive
 * number data types, each with it's own method applyToDouble, etc.
 * <p />
 * Taking data type restrictions into account, all applyTo* methods return double values to prevent any errors being
 * thrown due to overflow.
 */
public enum ArithmeticOperator {
  /**
   * Represents the addition (often signified by the plus symbol "+") arithmetic operation in mathematics.
   * <br />
   * <b>For example,</b> 1 + 1 = 2
   */
  ADDITION('+') {
    @Override public double applyToDouble(double d1, double d2) { return d1 + d2; }

    @Override public double applyToLong(long l1, long l2) { return l1 + l2; }

    @Override public double applyToInt(int i1, int i2) { return i1 + i2; }

    @Override public double applyToFloat(float f1, float f2) { return f1 + f2; }

    @Override public double applyToShort(short s1, short s2) { return s1 + s2; }

    @Override public double applyToByte(byte b1, byte b2) { return b1 + b2; }
  },
  /**
   * Represents the subtraction (often signified by the minus symbol "-") arithmetic operation in mathematics.
   * <br />
   * <b>For example,</b> 1 - 1 = 0
   */
  SUBTRACTION('-') {
    @Override public double applyToDouble(double d1, double d2) { return d1 - d2; }

    @Override public double applyToLong(long l1, long l2) { return l1 - l2; }

    @Override public double applyToInt(int i1, int i2) { return i1 - i2; }

    @Override public double applyToFloat(float f1, float f2) { return f1 - f2; }

    @Override public double applyToShort(short s1, short s2) { return s1 - s2; }

    @Override public double applyToByte(byte b1, byte b2) { return b1 - b2; }
  },
  /**
   * Represents the multiplication (often denoted by the cross symbol "&times;" or "*") arithmetic operation in
   * mathematics.
   * <br />
   * <b>For example,</b> 1 &times; 1 = 1
   * <br />
   * <b>For example,</b> 1 * 1 = 1
   */
  MULTIPLICATION('*') {
    @Override public double applyToDouble(double d1, double d2) { return d1 * d2; }

    @Override public double applyToLong(long l1, long l2) { return l1 * l2; }

    @Override public double applyToInt(int i1, int i2) { return i1 * i2; }

    @Override public double applyToFloat(float f1, float f2) { return f1 * f2; }

    @Override public double applyToShort(short s1, short s2) { return s1 * s2; }

    @Override public double applyToByte(byte b1, byte b2) { return b1 * b2; }
  },
  /**
   * Represents the division (often denoted as "&divide;" or "/") arithmetic operation in mathematics.
   * <br />
   * <b>For example,</b> 1 &divide; 1 = 1
   * <br />
   * <b>For example,</b> 1 / 1 = 1
   */
  DIVISION('/') {
    @Override public double applyToDouble(double d1, double d2) { return d1 / d2; }

    @Override public double applyToLong(long l1, long l2) { return l1 / l2; }

    @Override public double applyToInt(int i1, int i2) { return i1 / i2; }

    @Override public double applyToFloat(float f1, float f2) { return f1 / f2; }

    @Override public double applyToShort(short s1, short s2) { return s1 / s2; }

    @Override public double applyToByte(byte b1, byte b2) { return b1 / b2; }
  },
  /**
   * Represents the modulus aka modulo, (often denoted as "%") arithmetic operation in mathematics. Modulus returns the
   * remainder of a division operation.
   * <br />
   * <b>For example,</b> 1 % 1 = 0
   * <br />
   * <b>For example,</b> 1 % 2 = 1
   * <p />
   * An easy way to remember modulus' function is to think of a 12-hour clock: at 13:00 hours the clock will land at 1
   * o'clock. In computing, 13 % 12 will return 1. Which is the same as the preceding example.
   */
  MODULUS('%') {
    @Override public double applyToDouble(double d1, double d2) { return d1 % d2; }

    @Override public double applyToLong(long l1, long l2) { return l1 % l2; }

    @Override public double applyToInt(int i1, int i2) { return i1 % i2; }

    @Override public double applyToFloat(float f1, float f2) { return f1 % f2; }

    @Override public double applyToShort(short s1, short s2) { return s1 % s2; }

    @Override public double applyToByte(byte b1, byte b2) { return b1 % b2; }
  },
  /**
   * Represents the Exponentiation (often denoted as "^") arithmetic operation in mathematics.
   * <br />
   * <b>For example,</b> 2 ^ 2 = 4
   */
  POWER('^') {
    @Override public double applyToDouble(double d1, double d2) { return Math.pow(d1, d2); }

    @Override public double applyToLong(long l1, long l2) { return Math.pow(l1, l2); }

    @Override public double applyToInt(int i1, int i2) { return (int) Math.pow(i1, i2); }

    @Override public double applyToFloat(float f1, float f2) { return Math.pow(f1, f2); }

    @Override public double applyToShort(short s1, short s2) { return Math.pow(s1, s2); }

    @Override public double applyToByte(byte b1, byte b2) { return Math.pow(b1, b2); }
  };

  private final char opChar;

  public static ArithmeticOperator fromChar(char c) {
    switch (c) {
      case '+':
        return ADDITION;
      case '-':
        return SUBTRACTION;
      case '*':
        return MULTIPLICATION;
      case '/':
        return DIVISION;
      case '%':
        return MODULUS;
      case '^':
        return POWER;
      default:
        throw new IllegalArgumentException("Unknown operator from character " + c);
    }
  }

  ArithmeticOperator(char opChar) {
    this.opChar = opChar;
  }

  public abstract double applyToDouble(double d1, double d2);

  public abstract double applyToLong(long l1, long l2);

  public abstract double applyToInt(int i1, int i2);

  public abstract double applyToFloat(float f1, float f2);

  public abstract double applyToShort(short s1, short s2);

  public abstract double applyToByte(byte b1, byte b2);

  public char getChar() {
    return opChar;
  }
}
