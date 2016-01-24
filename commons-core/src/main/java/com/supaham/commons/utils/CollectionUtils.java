package com.supaham.commons.utils;

import java.util.Collection;
import java.util.List;

/**
 * Utility methods for working with {@link Collection}s. This class contains methods such as {@link
 * #getRandomElement(List)}, and more.
 *
 * @since 0.1
 */
public final class CollectionUtils {

  private CollectionUtils() {
  }

  /**
   * Gets a random element in a {@link List}.
   *
   * @param list list to use
   *
   * @return the random element
   */
  public static <T> T getRandomElement(List<T> list) {
    return list.get(RandomUtils.nextInt(list.size()));
  }

  /**
   * Gets a random element in a {@link Collection}.
   *
   * @param collection collection to use
   *
   * @return the random element
   */
  public static <T> T getRandomElement(Collection<T> collection) {
    return ((T) getRandomElement(collection.toArray()));
  }

  /**
   * @see ArrayUtils#getRandomElement(Object[])
   */
  public static <T> T getRandomElement(T[] array) {
    return ArrayUtils.getRandomElement(array);
  }

  /**
   * Returns whether a case-insensitive String is contained within a {@link Collection}.
   *
   * @param collection collection to operate in
   * @param string string to look for
   *
   * @return true if {@code string} is in {@code collection}
   */
  public static boolean containsIgnoreCase(Collection<String> collection, String string) {
    string = string.toLowerCase();
    for (String e : collection) {
      if (e.toLowerCase().equals(string)) {
        return true;
      }
    }
    return false;
  }
}
