package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.PI;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;

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
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link Player} instances. This class contains methods such as
 * {@link #dropItem(Player, ItemStack)} and more.
 *
 * @since 0.1
 */
public class PlayerUtils {

  private static final Predicate<Object> IS_PLAYER = Predicates.instanceOf(Player.class);
  private static final PlayersSupplier SERVER_SUPPLIER = new ServerSupplier(null);

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

  private PlayerUtils() {}

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

  public static interface PlayerSupplier extends Supplier<Player> {}

  public static interface PlayersSupplier extends Supplier<Collection<? extends Player>> {}

  private static class ChunkSupplier implements PlayersSupplier {

    private final Collection<Chunk> chunks;

    public ChunkSupplier(@Nonnull Collection<Chunk> chunks) {
      this.chunks = checkNotNull(chunks, "chunk cannot be null.");
    }

    @Override
    public Collection<? extends Player> get() {
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
    public Collection<? extends Player> get() {
      return Collections2.transform(this.world.getEntities(), entityToPlayerFunction());
    }
  }

  private static class ServerSupplier implements PlayersSupplier {

    private final Server server;

    public ServerSupplier(Server server) {
      this.server = checkNotNull(server, "server cannot be null.");
    }

    @Override
    public Collection<? extends Player> get() {
      return this.server.getOnlinePlayers();
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
        if(player.getName().equalsIgnoreCase(this.name)) {
          return player;
        }
      }
      return null;
    }
  }
}
