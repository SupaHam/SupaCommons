package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

/**
 * Represents a delayed iterator using {@link TickerTask}. Similar to an iterator, this class
 * iterates over a {@link Collection} which is supplied through a {@link Supplier}, at a set
 * interval.
 * <p />
 * Since this functionality is delayed, each iteration may be accessed through
 * {@link #onRun(Object)} and once the iterator is done, {@link #onDone()} is called, which by
 * default, is an empty implementation.
 * <p />
 * Although this class extends {@link TickerTask}, delay is not supported as it is unnecessary. The
 * iterator may be controlled through {@link TickerTask}'s state changing methods.
 * <p />
 * Although this class has the word iterator in it, it is anything but {@link Iterator}. Since
 * the whole iteration is delayed, the implementation of {@link Iterator} would be invalid for the
 * following reasons: this class does not support element removals (write), only read. And the
 * {@link Iterator} requires {@link Iterator#next()}, which does exist in this class
 * ({@link #next()}), but is NOT recommended to be used as it may cause instability issues across 
 * implementations, but is there for whatever reason someone might wish to use it for.
 *
 * @see #DelayedIterator(Plugin, Supplier, long)
 * @since 0.2
 */
public abstract class DelayedIterator<T> extends TickerTask {

  private final Supplier<Collection<T>> supplier;
  private Iterator<T> iterator;

  /**
   * Constructs a new {@link DelayedIterator} instance with a {@link Plugin} as the owner, a
   * {@link Supplier}, and 1 as the interval delay, iterating every tick.
   *
   * @param plugin plugin to handle this iterator
   * @param supplier suuplier to supply the collection to iterate over
   */
  public DelayedIterator(@Nonnull Plugin plugin, @Nonnull Supplier<Collection<T>> supplier) {
    this(plugin, supplier, 1);
  }

  /**
   * Constructs a new {@link DelayedIterator} instance with a {@link Plugin} as the owner, a
   * {@link Supplier}, and a set interval (in ticks) delay per iteration.
   *
   * @param plugin plugin to handle this iterator
   * @param supplier suuplier to supply the collection to iterate over
   * @param interval interval (in ticks) between each iteration
   */
  public DelayedIterator(@Nonnull Plugin plugin, @Nonnull Supplier<Collection<T>> supplier,
                         long interval) {
    super(plugin, 0, interval);
    this.supplier = Preconditions.checkNotNull(supplier, "supplier cannot be null.");
  }

  /**
   * This method is called per iteration of an object.
   *
   * @param t current element in the iterator
   */
  public abstract void onRun(T t);

  /**
   * This method is called when the whole iteration is finished.
   */
  public void onDone() {}

  @Override
  public final void run() {
    if (this.iterator == null) {
      this.iterator = this.supplier.get().iterator();
    }
    if (this.iterator.hasNext()) {
      onRun(this.iterator.next());
    }
    if (!this.iterator.hasNext()) { // We're done.
      stop();
      onDone();
    }
  }

  @Override
  public boolean stop() {
    boolean b = super.stop();
    this.iterator = null;
    return b;
  }

  /**
   * Returns true if the iteration has more elements. (In other words, returns true if
   * {@link #next} would return an element rather than throwing an exception.)
   *
   * @return whether the iteration has more elements
   *
   * @see {@link Iterator#hasNext()}
   */
  public boolean hasNext() {
    Iterator<T> it = this.iterator;
    return it != null && it.hasNext();
  }

  /**
   * Returns the next element in the iteration.
   * <p />
   * <b>NOTE: This method triggers {@link #onRun(Object)} assuming no exception is thrown,
   * please use with caution.</b>
   *
   * @return the next element in the iteration
   *
   * @throws NoSuchElementException if the iteration has no more elements
   */
  public T next() throws NoSuchElementException {
    T next = this.iterator.next();
    onRun(next);
    return next;
  }

  /**
   * Gets the collection {@link Supplier} of this delayed iterator.
   *
   * @return supplier of a collection
   */
  public Supplier<Collection<T>> getSupplier() {
    return supplier;
  }
}
