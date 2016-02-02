package com.supaham.commons.utils;

/**
 * Represents a utility class  for methods that interact with {@link Throwable}, {@link Error}, and {@link Exception}.
 */
public class ThrowableUtils {

  /**
   * Returns the cause of a {@link Throwable} if present, otherwise the given throwable is returned.
   *
   * @param throwable throwable to get cause from
   *
   * @return cause or {@code throwable}
   */
  public static Throwable getCause(Throwable throwable) {
    if (throwable == null) {
      return null;
    }
    if (throwable.getCause() != null) {
      return throwable.getCause();
    }
    return throwable;
  }

  private ThrowableUtils() {
    throw new AssertionError("Not sure if this is in an Inception or Exception.");
  }
}
