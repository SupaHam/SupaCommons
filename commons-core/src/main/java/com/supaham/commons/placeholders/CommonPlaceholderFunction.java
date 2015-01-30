package com.supaham.commons.placeholders;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Represents a simple extension of {@link PlaceholderFunction} that provides 
 * {@link CommonPlaceholderFunction#CommonPlaceholderFunction(Collection)} and implements the 
 * {@link PlaceholderFunction#getPlaceholders()} method.
 */
public class CommonPlaceholderFunction extends PlaceholderFunction {
  
  private Collection<? extends Placeholder> collection;

  public CommonPlaceholderFunction(@Nonnull Collection<? extends Placeholder> collection) {
    this.collection = collection;
  }

  @Override
  public Collection<? extends Placeholder> getPlaceholders() {
    return this.collection;
  }
}
