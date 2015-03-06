package com.supaham.commons.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;

import java.util.Random;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Random} instances. This class contains methods such as
 * {@link #nextInt(int)}, {@link #nextLong(long)}, and more.
 *
 * @since 0.1
 */
public class RandomUtils {

  private static Random random = new Random();

  /**
   * Returns a pseudo-random int between min and max, inclusive. This is equivalent to calling
   * {@link #nextInt(Random, int, int)} with {@link #getRandom()}.
   *
   * @param min min range
   * @param max max range
   *
   * @return random int in the given range.
   */
  public static int nextInt(int min, int max) {
    return nextInt(random, min, max);
  }

  /**
   * Returns a pseudo-random int between min and max, inclusive.
   *
   * @param random random instance to use
   * @param min min range
   * @param max max range
   *
   * @return random int in the given range.
   */
  public static int nextInt(@Nonnull Random random, int min, int max) {
    checkNotNull(random, "random cannot be null.");
    return random.nextInt((max - min) + 1) + min;
  }

  /**
   * Returns a pseudorandom, uniformly distributed int value between the given {@link Range}'s lower
   * end point and upper end point. This is equivalent to calling {@link #nextInt(Random, Range)}
   * using {@link #getRandom()}.
   *
   * @param range range to generate int from
   *
   * @return a pseudorandom value
   */
  public static int nextInt(Range<Integer> range) {
    return nextInt(random, range);
  }

  /**
   * Returns a pseudorandom, uniformly distributed int value between the given {@link Range}'s lower
   * end point and upper end point.
   *
   * @param random random instance to use
   * @param range range to generate int from
   *
   * @return a pseudorandom value
   */
  public static int nextInt(@Nonnull Random random, @Nonnull Range<Integer> range) {
    checkNotNull(range, "range cannot be null.");
    return nextInt(random, range.lowerEndpoint(), range.upperEndpoint());
  }

  /**
   * Returns a pseudorandom, uniformly distributed long value between 0 (inclusive) and the
   * specified value (exclusive), drawn from this random number generator's sequence.
   *
   * @param bound maximum long generation boundary
   *
   * @return the next pseudorandom, uniformly distributed long value between 0 (inclusive) and n
   * (exclusive) from this random number generator's sequence
   *
   * @see #nextLong(Random, long)
   */
  public static long nextLong(long bound) {
    return nextLong(0, bound);
  }

  /**
   * Returns a pseudorandom, uniformly distributed long value between 0 (inclusive) and the
   * specified value (exclusive), drawn from this random number generator's sequence.
   *
   * @param random random instance to use
   * @param bound maximum long generation boundary
   *
   * @return the next pseudorandom, uniformly distributed long value between 0 (inclusive) and n
   * (exclusive) from this random number generator's sequence
   *
   * @see #nextLong(Random, long, long)
   */
  public static long nextLong(@Nonnull Random random, long bound) {
    return nextLong(random, 0, bound);
  }

  /**
   * Returns a pseudorandom, uniformly distributed long value between origin (inclusive) and the
   * bound (exclusive), drawn from this random number generator's sequence. <p/> <b>Note: This piece
   * of code was copied directly from JDK 1.8 with minor visual changes, but same performance.</b>
   *
   * @param origin the least value, unless greater than bound
   * @param bound the upper bound (exclusive), must not equal origin
   *
   * @return a pseudorandom value
   *
   * @see #nextLong(Random, long, long)
   */
  public static long nextLong(long origin, long bound) {
    return nextLong(random, origin, bound);
  }

  /**
   * The form of nextLong used by LongStream Spliterators. If origin is greater than bound, acts as
   * unbounded form of nextLong, else as bounded form. <p/> <b>Note: This piece of code was copied
   * directly from JDK 1.8 with minor visual changes, but same performance.</b>
   *
   * @param random random instance to use
   * @param origin the least value, unless greater than bound
   * @param bound the upper bound (exclusive), must not equal origin
   *
   * @return a pseudorandom value
   */
  public static long nextLong(@Nonnull Random random, long origin, long bound) {
    checkNotNull(random, "random cannot be null.");
    long r = random.nextLong();
    if (origin < bound) {
      long n = bound - origin, m = n - 1;
      if ((n & m) == 0L) { // power of two
        r = (r & m) + origin;
      } else if (n > 0L) {
        for (long u = r >>> 1; // ensure nonnegative
             u + m - (r = u % n) < 0L; // rejection check
             u = random.nextLong() >>> 1) { // retry
        }
        r += origin;
      } else {
        while (r < origin || r >= bound) {
          r = random.nextLong();
        }
      }
    }
    return r;
  }

  /**
   * Returns a pseudo-random double between min and max, inclusive.
   *
   * @param min min range
   * @param max max range
   *
   * @return random double in the given range.
   */
  public static double getRandomDouble(double min, double max) {
    return min + (max - min) * random.nextDouble();
  }

  public static Random getRandom() {
    return random;
  }
  
  /* ================================
   * >> DELEGATE METHODS
   * ================================ */

  /**
   * @see Random#nextBytes(byte[])
   */
  public static void nextBytes(byte[] bytes) {
    random.nextBytes(bytes);
  }

  /**
   * @see Random#nextInt()
   */
  public static int nextInt() {
    return random.nextInt();
  }

  /**
   * @see Random#nextInt(int)
   */
  public static int nextInt(int bound) {
    return random.nextInt(bound);
  }

  /**
   * @see Random#nextLong()
   */
  public static long nextLong() {
    return random.nextLong();
  }

  /**
   * @see Random#nextBoolean()
   */
  public static boolean nextBoolean() {
    return random.nextBoolean();
  }

  /**
   * @see Random#nextFloat()
   */
  public static float nextFloat() {
    return random.nextFloat();
  }

  /**
   * @see Random#nextDouble()
   */
  public static double nextDouble() {
    return random.nextDouble();
  }

  /**
   * @see Random#nextGaussian()
   */
  public static double nextGaussian() {
    return random.nextGaussian();
  }
}
