package com.supaham.commons.bukkit.players;

import static com.google.common.base.Preconditions.checkNotNull;

import com.supaham.commons.bukkit.CommonPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

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
 * Represents a player manager class for {@link CPlayer} instances. This manager includes methods
 * such as {@link #createPlayer(Player)} and {@link #removePlayer(UUID)} that contains all the
 * boilerplate code. This manager also registers a package-private {@link Listener} that handles all
 * the {@link CPlayer} instances, such as when a player joins or leaves the server.
 *
 * @param <T> a class that extends {@link CPlayer}
 *
 * @since 0.1
 */
public class PlayerManager<T extends CPlayer> {

  private final CommonPlugin plugin;
  private final Class<T> playerClass;
  private final PlayerListener listener;
  private final Map<UUID, T> players = new HashMap<>();
  private final Map<String, UUID> namesToUUID = new HashMap<>();

  /**
   * Converts a {@link Collection} of {@link CPlayer}s into a {@link List} of {@link Player}s.
   *
   * @param cPlayers players list to convert.
   *
   * @return the new {@link List} of {@link Player}s.
   */
  public static <T extends CPlayer> List<Player> commonPlayersToPlayers(@Nonnull Collection<T> cPlayers) {
    List<Player> list = new ArrayList<>();
    for (T cPlayer : cPlayers) {
      if (cPlayer.getPlayer() != null) {
        list.add(cPlayer.getPlayer());
      }
    }
    return list;
  }

  public PlayerManager(@Nonnull CommonPlugin plugin, @Nonnull Class<T> playerClass) {
    checkNotNull(plugin, "plugin cannot be null.");
    checkNotNull(playerClass, "player class cannot be null.");
    this.plugin = plugin;
    this.playerClass = playerClass;
    plugin.registerEvents(this.listener = new PlayerListener(this));
  }
  
  protected void unload() {
    HandlerList.unregisterAll(this.listener);
  }
  
  protected T handleJoin(@Nonnull Player player, @Nonnull EventPriority priority) {
    return createPlayer(player);
  }
  
  protected void handleQuit(@Nonnull Player player, @Nonnull EventPriority priority) {
    if(priority.equals(EventPriority.MONITOR)) {
      T removed = removePlayer(player.getUniqueId());
      if (removed != null) {
        // Set status here in case any code needs to check if a player's online.
        // This is as late as we can set it, so accessors have a lot of time to do their checks.
        removed.status = PlayerStatus.OFFLINE;
      }
    } else {
      CPlayer cPlayer = getPlayer(player);
      if (cPlayer != null) {
        cPlayer.disconnect();
      }
    }
  }

  /**
   * Creates a new {@link CPlayer} out of a {@link Player}.
   *
   * @param player player to own the new {@link CPlayer} instance
   *
   * @return newly created instance of {@link CPlayer}
   *
   * @throws PlayerCreationException thrown if constructor invocation failed
   */
  public T createPlayer(Player player) throws PlayerCreationException {
    T cPlayer = getPlayer(player);
    if (cPlayer == null) {
      try {
        Constructor<T> ctor = this.playerClass.getConstructor(PlayerManager.class, Player.class);
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

  /**
   * Gets a {@link CPlayer} from a {@link CommandSender}, assuming its a {@link Player}.
   *
   * @param sender sender to get {@link CPlayer} instance for
   *
   * @return the {@link CPlayer} instance if the {@code sender} is a {@link Player} and found,
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
   * Gets a {@link CPlayer} from a {@link UUID} represented as a {@link String}.
   *
   * @param name name of the player to get
   *
   * @return {@link CPlayer} if found, otherwise null.
   *
   * @see #getPlayer(UUID)
   */
  @Nullable
  public T getPlayer(@Nonnull String name) {
    return getPlayer(namesToUUID.get(name.toLowerCase()));
  }

  /**
   * Gets a {@link CPlayer} from a {@link UUID}.
   *
   * @param uuid uuid to get {@link CPlayer} for
   *
   * @return {@link CPlayer} if found, otherwise null
   */
  @Nullable
  public T getPlayer(@Nullable UUID uuid) {
    return uuid == null ? null : this.players.get(uuid);
  }

  private void addPlayer(@Nonnull T cPlayer) {
    this.players.put(cPlayer.getUuid(), cPlayer);
    this.namesToUUID.put(cPlayer.getName().toLowerCase(), cPlayer.getUuid());
  }

  /**
   * Removes a {@link CPlayer} from this {@link PlayerManager} by {@link UUID}.
   *
   * @param uuid uuid that owns the {@link CPlayer} instance
   *
   * @return removed player instance
   */
  @Nullable
  protected T removePlayer(@Nonnull UUID uuid) {
    T removed = this.players.remove(uuid);
    if (removed != null) {
      this.namesToUUID.remove(removed.getName().toLowerCase());
    }
    return removed;
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
}
