package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Entity} instances. This class contains methods such as
 * {@link #getFreeLocation(LivingEntity)}, and more.
 *
 * @since 0.1
 */
public class EntityUtils {

  /**
   * Returns a free safe location above or at the given {@link LivingEntity}'s location.
   *
   * @param entity entity to get free space for
   *
   * @return a location with the required free space
   *
   * @see LocationUtils#getFreeLocation(Location, double)
   */
  public static Location getFreeLocation(@Nonnull LivingEntity entity) {
    checkNotNull(entity, "entity cannot be null.");
    return LocationUtils.getFreeLocation(entity.getLocation(), entity.getEyeHeight(true));
  }
  
  /* ================================
   * >> GUAVA implementations
   * ================================ */

  /**
   * Gets a new {@link EntitiesSupplier} which returns all entities in a {@link Chunk}.
   *
   * @param chunk chunk to get entities from
   *
   * @return entities supplier
   *
   * @see #multiChunkEntities(Collection)
   */
  public static EntitiesSupplier singleChunkEntities(@Nonnull Chunk chunk) {
    return multiChunkEntities(chunk);
  }

  /**
   * Gets a new {@link EntitiesSupplier} which returns all entities in an array of {@link Chunk}s.
   *
   * @param chunks chunks to get entities from
   *
   * @return entities supplier
   *
   * @see #multiChunkEntities(Collection)
   */
  public static EntitiesSupplier multiChunkEntities(@Nonnull Chunk... chunks) {
    return new ChunkEntitiesSupplier(Arrays.asList(chunks));
  }

  /**
   * Gets a new {@link EntitiesSupplier} which returns all entities in a collection of {@link
   * Chunk}s.
   *
   * @param chunks chunks to get entities from
   *
   * @return entities supplier
   */
  public static EntitiesSupplier multiChunkEntities(@Nonnull Collection<Chunk> chunks) {
    return new ChunkEntitiesSupplier(chunks);
  }

  /**
   * Gets a new {@link EntitiesSupplier} which returns all entities in a {@link World}.
   *
   * @param world world to get entities from
   *
   * @return entities supplier
   */
  public static EntitiesSupplier worldEntities(@Nonnull World world) {
    return new WorldEntitiesSupplier(world);
  }

  public static interface EntitySupplier extends Supplier<Entity> {}

  public static interface EntitiesSupplier extends Supplier<Collection<Entity>> {}

  private static class ChunkEntitiesSupplier implements EntitiesSupplier {

    private final Collection<Chunk> chunks;

    public ChunkEntitiesSupplier(Collection<Chunk> chunks) {
      this.chunks = checkNotNull(chunks, "chunk cannot be null.");
    }

    @Override
    public Collection<Entity> get() {
      ArrayList<Entity> entities = new ArrayList<>();
      for (Chunk chunk : chunks) {
        entities.addAll(Arrays.asList(chunk.getEntities()));
      }
      return entities;
    }
  }

  private static class WorldEntitiesSupplier implements EntitiesSupplier {

    private final World world;

    public WorldEntitiesSupplier(World world) {
      this.world = checkNotNull(world, "world cannot be null.");
    }

    @Override
    public Collection<Entity> get() {
      return world.getEntities();
    }
  }
}
