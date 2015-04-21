package com.supaham.commons.state;

/**
 * An enumeration of states that a {@link Stateable} object can be.
 */
public enum State {
  ACTIVE, STOPPED, PAUSED;

  public boolean isIdle() {
    return this != ACTIVE;
  }
}
