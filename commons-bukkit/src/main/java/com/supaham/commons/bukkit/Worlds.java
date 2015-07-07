package com.supaham.commons.bukkit;

import com.supaham.commons.bukkit.utils.EntityUtils.EntitySupplier;

import org.bukkit.World;
import org.bukkit.entity.Entity;

/**
 * Utility methods for working with {@link World} instances. This class contains methods such as
 * {@link #getEntityById(World, int)} and more.
 *
 * @since 0.2.7
 */
public class Worlds {

  public static EntitySupplier getEntityById(World world, int entityId) {
    return new EntityIdSupplier(world, entityId);
  }

  private Worlds() {
    throw new AssertionError("YOOOOOU SHALL NOT PASSSSSSSSSS!");
  }

  private static class EntityIdSupplier implements EntitySupplier {

    private final World world;
    private final int entityId;

    public EntityIdSupplier(World world, int entityId) {
      this.world = world;
      this.entityId = entityId;
    }

    @Override public Entity get() {
      for (Entity entity : world.getEntities()) {
        if (entity.getEntityId() == this.entityId) {
          return entity;
        }
      }
      return null;
    }
  }
}
