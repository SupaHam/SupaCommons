package com.supaham.commons.exceptions;

/**
 * Represents a {@link RuntimeException} thrown when a duration string has failed to parse.
 *
 * @since 0.1
 */
public class DurationParseException extends RuntimeException {

  public DurationParseException(String message) {
    super(message);
  }

  public DurationParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
