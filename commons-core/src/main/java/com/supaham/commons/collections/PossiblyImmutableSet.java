package com.supaham.commons.collections;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link Set} that may or may not be immutable, throwing {@link IllegalArgumentException} when
 * an attempt to modify the underlying collection. Immutability can be checked via {@link
 * PossiblyImmutableCollection#isImmutable()}.
 */
public class PossiblyImmutableSet<E> extends PossiblyImmutableCollection<E> implements Set<E> {

  public PossiblyImmutableSet() {
  }

  public PossiblyImmutableSet(@Nullable Set<E> original) {
    super(original);
  }

  public PossiblyImmutableSet(@Nonnull Set<E> original, boolean immutable) {
    super(original, immutable);
  }
}
