package com.supaham.commons.utils;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Map} builder. The process starts with {@link #builder(Map)}, then followed
 * by {@link #put(Object, Object)}, and lastly {@link #build()} to retrieve the initial map.
 */
public class MapBuilder<M extends Map<K, V>, K, V> {

  private final M map;
  private boolean built;

  /**
   * Returns a {@link HashMap} {@link MapBuilder}. This is equivalent to calling {@link
   * #builder(Map)} with {@code new HashMap()}.
   *
   * @param <K> key type
   * @param <V> value type
   *
   * @return map builder
   */
  public static <K, V> MapBuilder<HashMap<K, V>, K, V> newHashMap() {
    return builder(new HashMap<K, V>());
  }

  /**
   * Returns a {@link LinkedHashMap} {@link MapBuilder}. This is equivalent to calling {@link
   * #builder(Map)} with {@code new LinkedHashMap()}.
   *
   * @param <K> key type
   * @param <V> value type
   *
   * @return map builder
   */
  public static <K, V> MapBuilder<LinkedHashMap<K, V>, K, V> newLinkedHashMap() {
    return builder(new LinkedHashMap<K, V>());
  }

  /**
   * Creates a new {@link MapBuilder} with a map.
   *
   * @param map map to build on
   * @param <M> map type
   * @param <K> key type
   * @param <V> value type
   *
   * @return map builder
   */
  public static <M extends Map<K, V>, K, V> MapBuilder<M, K, V> builder(M map) {
    return new MapBuilder<>(map);
  }

  private MapBuilder(@Nonnull M map) {
    this.map = Preconditions.checkNotNull(map, "map cannot be null.");
  }

  private void checkState() {
    Preconditions.checkState(!built, "Map already built.");
  }

  /**
   * Inserts an entry into this builder. This is inserted live into the initial map.
   *
   * @param key key of the pair
   * @param value value of the pair
   *
   * @return this map builder for chaining
   */
  public MapBuilder<M, K, V> put(K key, V value) {
    checkState();
    map.put(key, value);
    return this;
  }

  /**
   * Returns the initial map with all entries.
   *
   * @return map
   */
  public M build() {
    checkState();
    built = true;
    return map;
  }
}
