package com.supaham.commons.bukkit.players;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.PI;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import com.supaham.commons.utils.RandomUtils;
import com.supaham.commons.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link Player} instances. This class contains methods such as
 * {@link #dropItem(Player, ItemStack)} and more.
 *
 * @since 0.1
 */
public class Players {

  public static final float DEFAULT_FLY_SPEED = 0.1f;
  public static final float DEFAULT_WALK_SPEED = 0.2f;

  private static final Predicate<Object> IS_PLAYER = Predicates.instanceOf(Player.class);
  private static final PlayersSupplier SERVER_SUPPLIER = new ServerSupplier(Bukkit.getServer());

  public static Item dropItem(@Nonnull Player player, @Nonnull ItemStack itemStack) {
    return dropItem(player, itemStack, false);
  }

  public static Item dropItem(@Nonnull Player player, @Nonnull ItemStack itemStack, boolean death) {
    Item item = player.getWorld().dropItem(player.getLocation(), itemStack);
    dropItem(player, item, death);
    return item;
  }

  public static void dropItem(@Nonnull Player player, @Nonnull Item item) {
    dropItem(player, item, false);
  }

  public static void dropItem(@Nonnull Player player, @Nonnull Item item, boolean death) {
    Location l = player.getLocation();
    Vector v;

    Random random = RandomUtils.getRandom();
    if (death) {
      double d = random.nextDouble() * 0.5;
      double d1 = random.nextDouble() * PI * 2.0;
      v = new Vector(-Math.sin(d1) * d, 0.2, Math.cos(d1) * d);
    } else {
      double d = 0.3F;
      v = new Vector((-Math.sin(l.getYaw() / 180.0 * PI)) * Math.cos(l.getPitch() / 180.0 * PI * d),
                     -Math.sin(l.getPitch() / 180.0 * PI) * d + 0.1,
                     (Math.cos(l.getYaw() / 180.0 * PI)) * Math.cos(l.getPitch() / 180.0 * PI * d));
      double d1 = random.nextDouble() * PI * 2.0;
      d = 0.02 * random.nextDouble();
      v.setX(v.getX() + (Math.cos(d1) * d));
      v.setY(v.getY() + (random.nextDouble() - random.nextDouble()) * 0.1F);
      v.setZ(v.getZ() + (Math.sin(d1) * d));
    }
    item.setVelocity(v);
  }

  /**
   * Returns whether a {@link Player} is vanished.
   *
   * @param player player to check
   *
   * @return whether the {@code player} is vanished
   */
  public static boolean isVanished(@Nonnull Player player) {
    Preconditions.checkNotNull(player, "player cannot be null.");
    return player.hasMetadata("vanished")
           && ((Boolean) player.getMetadata("vanished").get(0).value());
  }

  /**
   * Gets a constant Predicate which returns true if the passed object is instance of {@link
   * Player}.
   *
   * @return predicate
   */
  public static Predicate<Object> isPlayer() {
    return IS_PLAYER;
  }

  private static Function<Entity, Player> entityToPlayerFunction() {
    return EntityToPlayer.INSTANCE;
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in a {@link Chunk}.
   *
   * @param chunk chunk to get entities from
   *
   * @return entities supplier
   *
   * @see #multiChunkPlayers(Collection)
   */
  public static PlayersSupplier singleChunkPlayers(@Nonnull Chunk chunk) {
    return multiChunkPlayers(chunk);
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in an array of {@link Chunk}s.
   *
   * @param chunks chunks to get entities from
   *
   * @return entities supplier
   *
   * @see #multiChunkPlayers(Collection)
   */
  public static PlayersSupplier multiChunkPlayers(@Nonnull Chunk... chunks) {
    return new ChunkSupplier(Arrays.asList(chunks));
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in a collection of {@link
   * Chunk}s.
   *
   * @param chunks chunks to get entities from
   *
   * @return entities supplier
   */
  public static PlayersSupplier multiChunkPlayers(@Nonnull Collection<Chunk> chunks) {
    return new ChunkSupplier(chunks);
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in a {@link World}.
   *
   * @param world world to get entities from
   *
   * @return entities supplier
   */
  public static PlayersSupplier worldPlayers(@Nonnull World world) {
    return new WorldSupplier(world);
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in a {@link Server}.
   *
   * @param server server to get players from
   *
   * @return entities supplier
   */
  public static PlayersSupplier serverPlayers(@Nonnull Server server) {
    return new ServerSupplier(server);
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in a {@link Bukkit#getServer()}.
   *
   * @return entities supplier
   */
  public static PlayersSupplier serverPlayers() {
    return SERVER_SUPPLIER;
  }

  /**
   * Gets a new {@link PlayerSupplier} which returns a matched player by name. This is equivalent
   * to calling {@code playerByName&#40;}{@link #serverPlayers()},{@code String&#41;}.
   *
   * @return entities supplier
   *
   * @see #playerByName(PlayersSupplier, String)
   */
  public static PlayerSupplier playerByName(@Nonnull String name) {
    return new PlayerByNameSupplier(serverPlayers(), name);
  }

  /**
   * Gets a new {@link PlayersSupplier} which returns all players in a {@link Bukkit#getServer()}.
   *
   * @return entities supplier
   */
  public static PlayerSupplier playerByName(@Nonnull PlayersSupplier supplier,
                                            @Nonnull String name) {
    return new PlayerByNameSupplier(supplier, name);
  }

  /**
   * Gets a new {@link PlayersSupplierFor} which returns all players within a radius of a
   * {@link Location}. Although this utilizes a {@link PlayersSupplier} it returns a new
   * {@link ArrayList} as opposed to a {@link Collection} of the players within range.
   * <p />
   * If the {@code supplier} is null, {@link #worldPlayers(World)} is called during each call.
   *
   * @param supplier players to check radius against, nullable
   * @param radius radius of the players to return
   *
   * @return players supplier
   */
  public static PlayersSupplierFor<Location> playersByRadius(@Nullable PlayersSupplier supplier,
                                                             double radius) {
    return new PlayersRadiusSupplier(supplier, radius);
  }

  private Players() {}

  private static class EntityToPlayer implements Function<Entity, Player> {

    private static final EntityToPlayer INSTANCE = new EntityToPlayer();

    @Nullable
    @Override
    public Player apply(Entity input) {
      if (isPlayer().apply(input)) {
        return ((Player) input);
      }
      return null;
    }
  }

  public interface PlayerSupplier extends Supplier<Player> {}

  public interface PlayersSupplier extends Supplier<Collection<Player>> {}

  /**
   * Represents a {@link Player} supplier for a specific type.
   *
   * @param <T> type used to supply the player
   */
  public interface PlayerSupplierFor<T> {

    Player get(T t);
  }

  /**
   * Represents a {@link Player} collection supplier for a specific type.
   *
   * @param <T> type used to supply the players collection
   */
  public interface PlayersSupplierFor<T> {

    Collection<? extends Player> get(T t);
  }

  private static class ChunkSupplier implements PlayersSupplier {

    private final Collection<Chunk> chunks;

    public ChunkSupplier(@Nonnull Collection<Chunk> chunks) {
      this.chunks = checkNotNull(chunks, "chunk cannot be null.");
    }

    @Override
    public Collection<Player> get() {
      ArrayList<Player> players = new ArrayList<>();
      for (Chunk chunk : this.chunks) {
        players.addAll(Collections2.transform(Arrays.asList(chunk.getEntities()),
                                              entityToPlayerFunction()));
      }
      return players;
    }
  }

  private static class WorldSupplier implements PlayersSupplier {

    private final World world;

    public WorldSupplier(World world) {
      this.world = checkNotNull(world, "world cannot be null.");
    }

    @Override
    public Collection<Player> get() {
      return Collections2.filter(
          Collections2.transform(this.world.getEntities(), entityToPlayerFunction()),
          Predicates.notNull());
    }
  }

  private static class ServerSupplier implements PlayersSupplier {

    private final Server server;

    public ServerSupplier(Server server) {
      this.server = checkNotNull(server, "server cannot be null.");
    }

    @Override
    public Collection<Player> get() {
      return ((Collection<Player>) this.server.getOnlinePlayers());
    }
  }

  private static class PlayerByNameSupplier implements PlayerSupplier {

    private final PlayersSupplier supplier;
    private final String name;

    public PlayerByNameSupplier(@Nonnull PlayersSupplier supplier, @Nonnull String name) {
      this.supplier = checkNotNull(supplier, "supplier cannot be null.");
      this.name = StringUtils.checkNotNullOrEmpty(name, "name");
    }

    @Override
    public Player get() {
      for (Player player : this.supplier.get()) {
        if (player.getName().equalsIgnoreCase(this.name)) {
          return player;
        }
      }
      return null;
    }
  }

  private static class PlayersRadiusSupplier implements PlayersSupplierFor<Location> {

    private final PlayersSupplier supplier;
    private final double radius;

    public PlayersRadiusSupplier(PlayersSupplier supplier, double radius) {
      Preconditions.checkArgument(radius >= 0, "radius cannot be smaller than 1.");
      this.supplier = supplier;
      this.radius = radius;
    }

    @Override
    public List<? extends Player> get(Location location) {
      PlayersSupplier supplier = this.supplier == null ? worldPlayers(location.getWorld())
                                                       : this.supplier;
      List<Player> players = Lists.reverse(new ArrayList<>(supplier.get()));
      for (int i = 0; i < players.size(); i++) {
        if (players.get(i).getLocation().distanceSquared(location) > radius * radius) {
          players.remove(i);
        }
      }
      return players;
    }
  }
}
