package com.supaham.commons.bukkit.players;

/**
 * Represents a player status for {@link CPlayer}.
 * <p />
 * This Enumeration has been sorted in the order OF states the player can be in since creation;
 * that is:
 * <ol>
 *   <li><b>OFFLINE</b></li>
 *   <li><b>CONNECTING</b></li>
 *   <li><b>ONLINE</b></li>
 *   <li><b>DISCONNECTING</b></li>
 * </ol>
 */
public enum PlayerStatus {
  /**
   * An idle state, where there is absolutely no connection to a player.
   */
  OFFLINE,
  /**
   * A connecting state, where the player is attempting to connect to the server.
   */
  CONNECTING,
  /**
   * An online state, where the player is online and well on the server.
   */
  ONLINE,
  /**
   * A disconnecting state, where the player is disconnecting from the server.
   */
  DISCONNECTING;
}
