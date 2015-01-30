package com.supaham.commons.placeholders;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import java.util.Collection;
import java.util.HashSet;

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
   * @see #PlaceholderSet(int)
   */
  public PlaceholderSet(int initialCapacity, @Nullable PlaceholderFunction function) {
    super(initialCapacity);
    if (function != null) {
      this.placeholderFunction = function;
    }
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
   * @deprecated Not sure whether this method is necessary, it could almost be considered useless.
   */
  @Nonnull
  @Deprecated
  public String apply(String input) {
    checkNotNullOrEmpty(input);
    return apply(PlaceholderData.builder().input(input).build());
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
  public String apply(PlaceholderData data) {
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
