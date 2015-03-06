package com.supaham.commons;

import static com.google.common.base.Preconditions.checkArgument;

import com.supaham.commons.utils.StringUtils;

import javax.annotation.Nonnull;

/**
 * Represents a string scroller that functions through a {@link Runnable}. Each run increments the
 * position, string, etc.. <p /> A String Scroller works as follows:<br />
 * <pre>
 *     StringScroller sc = new StringScroller("ABCDEF", 4)
 *     sc.run()
 *     sc.getCurrentString() == "ABCD"
 *     sc.run()
 *     sc.getCurrentString() == "BCDE"
 *     sc.run()
 *     sc.getCurrentString() == "CDEF"
 *     sc.run()
 *     sc.getCurrentString() == "DEFA"
 *     sc.run()
 *     sc.getCurrentString() == "EFAB"
 *     sc.run()
 *     sc.getCurrentString() == "FABC"
 *
 *     sc = new StringScroller("ABCDEF", 4, true)
 *     sc.run()
 *     sc.getCurrentString() == "ABCD"
 *     sc.run()
 *     sc.getCurrentString() == "BCDE"
 *     sc.run()
 *     sc.getCurrentString() == "CDEF"
 *     sc.run()
 *     sc.getCurrentString() == "ABCD"
 * </pre>
 *
 * @see #StringScroller(String, int)
 * @see #StringScroller(String, int, boolean)
 * @since 0.1
 */
public class StringScroller implements Runnable, Cloneable {

  private final String string;
  private final int displayLength;
  private final boolean instantlyRepeat;
  protected int position = -1;
  private boolean reset;
  private String currentString;

  /**
   * Constructs a new {@link StringScroller} with a String and a display length, and {@code
   * instantlyRepeat} as false.
   *
   * @param string string to scroll over
   * @param displayLength the threshold of the scroll, this is the length of characters displayed
   * per scroll.
   */
  public StringScroller(@Nonnull String string, int displayLength) {
    this(string, displayLength, false);
  }

  /**
   * Constructs a new {@link StringScroller} with a String and a display length. If {@code
   * instantlyRepeat} is true, the position is immediately reset once the last character of the
   * {@code string} is reached (at the end of the current string). See {@link StringScroller} for
   * more information.
   *
   * @param string string to scroll over
   * @param displayLength the threshold of the scroll, this is the length of characters displayed
   * per scroll. If this is larger than the string itself, its set to the string length.
   * @param instantlyRepeat whether to instantly repeat the string,
   *
   * @see StringScroller
   */
  public StringScroller(@Nonnull String string, int displayLength, boolean instantlyRepeat) {
    // Safe precondition
    if (displayLength > string.length()) {
      displayLength = string.length();
    }
    this.string = StringUtils.checkNotNullOrEmpty(string);
    this.displayLength = displayLength;
    this.instantlyRepeat = instantlyRepeat;
  }

  @Override
  public void run() {
    if (!preRun()) {
      return;
    }
    if (!this.instantlyRepeat) {
      this.reset = this.position >= this.string.length();
    } else {
      this.reset = this.position >= this.string.length() - this.displayLength;
    }
    if (this.reset) {
      onReset();
      this.position = -1;
    }
    this.position++;
    afterResetRun();
    String str = string.substring(position,
                                  Math.min(position + displayLength, string.length()));
    if (!this.instantlyRepeat) {
      int aint = this.displayLength - (this.string.length() - this.position);
      if (aint > 0) {
        str += this.string.substring(0, aint);
      }
    }
    str = postRun(str);
    checkArgument(str.length() <= displayLength, "string is longer than the display length.");
    this.currentString = str;
  }

  @Override
  public StringScroller clone() {
    try {
      return ((StringScroller) super.clone());
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof StringScroller)) {
      return false;
    }
    StringScroller ss = (StringScroller) obj;
    return this.string.equals(ss.string) && this.displayLength == ss.displayLength
           && this.instantlyRepeat == ss.instantlyRepeat;
  }

  @Override
  public String toString() {
    return this.currentString;
  }

  /**
   * This method is called first in the {@link #run()}. Used to determine if the runnable should be
   * terminated or not.
   *
   * @return true to continue the runnable execution, false to terminate it
   */
  protected boolean preRun() {
    return true;
  }

  // TODO HELP I NEED A BETTER NAME BEFORE RELEASE!

  /**
   * This method is called after a possible reset in the {@link #run()}.
   */
  protected void afterResetRun() {
  }

  /**
   * This method is called after the next string has been generated, which is passed to this method.
   * Use this method to modify the final string. By default, the string that is passed is returned
   * directly, causing absolutely no change.
   *
   * @param string new generated string
   *
   * @return the new current string
   */
  protected String postRun(String string) {
    return string;
  }

  /**
   * This method is called when the position is being reset, as a result of the position being the
   * length of the string passed to the constructor.
   */
  protected void onReset() {

  }

  protected int getPositionInString(int index) {
    return index % this.string.length();
  }

  public boolean isFirstCharacter() {
    return isInstantlyRepeating()
           ? this.position == 0 // don't use reset in case this hasn't been run before.
           : getPositionInString(position + this.getDisplayLength()) == 0;
  }

  public String getFinalString() {
    return string;
  }

  public int getDisplayLength() {
    return displayLength;
  }

  public boolean isInstantlyRepeating() {
    return instantlyRepeat;
  }

  public int getPosition() {
    return position;
  }

  public boolean isReset() {
    return reset;
  }

  public String getCurrentString() {
    return currentString;
  }

  protected void setCurrentString(String currentString) {
    this.currentString = currentString;
  }
}
