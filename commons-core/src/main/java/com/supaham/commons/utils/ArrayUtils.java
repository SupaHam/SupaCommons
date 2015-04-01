package com.supaham.commons.utils;

import java.util.Arrays;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with arrays. This class contains methods such as {@link
 * #removeEmptyStrings(String[])}, {@link #getTotalStringsLength(String[])}, and more.
 *
 * @since 0.1
 */
public final class ArrayUtils extends org.apache.commons.lang.ArrayUtils {

  private ArrayUtils() {
  }

  /**
   * Removes empty (null or literally of length 0) {@link String}s from an array.
   *
   * <pre>
   *   ArrayUtils.removeEmptyStrings({"abc", ""})            = {"abc"}
   *   ArrayUtils.removeEmptyStrings({"abc", " "})           = {"abc"}
   *   ArrayUtils.removeEmptyStrings({"abc", " ", " def "})  = {"abc", " def "}
   * </pre>
   *
   * @param original original to use
   *
   * @return a new {@link String} array with the results, or if the provided array is empty, itself
   * is returned.
   */
  @Nonnull
  public static String[] removeEmptyStrings(@Nonnull String[] original) {
    return removeEmptyStrings(original, true);
  }

  /**
   * Removes empty (null or literally of length 0) {@link String}s from an array.
   *
   * <pre>
   *   ArrayUtils.removeEmptyStrings({"abc", ""}, false)           = {"abc"}
   *   ArrayUtils.removeEmptyStrings({"abc", ""}, true)            = {"abc"}
   *   ArrayUtils.removeEmptyStrings({"abc", " "}, false)          = {"abc", " "}
   *   ArrayUtils.removeEmptyStrings({"abc", " "}, true)           = {"abc"}
   *   ArrayUtils.removeEmptyStrings({"abc", " ", " def "}, true)  = {"abc", " def "}
   * </pre>
   *
   * @param original original to use
   * @param trim whether to trim each string when checking length, keep in mind this does not
   * actually modify the String itself in the returned array
   *
   * @return a new {@link String} array with the results, or if the provided array is empty, itself
   * is returned.
   */
  @Nonnull
  public static String[] removeEmptyStrings(@Nonnull String[] original, boolean trim) {
    if (original.length == 0) {
      return original;
    }

    String[] strings = new String[original.length];
    int length = 0;
    for (String s : original) {
      if (s != null) {
        String trimmed = trim ? s.trim() : s;
        if (!trimmed.isEmpty()) {
          strings[length++] = s;
        }
      }
    }
    return Arrays.copyOfRange(strings, 0, length);
  }

  public static int getTotalStringsLength(@Nonnull String[] arr) {
    return getTotalStringsLength(arr, true);
  }

  public static int getTotalStringsLength(@Nonnull String[] arr, boolean trim) {
    int length = 0;
    for (String s : arr) {
      String trimmed = trim ? s.trim() : s;
      if (!trimmed.isEmpty()) {
        length += s.length();
      }
    }
    return length;
  }

  /**
   * Checks if an array contains a null object. If it does, a {@link NullPointerException} is thrown
   * with the message 'array contains null element.'.
   *
   * @param array array to check
   *
   * @return the same {@code array} for chaining
   */
  public static Object[] checkForNullElements(Object[] array) {
    return checkForNullElements(array, "array contains null element.");
  }

  /**
   * Checks if an array contains a null object. If it does, a {@link NullPointerException} is thrown
   * with the description specified.
   *
   * @param array array to check
   * @param desc detailed message to pass along side the {@link NullPointerException}
   *
   * @return the same {@code array} for chaining
   */
  public static Object[] checkForNullElements(Object[] array, String desc) {
    for (Object o : array) {
      if (o == null) {
        throw new NullPointerException(desc);
      }
    }
    return array;
  }

  /**
   * Gets a random element in an array.
   *
   * @param array array to use
   *
   * @return the random element
   */
  public static <T> T getRandomElement(T[] array) {
    return array[RandomUtils.nextInt(array.length)];
  }
}
