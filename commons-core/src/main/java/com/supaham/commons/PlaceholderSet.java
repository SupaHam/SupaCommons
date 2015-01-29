package com.supaham.commons;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Nonnull;

/**
 * Represents a {@link HashSet} of {@link Placeholder}s. This class also provides {@link
 * #apply(String)} which performs the placeholder strings replacing task.
 */
public class PlaceholderSet<T extends Placeholder> extends HashSet<T> {

  private PlaceholderFunction placeholderFunction = new PlaceholderFunction() {
    @Override
    Collection<T> getPlaceholders() {
      return PlaceholderSet.this;
    }
  };

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet() {
  }

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet(Collection<T> c) {
    super(c);
  }

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Performs a placeholder replacing task using all the {@link Placeholder}s in this {@link
   * PlaceholderSet}.
   *
   * @param input input to search for placeholders in
   *
   * @return the placeholder replaced string
   *
   * @see PlaceholderFunction#apply(String)
   */
  @Nonnull
  public String apply(String input) {
    return placeholderFunction.apply(checkNotNullOrEmpty(input));
  }
}
