package com.supaham.commons.collections;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link Collection} that may or may not be immutable, throwing {@link IllegalArgumentException} when
 * an attempt to modify the underlying collection. Immutability can be checked via {@link
 * PossiblyImmutableCollection#isImmutable()}.
 */
public class PossiblyImmutableCollection<E> implements Collection<E> {

  private final Collection<E> original;
  private final boolean immutable;

  public PossiblyImmutableCollection() {
    this.original = Collections.emptyList();
    this.immutable = true;
  }

  public PossiblyImmutableCollection(@Nullable Collection<E> original) {
    this(original == null ? Collections.emptyList() : original, original == null);
  }

  public PossiblyImmutableCollection(@Nonnull Collection<E> original, boolean immutable) {
    this.original = Preconditions.checkNotNull(original, "original cannot be null.");
    this.immutable = immutable;
  }

  protected Collection<E> getOriginal() {
    return original;
  }

  public boolean isImmutable() {
    return immutable;
  }
  
  /* ================================
   * >> READ METHODS
   * ================================ */


  @Override public int size() {
    return original.size();
  }

  @Override public boolean isEmpty() {
    return original.isEmpty();
  }

  @Override public boolean contains(Object o) {
    return original.contains(o);
  }

  @Override public Iterator<E> iterator() {
    return original.iterator();
  }

  @Override public Object[] toArray() {
    return original.toArray();
  }

  @Override public <T> T[] toArray(T[] a) {
    return original.toArray(a);
  }

  @Override public boolean containsAll(Collection<?> c) {
    return original.containsAll(c);
  }
  
  /* ================================
   * >> MODIFICATION METHODS
   * ================================ */

  @Override public boolean add(E e) {
    return check().add(e);
  }

  @Override public boolean remove(Object o) {
    return check().remove(o);
  }

  @Override public boolean addAll(Collection<? extends E> c) {
    return check().addAll(c);
  }

  @Override public boolean removeAll(Collection<?> c) {
    return check().removeAll(c);
  }

  @Override public boolean retainAll(Collection<?> c) {
    return check().retainAll(c);
  }

  @Override public void clear() {
    check().clear();
  }

  protected Collection<E> check() {
    if (isImmutable()) {
      throw new IllegalStateException("Immutable collection may not be modified.");
    }
    return this.original;
  }
}
