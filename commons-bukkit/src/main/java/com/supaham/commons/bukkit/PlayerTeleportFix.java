// Released under the BSD License.
// Kristian S. Stangeland 2013.
package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.PlayerUtils;
import com.supaham.commons.bukkit.utils.PlayerUtils.PlayersSupplierFor;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * This class fixes player entity mispositioning, whether it be a block off, or completely
 * invisible, by hiding the entity from the online players and back again.
 *
 * @see #PlayerTeleportFix(Plugin)
 * @see #PlayerTeleportFix(Plugin, long)
 * @see #PlayerTeleportFix(Plugin, PlayersSupplierFor)
 * @see #PlayerTeleportFix(Plugin, long, PlayersSupplierFor)
 * @since 0.2
 */
public class PlayerTeleportFix implements Listener {

  public static final long DEFAULT_TASK_DELAY = 10;
  private final Plugin plugin;
  private final long taskDelay;
  private final PlayersSupplierFor<Player> supplier;

  private boolean enabled;

  private static PlayersSupplierFor<Player> getPlayerTrackingRange(Plugin plugin) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    final Server server = plugin.getServer();
    // TODO support spigot
    final double radius = server.getViewDistance() * 16;

    // The following is a playersByRadius wrapper that passes the given player's location
    return new PlayersSupplierFor<Player>() {
      PlayersSupplierFor<Location> delegate = PlayerUtils.playersByRadius(null, radius);

      @Override
      public Collection<? extends Player> get(Player player) {
        return delegate.get(player.getLocation());
      }
    };
  }

  /**
   * Constructs a new instance with the given {@link Plugin} and {@link #DEFAULT_TASK_DELAY} as the
   * task delay.
   *
   * @param plugin plugin to own this instance
   *
   * @see #PlayerTeleportFix(Plugin, long)
   */
  public PlayerTeleportFix(@Nonnull Plugin plugin) {
    this(plugin, DEFAULT_TASK_DELAY);
  }

  /**
   * Constructs a new instance with the given {@link Plugin} and task delay.
   *
   * @param plugin plugin to own this instance
   * @param taskDelay delay of the fix AFTER a player teleports
   *
   * @see #PlayerTeleportFix(Plugin, long, PlayersSupplierFor)
   */
  public PlayerTeleportFix(@Nonnull Plugin plugin, long taskDelay) {
    this(plugin, taskDelay, getPlayerTrackingRange(plugin));
  }

  /**
   * Constructs a new instance with the given {@link Plugin} and {@link PlayersSupplierFor}, and
   * {@link #DEFAULT_TASK_DELAY} as the task delay.
   *
   * @param plugin plugin to own this instance
   * @param supplier players supplier
   *
   * @see #PlayerTeleportFix(Plugin, long, PlayersSupplierFor)
   */
  public PlayerTeleportFix(@Nonnull Plugin plugin, @Nonnull PlayersSupplierFor<Player> supplier) {
    this(plugin, DEFAULT_TASK_DELAY, supplier);
  }

  /**
   * Constructs a new instance with the given {@link Plugin}, task delay, and
   * {@link PlayersSupplierFor}.
   *
   * @param plugin plugin to own this instance
   * @param taskDelay delay of the fix AFTER a player teleports
   * @param supplier players supplier
   */
  public PlayerTeleportFix(@Nonnull Plugin plugin, long taskDelay,
                           @Nonnull PlayersSupplierFor<Player> supplier) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    Preconditions.checkNotNull(supplier, "supplier cannot be null.");
    this.plugin = plugin;
    this.taskDelay = taskDelay;
    this.supplier = supplier;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  protected void onPlayerTeleport(final PlayerTeleportEvent event) {
    if (!isEnabled() || event.getFrom().equals(event.getPlayer().getLocation())) {
      return;
    }
    new BukkitRunnable() {
      @Override
      public void run() {
        final Collection<? extends Player> players = supplier.get(event.getPlayer());
        updateEntities(players, false); // hide all
        new BukkitRunnable() {
          @Override
          public void run() {
            updateEntities(players, true); // show all
          }
        }.runTaskLater(plugin, 1);
      }
    }.runTaskLater(plugin, taskDelay);
  }

  private void updateEntities(Collection<? extends Player> players, boolean visible) {
    // Hide every player
    for (Player observer : players) {
      for (Player player : players) {
        if (observer.getEntityId() != player.getEntityId()) {
          if (visible) {
            observer.showPlayer(player);
          } else {
            observer.hidePlayer(player);
          }
        }
      }
    }
  }

  public Plugin getPlugin() {
    return plugin;
  }

  public PlayersSupplierFor<Player> getSupplier() {
    return supplier;
  }

  public long getTaskDelay() {
    return taskDelay;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
