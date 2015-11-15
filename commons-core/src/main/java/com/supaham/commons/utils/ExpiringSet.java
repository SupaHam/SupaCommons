package com.supaham.commons.utils;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link Set} with expiring elements. The expiry countdown starts when an item has
 * been added to this set using {@link #add(Object)}.
 *
 * @param <E> type of element this set contains
 */
public class ExpiringSet<E> extends AbstractSet<E> implements Set<E> {

  // Dummy value to associate with an Object in the backing Cache
  private static final Object PRESENT = new Object();

  private final Cache<E, Object> cache;

  public ExpiringSet(long duration, @Nonnull TimeUnit unit) {
    this(duration, unit, null);
  }

  public ExpiringSet(long duration, @Nonnull TimeUnit unit,
                     @Nullable final RemovalListener<E> removalListener) {
    Preconditions.checkNotNull(unit, "unit cannot be null.");
    Preconditions.checkArgument(duration > 0, "duration must be positive: %s %s", duration, unit);

    CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder()
        .expireAfterWrite(duration, unit);

    if (removalListener != null) {
      builder.removalListener(new com.google.common.cache.RemovalListener<E, Object>() {
        @Override public void onRemoval(@Nonnull RemovalNotification<E, Object> notification) {
          removalListener.onRemoval(notification.getKey());
        }
      });
    }

    cache = builder.build();
  }

  @Override public int size() {
    return (int) cache.size();
  }

  @Override public boolean isEmpty() {
    return size() == 0;
  }

  @Override public boolean contains(Object o) {
    return cache.getIfPresent(o) != null;
  }

  @NotNull @Override public Iterator<E> iterator() {
    return cache.asMap().keySet().iterator();
  }

  @NotNull @Override public Object[] toArray() {
    return cache.asMap().keySet().toArray();
  }

  @NotNull @Override public <T> T[] toArray(@Nonnull T[] a) {
    return cache.asMap().keySet().toArray(a);
  }

  @Override public boolean add(E e) {
    int size = size();
    cache.put(e, PRESENT);
    return size != size();
  }

  @Override public boolean remove(Object o) {
    int size = size();
    cache.invalidate(o);
    return size != size();
  }

  @Override public void clear() {
    cache.invalidateAll();
  }

  /**
   * Cleans up this set. This refreshes this set and expires outdated elements. Otherwise, expiries 
   * occur during read and write operations.
   */
  public void cleanUp() {
    this.cache.cleanUp();
  }

  /**
   * Interface used for notifying when an element has been removed from an {@link ExpiringSet}.
   */
  public interface RemovalListener<E> {

    /**
     * Called when an element has been removed from an {@link ExpiringSet}.
     *
     * @param e element, nonnull
     */
    void onRemoval(@Nonnull E e);
  }
}
