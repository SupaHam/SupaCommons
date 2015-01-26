package com.supaham.commons;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

/**
 * Created by Ali on 11/01/2015.
 */
public final class Enums {
  private Enums() {
  }

  /**
   * Search the given enum for a value that is equal to the one of the given values, searching in 
   * an ascending manner.
   *
   * @param enumType the enum type
   * @param values the list of values
   * @param <T> the type of enum
   * @return the found value or null
   */
  @Nullable
  public static <T extends Enum<T>> T findByValue(Class<T> enumType, String... values) {
    checkNotNull(enumType);
    checkNotNull(values);
    for (String val : values) {
      try {
        return Enum.valueOf(enumType, val);
      } catch (IllegalArgumentException ignored) {}
    }
    return null;
  }

  /**
   * Search the given enum for a value that is equal to the one of the given values, searching in 
   * an ascending manner.
   *
   * <p>Some fuzzy matching of the provided values may be performed.</p>
   *
   * @param enumType the enum type
   * @param values the list of values
   * @param <T> the type of enum
   * @return the found value or null
   */
  @Nullable
  public static <T extends Enum<T>> T findFuzzyByValue(Class<T> enumType, String... values) {
    checkNotNull(enumType);
    checkNotNull(values);
    for (String test : values) {
      test = test.replace("_", "");
      for (T value : enumType.getEnumConstants()) {
        if (value.name().replace("_", "").equalsIgnoreCase(test)) {
          return value;
        }
      }
    }
    return null;
  }
}
