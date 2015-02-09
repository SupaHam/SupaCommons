package com.supaham.commons.exceptions;

/**
 * Represents a {@link RuntimeException} thrown when a duration string has failed to parse.
 */
public class DurationParseException extends RuntimeException {

  public DurationParseException(String message) {
    super(message);
  }

  public DurationParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
