package com.supaham.commons.state;

import javax.annotation.Nonnull;

/**
 * Represents an object with a {@link State}.
 */
public interface Stateable {

  /**
   * Gets the {@link State} of this object.
   *
   * @return state, not null
   */
  @Nonnull
  State getState();

  /**
   * Sets the {@link State} of this object. If the previous state was idle and the new one is idle,
   * false is returned.
   *
   * @param state state to set
   *
   * @return whether there was a change in state
   *
   * @throws UnsupportedOperationException thrown if this object does not support setting state
   */
  boolean setState(State state) throws UnsupportedOperationException;
}
