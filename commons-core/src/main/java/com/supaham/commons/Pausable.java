package com.supaham.commons;

/**
 * Represents am object with the ability to pause and resume.
 */
public interface Pausable {

  /**
   * Pauses this object.
   *
   * @return whether the operation was successful, typically false if the state is already paused
   */
  boolean pause();

  /**
   * Resumes this object.
   *
   * @return whether the operation was successful, typically false if the state is already resumed
   */
  boolean resume();

  /**
   * Returns whether this object is paused.
   *
   * @return whether this object is paused
   */
  boolean isPaused();
}
