package com.supaham.commons;

import com.google.common.base.Function;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a placeholder {@link Callable} that returns a {@link String}.
 */
public interface Placeholder extends Function<String, String> {

  /**
   * This method is called with a {@link String} as a placeholder. This method specifies what should
   * be replaced instead of the given placeholder using the return value. Please keep in mind that
   * any fixes to the placeholder, such as braces ({}) or percent signs (%), are not provided.
   *
   * @return the string to replace the {@code input} with
   */
  @Nullable
  String apply(String input);

  /**
   * This method is called when this {@link Placeholder} has taken part in a placeholder replacing
   * process during the {@link #apply(String)} call. To clarify, this method is called after all
   * {@link Placeholder}s' {@link #apply(String)} methods have been invoked.
   *
   * @param string the final string
   */
  void onComplete(String string);

  /**
   * Checks whether a {@link String} is a valid placeholder for this {@link Placeholder} instance.
   *
   * @param string placeholder to test
   *
   * @return true if the {@code string} is a valid placeholder
   */
  boolean isPlaceholder(@Nullable String string);

  /**
   * Gets a {@link List} of placeholders that this callable should interfere with.
   *
   * @return a List of placeholders
   */
  @Nonnull
  List<String> getPlaceholders();
}
