package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Represents a sound that is playable to a {@link World} or {@link Player}s.
 *
 * @since 0.1
 */
public abstract class Sound {

  /**
   * Plays this sound in a {@link World} at each players' location.
   *
   * @param world world to play this sound in
   *
   * @see #play(Collection)
   */
  public void play(@Nonnull World world) {
    checkNotNull(world, "world cannot be null.");
    play(world.getPlayers());
  }

  /**
   * Plays this sound in a {@link World} at a {@link Location}.
   *
   * @param world world to play this sound in
   * @param location location to play this sound at
   *
   * @see #play(Collection, Location)
   */
  public void play(@Nonnull World world, @Nonnull Location location) {
    checkNotNull(world, "world cannot be null.");
    checkNotNull(location, "location cannot be null.");
    play(world.getPlayers(), location);
  }

  /**
   * Plays this sound to a {@link Collection} of {@link Player}s at their {@link Location}.
   *
   * @param players players to play this sound for
   *
   * @see #play(Player)
   */
  public void play(@Nonnull Collection<Player> players) {
    checkNotNull(players, "players collection cannot be null.");
    for (Player player : players) {
      play(player);
    }
  }

  /**
   * Plays this sound to a {@link Collection} of {@link Player}s.
   *
   * @param players players to play this sound for
   * @param location location to play this sound at
   *
   * @see #play(Player, Location)
   */
  public void play(@Nonnull Collection<Player> players, @Nonnull Location location) {
    checkNotNull(players, "players collection cannot be null.");
    checkNotNull(location, "location cannot be null.");
    for (Player player : players) {
      play(player, location);
    }
  }

  /**
   * Plays this sound to a {@link Player} at their location.
   *
   * @param player player to play this sound for
   *
   * @see #play(Player, Location)
   */
  public void play(@Nonnull Player player) {
    checkNotNull(player, "player cannot be null.");
    play(player, player.getLocation());
  }

  /**
   * Plays this sound to a {@link Player} at a {@link Location}.
   *
   * @param player player to play this sound for
   * @param location location to play this sound at
   */
  public abstract void play(@Nonnull Player player, @Nonnull Location location);
}
