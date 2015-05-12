package com.supaham.commons.bukkit.scoreboards;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Represents a scoreboard of the SupaCommons library, providing convenience when interacting with
 * scoreboards.
 */
public class CommonScoreboard {

  private final Scoreboard bukkitScoreboard;
  private final Set<Player> viewers = new HashSet<>();

  public CommonScoreboard() {
    this(Bukkit.getScoreboardManager().getNewScoreboard());
  }

  public CommonScoreboard(@Nonnull Scoreboard bukkitScoreboard) {
    Preconditions.checkNotNull(bukkitScoreboard, "scoreboard cannot be null.");
    this.bukkitScoreboard = bukkitScoreboard;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{"
           + "viewers=" + this.viewers.toString()
           + "}";
  }

  /**
   * Clears this scoreboard's viewers.
   */
  public void clear() {
    Iterator<Player> it = this.viewers.iterator();
    while (it.hasNext()) {
      resetScoreboard(it.next());
      it.remove();
    }
  }

  /**
   * Adds a {@link Player} to the this scoreboard as a viewer.
   *
   * @param player player to add
   *
   * @return whether the player was already added, this would be false if the player is already a
   * viewer
   */
  public boolean addViewer(Player player) {
    boolean added = this.viewers.add(player);
    if (added) {
      player.setScoreboard(this.bukkitScoreboard);
    }
    return added;
  }

  /**
   * Removes a {@link Player} viewer from this scoreboard.
   *
   * @param player player to remove
   *
   * @return whether the player was removed, false if the player was not already viewing this
   * scoreboard
   */
  public boolean removeViewer(Player player) {
    resetScoreboard(player);
    return this.viewers.remove(player);
  }

  protected boolean resetScoreboard(Player player) {
    if (player.getScoreboard().equals(this.bukkitScoreboard)) {
      player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
      return true;
    }
    return false;
  }

  /**
   * Gets the {@link Scoreboard} instance.
   *
   * @return scoreboard instance
   */
  public Scoreboard getBukkitScoreboard() {
    return bukkitScoreboard;
  }

  /**
   * Gets an unmodifiable collection of the {@link Player} viewers of this scoreboard.
   *
   * @return collection of viewers
   */
  public Collection<Player> getViewers() {
    return Collections.unmodifiableCollection(viewers);
  }
}
