package com.supaham.commons.exceptions;

/**
 * Represents a {@link RuntimeException} thrown when a time string has failed to be parsed.
 *
 * @since 0.5.2
 */
public class TimeParseException extends IllegalArgumentException {

  public TimeParseException(String timeString, String message) {
    super("Failed to parse '" + timeString + "': " + message);
  }
}
