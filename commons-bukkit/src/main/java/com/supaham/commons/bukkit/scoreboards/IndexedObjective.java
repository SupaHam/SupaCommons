package com.supaham.commons.bukkit.scoreboards;

import com.google.common.base.Preconditions;

import com.supaham.commons.utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;


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
  private final Team[] entries = new Team[16];

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

  private String indexToStringTag(int index) {
    Preconditions.checkArgument(0 <= index && index <= 15, "index out of bounds.");

    // Create a 5 'bit' (black = 0, white = 1) tag String unique to each index number that consists of no rendered characters.
    StringBuilder tag = new StringBuilder();
    for (int i =0; i < 5; i++) {
      if ((index & 0b00000001 << i) == 0) {
        tag.append(ChatColor.BLACK);
      } else {
        tag.append(ChatColor.WHITE);
      }
    }
    return tag.toString();
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
    return entries[validateIndex(index)].getPrefix();
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
      String old = (this.entries[index] != null) ? this.entries[index].getPrefix() : null;
      if (entries[index] == null) {
          this.entries[index] = scoreboard.getBukkitScoreboard().registerNewTeam(String.format("%d", index));
          entries[index].addEntry(indexToStringTag(index));
          bukkitObjective.getScore(indexToStringTag(index)).setScore(sanitizeIndex(index));
      }

      this.entries[index].setPrefix(string);
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
    String old = (entries[validateIndex(index)] != null) ? entries[validateIndex(index)].getPrefix() : null;
    entries[validateIndex(index)].unregister();
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
