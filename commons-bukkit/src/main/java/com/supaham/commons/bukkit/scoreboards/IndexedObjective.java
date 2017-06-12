package com.supaham.commons.bukkit.scoreboards;

import com.google.common.base.Preconditions;

import com.supaham.commons.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Represents an indexed {@link Objective}, providing the ability to display entries in a specific
 * order. This feature utilizes the entry's score in the scoreboard, making it impossible to
 * utilize from this API (or any for that matter).
 *
 * @since 0.2.5
 */
public class IndexedObjective {

  public static final int MAX_ENTRY_LENGTH = 40;

  private CommonScoreboard scoreboard;
  private Objective bukkitObjective;
  private final String[] entries = new String[16];

  /**
   * Constructs a new indexed objective. This operation only sets the display slot. The name of the
   * objective is the given id.
   *
   * @param scoreboard scoreboard this objective should belong to
   * @param id id of the objective
   */
  public IndexedObjective(@Nonnull CommonScoreboard scoreboard, @Nonnull String id) {
    this.scoreboard = Preconditions.checkNotNull(scoreboard, "scoreboard cannot be null.");
    StringUtils.checkNotNullOrEmpty(id, "id");
    this.bukkitObjective = scoreboard.getBukkitScoreboard().registerNewObjective(id, "dummy");
    this.bukkitObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
  }

  private int validateIndex(int index) {
    if (index < 0 || index > 15) {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    return index;
  }

  protected int sanitizeIndex(int index) {
    return 15 - index;
  }

  /**
   * Returns whether this indexed objective contains an index.
   *
   * @param index inclusive index of the entry to check
   *
   * @return whether this objective contains the index
   */
  public boolean contains(int index) {
    return entries[validateIndex(index)] != null;
  }

  /**
   * Gets an entry by index (inclusive).
   *
   * @param index inclusive index of the entry to get
   *
   * @return entry by index
   */
  public String getEntry(int index) {
    return entries[validateIndex(index)];
  }

  /**
   * Inserts an entry to this objective. The returned string is the previous entry text that was
   * in place of the given index, that has been replaced with the given entry text.
   *
   * @param index index of the new entry
   * @param string entry text
   *
   * @return previous entry text this insertion operation replaced
   */
  public String put(int index, String string) {
    String old = entries[validateIndex(index)];
    if (old != null) {
      this.scoreboard.getBukkitScoreboard().resetScores(old);
    }

    // Minecraft scoreboards only allow unique entries. When the current string already exists in the scoreboard, it
    // is replaced and added with the new score (below in setScore).
    // To get around this we append RESET codes to the string until it becomes unique. There is one problem with
    // this: if the string exceeds maximum length, then the entry cannot be inserted and an error will occur.
    Set<String> existingBukkitEntries = this.scoreboard.getBukkitScoreboard().getEntries();
    if (existingBukkitEntries.contains(string)) {
      String uniqueString = string;
      boolean append = true; // Used to change where the RESET codes are inserted for more unique values.
      while (existingBukkitEntries.contains(uniqueString)) {
        if (append) {
          uniqueString += ChatColor.RESET;
        } else { // prepend
          uniqueString = ChatColor.RESET + uniqueString;
        }
        if (uniqueString.length() > MAX_ENTRY_LENGTH) {
          append = false;
          uniqueString = string;
        }
      }
      string = uniqueString;
    }
    this.bukkitObjective.getScore(string).setScore(sanitizeIndex(index));
    this.entries[index] = string;
    return old;
  }

  /**
   * Removes an entry by index (inclusive).
   *
   * @param index inclusive index of the entry to remove
   *
   * @return the removed entry
   */
  public String remove(int index) {
    String old = entries[validateIndex(index)];
    if (old != null) {
      this.scoreboard.getBukkitScoreboard().resetScores(old);
    }
    entries[index] = null;
    return old;
  }

  /**
   * Gets the {@link CommonScoreboard} owner of this objective.
   *
   * @return common scoreboard
   */
  public CommonScoreboard getScoreboard() {
    return scoreboard;
  }

  /**
   * Gets the bukkit {@link Objective} that this indexed objective is utilizing.
   *
   * @return bukkit objective
   */
  public Objective getBukkitObjective() {
    return bukkitObjective;
  }
}
