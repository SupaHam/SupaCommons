package com.supaham.commons.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link List} that may or may not be immutable, throwing {@link IllegalArgumentException} when
 * an attempt to modify the underlying collection. Immutability can be checked via {@link
 * PossiblyImmutableCollection#isImmutable()}.
 */
public class PossiblyImmutableList<E> extends PossiblyImmutableCollection<E> implements List<E> {

  public PossiblyImmutableList() {}

  public PossiblyImmutableList(@Nullable List<E> original) {
    super(original);
  }

  public PossiblyImmutableList(@Nonnull List<E> original, boolean immutable) {
    super(original, immutable);
  }

  /* ================================
   * >> READ METHODS
   * ================================ */

  @Override public E get(int index) {
    return getOriginal().get(index);
  }

  @Override public int indexOf(Object o) {
    return getOriginal().indexOf(o);
  }

  @Override public int lastIndexOf(Object o) {
    return getOriginal().lastIndexOf(o);
  }

  @NotNull @Override public ListIterator<E> listIterator() {
    return getOriginal().listIterator();
  }

  @NotNull @Override public ListIterator<E> listIterator(int index) {
    return getOriginal().listIterator(index);
  }

  /* ================================
   * >> MODIFICATION METHODS
   * ================================ */

  @Override public boolean addAll(int index, Collection<? extends E> c) {
    return check().addAll(index, c);
  }

  @Override public E set(int index, E element) {
    return check().set(index, element);
  }

  @Override public void add(int index, E element) {
    check().add(index, element);
  }

  @Override public E remove(int index) {
    return check().remove(index);
  }

  @NotNull @Override public List<E> subList(int fromIndex, int toIndex) {
    return check().subList(fromIndex, toIndex);
  }

  @Override protected List<E> getOriginal() {
    return (List<E>) super.getOriginal();
  }

  @Override protected List<E> check() {
    return (List<E>) super.check();
  }
}
