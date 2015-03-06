package com.supaham.commons.placeholders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.supaham.commons.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an abstract simple {@link Placeholder} implementation that provides a constructor
 * that takes an array of {@link String}s that is placeholders that this.
 * 
 * @since 0.1
 */
public abstract class SimplePlaceholder implements Placeholder {

  @Nonnull
  private final List<String> placeholders;

  public SimplePlaceholder(@Nonnull String... placeholders) {
    checkNotNull(placeholders, "placeholders cannot be null.");
    checkArgument(placeholders.length > 0, "placeholders cannot be empty.");
    ArrayUtils.checkForNullElements(placeholders, "placeholder cannot be null.");
    
    this.placeholders = new ArrayList<>(placeholders.length);
    Collections.addAll(this.placeholders, placeholders);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onComplete(String string) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlaceholder(@Nullable String string) {
    return this.placeholders.contains(string);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<String> getPlaceholders() {
    return placeholders;
  }
}
