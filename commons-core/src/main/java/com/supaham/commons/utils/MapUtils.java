package com.supaham.commons.utils;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link Map} instances. This class contains methods such as
 * {@link #removeValue(Map, Object)}.
 *
 * @since 0.1
 */
public class MapUtils {

  /**
   * Removes a value from a {@link Map}. The return type is whatever the Map's value generic type
   * is.
   *
   * @param map map to remove value from
   * @param value value to remove
   * @param <V> type of the value to remove and return, retrieved through the map generic type
   * value.
   *
   * @return removed value
   */
  @Nullable
  public static <V> V removeValue(Map<?, V> map, V value) {
    for (Entry<?, V> entry : map.entrySet()) {
      if (entry.getValue().equals(value)) {
        map.remove(entry.getKey());
        return entry.getValue();
      }
    }
    return null;
  }
}
