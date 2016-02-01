package com.supaham.commons.bukkit.players;

import static com.google.common.base.Preconditions.checkNotNull;

import com.supaham.commons.Uuidable;
import com.supaham.commons.bukkit.CommonPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a player manager class for {@link CommonPlayer} instances. This manager includes methods
 * such as {@link #createPlayer(Player)} and {@link #removePlayer(UUID)} that contains all the
 * boilerplate code. This manager also registers a package-private {@link Listener} that handles all
 * the {@link CommonPlayer} instances, such as when a player joins or leaves the server.
 *
 * @param <T> a class that extends {@link CommonPlayer}
 *
 * @since 0.1
 */
public class BukkitPlayerManager<T extends CommonPlayer> {

  private final CommonPlugin plugin;
  private final Class<T> playerClass;
  private final PlayerListener listener;
  private final Map<UUID, T> players = new HashMap<>();
  private final Map<String, UUID> namesToUUID = new HashMap<>();

  /**
   * Converts a {@link Collection} of {@link CommonPlayer}s into a {@link List} of {@link Player}s.
   *
   * @param cPlayers players list to convert.
   *
   * @return the new {@link List} of {@link Player}s.
   */
  public static <T extends CommonPlayer> List<Player> commonPlayersToPlayers(@Nonnull Collection<T> cPlayers) {
    List<Player> list = new ArrayList<>();
    for (T cPlayer : cPlayers) {
      if (cPlayer.getPlayer() != null) {
        list.add(cPlayer.getPlayer());
      }
    }
    return list;
  }

  public BukkitPlayerManager(@Nonnull CommonPlugin plugin, @Nonnull Class<T> playerClass) {
    checkNotNull(plugin, "plugin cannot be null.");
    checkNotNull(playerClass, "player class cannot be null.");
    this.plugin = plugin;
    this.playerClass = playerClass;
    plugin.registerEvents(this.listener = new PlayerListener());
  }
  
  public void unload() {
    HandlerList.unregisterAll(this.listener);
  }
  
  /**
   * Creates a new {@link CommonPlayer} out of a {@link Player}.
   *
   * @param player player to own the new {@link CommonPlayer} instance
   *
   * @return newly created instance of {@link CommonPlayer}
   *
   * @throws PlayerCreationException thrown if constructor invocation failed
   */
  protected T createPlayer(Player player) throws PlayerCreationException {
    T cPlayer = getPlayer(player);
    if (cPlayer == null) {
      try {
        Constructor<T> ctor = this.playerClass.getConstructor(BukkitPlayerManager.class, Player.class);
        cPlayer = ctor.newInstance(this, player);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException
          | InstantiationException e) {
        e.printStackTrace();
        throw (PlayerCreationException) new PlayerCreationException(player).initCause(e);
      }
      addPlayer(cPlayer);
    }
    cPlayer.join();
    return cPlayer;
  }

  protected void addPlayer(@Nonnull T cPlayer) {
    this.players.put(cPlayer.getUuid(), cPlayer);
    this.namesToUUID.put(cPlayer.getName().toLowerCase(), cPlayer.getUuid());
  }

  /**
   * Removes a {@link CommonPlayer} from this {@link BukkitPlayerManager} by {@link UUID}.
   *
   * @param uuid uuid that owns the {@link CommonPlayer} instance
   *
   * @return removed player instance
   */
  @Nullable
  public T removePlayer(@Nonnull UUID uuid) {
    T removed = this.players.remove(uuid);
    if (removed != null) {
      this.namesToUUID.remove(removed.getName().toLowerCase());
    }
    return removed;
  }

  /**
   * Gets a {@link CommonPlayer} from a {@link CommandSender}, assuming its a {@link Player}.
   *
   * @param sender sender to get {@link CommonPlayer} instance for
   *
   * @return the {@link CommonPlayer} instance if the {@code sender} is a {@link Player} and found,
   * otherwise null.
   *
   * @see #getPlayer(UUID)
   */
  @Nullable
  public T getPlayer(@Nonnull CommandSender sender) {
    if (!(sender instanceof Player)) {
      return null;
    }
    return getPlayer(((Player) sender).getUniqueId());
  }

  /**
   * Gets a {@link CommonPlayer} from a {@link UUID} represented as a {@link String}.
   *
   * @param name name of the player to get
   *
   * @return {@link CommonPlayer} if found, otherwise null.
   *
   * @see #getPlayer(UUID)
   */
  @Nullable
  public T getPlayer(@Nonnull String name) {
    return getPlayer(namesToUUID.get(name.toLowerCase()));
  }

  /**
   * Gets a {@link CommonPlayer} from a {@link UUID}.
   *
   * @param uuidable uuidable object to get {@link CommonPlayer} for
   *
   * @return {@link CommonPlayer} if found, otherwise null
   */
  @Nullable
  public T getPlayer(@Nullable Uuidable uuidable) {
    return uuidable == null ? null : getPlayer(uuidable.getUuid());
  }

  /**
   * Gets a {@link CommonPlayer} from a {@link UUID}.
   *
   * @param uuid uuid to get {@link CommonPlayer} for
   *
   * @return {@link CommonPlayer} if found, otherwise null
   */
  @Nullable
  public T getPlayer(@Nullable UUID uuid) {
    return uuid == null ? null : this.players.get(uuid);
  }

  public CommonPlugin getPlugin() {
    return this.plugin;
  }

  public Map<UUID, T> getPlayers() {
    return this.players;
  }

  public Map<String, UUID> getNamesToUUID() {
    return this.namesToUUID;
  }

  /**
   * @since 0.2.7
   */
  class PlayerListener implements Listener {
  
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
      createPlayer(event.getPlayer());
    }
  
    /*
     * This listener's task is to trigger the player quitting but not removing the instance.
     * Instance is removed in the HIGHEST priority listener of PlayerQuitEvent.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
      CommonPlayer commonPlayer = getPlayer(event.getPlayer());
      if (commonPlayer != null) {
        commonPlayer.disconnect();
      }
    }

    /*
     * This listener's task is to clear the player reference at the latest time possible.
     * This is to allow other classes to be able to access the reference whilst the player is
     * quitting.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void removeReference(PlayerQuitEvent event) {
      CommonPlayer removed = removePlayer(event.getPlayer().getUniqueId());
      if (removed != null) {
        // Set status here in case any code needs to check if a player's online.
        // This is as late as we can set it, so accessors have a lot of time to do their checks.
        removed.setStatus(PlayerStatus.OFFLINE);
      }
    }
  }
}
