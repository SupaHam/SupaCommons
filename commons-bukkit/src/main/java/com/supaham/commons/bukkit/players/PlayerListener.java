package com.supaham.commons.bukkit.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @since 0.1
 */
class PlayerListener implements Listener {

  private final PlayerManager manager;

  PlayerListener(PlayerManager playerManager) {
    this.manager = playerManager;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(final PlayerJoinEvent event) {
    manager.handleJoin(event.getPlayer(), EventPriority.LOWEST);
  }

  /*
   * This listener's task is to trigger the player quitting but not removing the instance.
   * Instance is removed in the HIGHEST priority listener of PlayerQuitEvent.
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    manager.handleQuit(event.getPlayer(), EventPriority.LOWEST);
  }

  /*
   * This listener's task is to clear the player reference at the latest time possible.
   * This is to allow other classes to be able to access the reference whilst the player is
   * quitting.
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void removeReference(PlayerQuitEvent event) {
    manager.handleQuit(event.getPlayer(), EventPriority.MONITOR);
  }
}
