package com.supaham.commons.bukkit.players;

import org.bukkit.entity.Player;

/**
 * Represents an exception that is thrown when a {@link CPlayer} instance creation has failed, as a
 * result of the {@link PlayerManager#createPlayer(Player)} call.
 *
 * @since 0.1
 */
public class PlayerCreationException extends RuntimeException {

  public PlayerCreationException(Player player) {
    super("Failed to create player object for player " + player.getName() + ".");
  }
}
