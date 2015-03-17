package com.supaham.commons.exceptions;

/**
 * Represents a commons library exception that can be thrown at any time.
 * 
 * @since 0.1
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
