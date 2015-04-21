package com.supaham.commons.bukkit.listeners;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import com.supaham.commons.bukkit.players.Players;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class of useful {@link Listener}s of events based on players such as {@link PlayerJoinEvent}.
 */
public class PlayerListeners {

  /**
   * Registers a new {@link Listener} to a {@link Plugin} that defaults all joining players' walk
   * and flight speed.
   *
   * @param plugin plugin to own the listener
   */
  public static Listener defaultSpeeds(@Nonnull Plugin plugin) {
    return defaultSpeeds(plugin, null);
  }

  /**
   * Registers a new {@link Listener} to a {@link Plugin} that defaults a joining player's walk and
   * flight speed. A {@link Predicate} may be passed, otherwise null. If a predicate is passed, it
   * will be tested (calling {@link Predicate#apply(Object)} with the joining player object, if it
   * returns true it will set the defaults, otherwise it will ignore the joining player. Otherwise,
   * if null is passed for the predicate, the defaults are always applied to the player.
   *
   * @param plugin plugin to own the listener
   * @param predicate predicate to test the player
   */
  public static Listener defaultSpeeds(@Nonnull Plugin plugin,
                                       @Nullable final Predicate<Player> predicate) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    DefaultSpeed listener = predicate == null ? DefaultSpeed.INSTANCE : new DefaultSpeed(predicate);
    plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    return listener;
  }

  private static final class DefaultSpeed implements Listener {

    private static final DefaultSpeed INSTANCE = new DefaultSpeed(null);

    private final Predicate<Player> predicate;

    public DefaultSpeed(Predicate<Player> predicate) {
      this.predicate = predicate;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
      if (predicate == null || predicate.apply(event.getPlayer())) {
        if (event.getPlayer().getWalkSpeed() != Players.DEFAULT_WALK_SPEED) {
          event.getPlayer().setWalkSpeed(Players.DEFAULT_WALK_SPEED);
        }
        if (event.getPlayer().getFlySpeed() != Players.DEFAULT_FLY_SPEED) {
          event.getPlayer().setFlySpeed(Players.DEFAULT_FLY_SPEED);
        }
      }
    }
  }
}
