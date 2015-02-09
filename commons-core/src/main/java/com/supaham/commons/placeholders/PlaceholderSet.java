package com.supaham.commons.placeholders;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link HashSet} of {@link Placeholder}s. This class also provides {@link
 * #apply(String)} which performs the placeholder strings replacing task.
 */
public class PlaceholderSet<T extends Placeholder> extends HashSet<T> {

  private PlaceholderFunction placeholderFunction = new CommonPlaceholderFunction(this);

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet() {
    super();
  }

  /**
   *
   */
  public PlaceholderSet(@Nonnull PlaceholderFunction function) {
    super();
    checkNotNull(function);
    this.placeholderFunction = function;
  }

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet(Collection<T> c) {
    this(c, null);
  }

  /**
   * @param function function to handle placeholders. If null, the default is used instead
   *
   * @see #PlaceholderSet(Collection)
   */
  public PlaceholderSet(Collection<T> c, @Nullable PlaceholderFunction function) {
    super(c);
    if (function != null) {
      this.placeholderFunction = function;
    }
  }

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet(int initialCapacity, float loadFactor) {
    this(initialCapacity, loadFactor, null);
  }

  /**
   * @param function function to handle placeholders. If null, the default is used instead
   *
   * @see #PlaceholderSet(int, float)
   */
  public PlaceholderSet(int initialCapacity, float loadFactor,
                        @Nullable PlaceholderFunction function) {
    super(initialCapacity, loadFactor);
    if (function != null) {
      this.placeholderFunction = function;
    }
  }

  /**
   * {@inheritDoc}
   */
  public PlaceholderSet(int initialCapacity) {
    this(initialCapacity, null);
  }

  /**
   * @param function function to handle placeholders. If null, the default is used instead
   *
   * @see #PlaceholderSet(int)
   */
  public PlaceholderSet(int initialCapacity, @Nullable PlaceholderFunction function) {
    super(initialCapacity);
    if (function != null) {
      this.placeholderFunction = function;
    }
  }

  /**
   * Checks if this {@link PlaceholderSet} has a {@link Placeholder} with a given {@link String} as
   * a valid placeholder.
   *
   * @param placeholder placeholder string to check
   *
   * @return true if the given {@code placeholder} string belongs to one of the {@link Placeholder}s
   * registered to this {@link PlaceholderSet}
   *
   * @see Placeholder#isPlaceholder(String)
   */
  public boolean hasPlaceholder(@Nonnull String placeholder) {
    if (placeholder != null && !placeholder.isEmpty()) {
      for (Placeholder placeholder1 : this) {
        if (placeholder1.isPlaceholder(placeholder)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Performs a placeholder replacing task using all the {@link Placeholder}s in this {@link
   * PlaceholderSet}.
   *
   * @param input input to search for placeholders in
   *
   * @return the placeholder replaced string
   *
   * @see #apply(PlaceholderData)
   */
  @Nonnull
  public String apply(@Nonnull String input) {
    return apply(PlaceholderData.build(input));
  }

  /**
   * Performs a placeholder replacing task using all the {@link Placeholder}s in this {@link
   * PlaceholderSet} on an {@link ArrayList}.
   *
   * @param list list input to search for and replace placeholders in
   *
   * @return the same {@code list} instance provided, but with its elements replaced
   *
   * @see #apply(PlaceholderData)
   */
  public List<String> apply(@Nonnull List<String> list) {
    for (int i = 0; i < list.size(); i++) {
      // Use get and set instead of add and remove in case the list does not support remove(). 
      list.set(i, apply(list.get(i)));
    }
    return list;
  }

  /**
   * Performs a placeholder replacing task using all the {@link Placeholder}s in this {@link
   * PlaceholderSet}.
   *
   * @param data placeholder data to perform the placeholder function with
   *
   * @return the placeholder replaced string
   *
   * @see PlaceholderFunction#apply(PlaceholderData)
   */
  @Nonnull
  public String apply(@Nonnull PlaceholderData data) {
    return placeholderFunction.apply(data);
  }

  /**
   * Gets the {@link PlaceholderFunction} of this {@link PlaceholderSet}.
   *
   * @return placeholder function
   */
  public PlaceholderFunction getPlaceholderFunction() {
    return placeholderFunction;
  }

  /**
   * Sets the {@link PlaceholderFunction} of this {@link PlaceholderSet} for overriding the
   * default.
   *
   * @param function placeholder function
   */
  public void setPlaceholderFunction(PlaceholderFunction function) {
    this.placeholderFunction = function;
  }
}
