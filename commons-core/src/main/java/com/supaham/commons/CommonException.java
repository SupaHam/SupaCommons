package com.supaham.commons;

/**
 * Represents a commons library exception that can be thrown at any time.
 */
public class CommonException extends Exception {

  public CommonException(String message) {
    super(message);
  }

  public CommonException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommonException(Throwable cause) {
    super(cause);
  }
}
