package com.supaham.commons.bukkit;

import com.supaham.commons.StringScroller;

import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Represents an extension of {@link StringScroller} that supports minecraft colors.
 *
 * @see StringScroller
 * @since 0.1
 */
public class ColorfulStringScroller extends StringScroller {

  private final Set<ChatColor> lastColors = new HashSet<>(6);

  public ColorfulStringScroller(@Nonnull String string, int displayLength) {
    super(string, displayLength);
  }

  public ColorfulStringScroller(@Nonnull String string, int displayLength,
                                boolean instantlyRepeat) {
    super(string, displayLength, instantlyRepeat);
  }

  @Override
  protected void afterResetRun() {
    if (this.position > 0
        && getFinalString().charAt(this.position - 1) == ChatColor.COLOR_CHAR
        && ChatColor.getByChar(getFinalString().charAt(this.position)) != null) {
      this.position++;
    }
  }

  @Override
  protected String postRun(String string) {
    if (isFirstCharacter()) { // We're back at the start of the string, clear colors.
      this.lastColors.clear();
    } else {
      if (string.charAt(0) == ChatColor.COLOR_CHAR) {
        ChatColor color = ChatColor.getByChar(string.charAt(1));
        if (color != null) {
          if (color.isColor()
              || color.equals(ChatColor.RESET)) { // colors overwrite everything visually
            this.lastColors.clear();
          }
          if (!color.equals(ChatColor.RESET) && !this.lastColors.contains(color)) {
            this.lastColors.add(color);
          }
        }
      }
    }
    string = string.substring(0, string.length() - (this.lastColors.size() * 2));
    if (string.charAt(string.length() - 1) == ChatColor.COLOR_CHAR) {
      string = string.substring(0, string.length() - 1);
    }
    if (!this.lastColors.isEmpty()) {
      String prefix = "";
      for (ChatColor lastColor : lastColors) {
        prefix += lastColor;
      }
      string = prefix + string;
    }
    return string;
  }
}
