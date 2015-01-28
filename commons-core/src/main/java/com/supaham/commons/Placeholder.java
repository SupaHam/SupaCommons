package com.supaham.commons;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a placeholder {@link Callable} that returns a {@link String}.
 */
public interface Placeholder {

  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @param placeholder the given placeholder to test
   *
   * @return computed result
   *
   * @throws Exception if unable to compute a result
   */
  @Nullable
  String call(String placeholder) throws Exception;

  /**
   * This method is called when this {@link Placeholder} has taken part in a placeholder replacing
   * process during the {@link #call(String)} call. To clarify, this method is called after all
   * {@link Placeholder}s' {@link #call(String)} methods have been invoked.
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
