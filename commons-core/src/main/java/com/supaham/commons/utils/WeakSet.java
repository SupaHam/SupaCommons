package com.supaham.commons.utils;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

/**
 * Represents a delegation for a {@link WeakHashMap} that supports taking a single Element instead
 * of a WeakHashMap with object fillers, etc.
 * @param <E> Type of set
 */
public class WeakSet<E> extends AbstractSet<E> {

  private static final Object OBJECT_FILLER = new Object();
  private final WeakHashMap<E, Object> map;

  public WeakSet() {
    this.map = new WeakHashMap<>();
  }

  public WeakSet(int expectedSize) {
    this.map = new WeakHashMap<>(expectedSize);
  }

  public WeakSet(int expectedSize, float loadFactor) {
    this.map = new WeakHashMap<>(expectedSize, loadFactor);
  }

  public WeakSet(Collection<? extends E> c) {
    this.map = new WeakHashMap<>(c.size());
    addAll(c);
  }

  @Override public boolean add(E e) {
    this.map.put(e, OBJECT_FILLER);
    return true;
  }

  @Override public boolean remove(Object o) {
    return this.map.remove(o) != null;
  }

  @Nonnull @Override public Iterator<E> iterator() {
    return map.keySet().iterator();
  }

  @Override public int size() {
    return map.size();
  }
}
