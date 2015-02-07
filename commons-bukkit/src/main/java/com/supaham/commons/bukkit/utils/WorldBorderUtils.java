package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.WorldBorder;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with the {@link WorldBorder} dependency. This class contains methods
 * such as {@link #get()}.
 *
 * @since 0.1
 */
public class WorldBorderUtils {

  /**
   * Gets the instance of {@link WorldBorder}.
   *
   * @return the plugin instance if it is loaded, otherwise null
   */
  public static WorldBorder get() {
    try {
      Class.forName("com.wimbli.WorldBorder.WorldBorder");
      return WorldBorder.plugin;
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  public static BorderData getWorldBorder(@Nonnull World world) {
    checkNotNull(world, "world cannot be null.");
    WorldBorder worldBorder = get();
    return worldBorder != null ? worldBorder.getWorldBorder(world.getName()) : null;
  }

  public static Location getRandomLocation(@Nonnull World world) {
    BorderData bd = getWorldBorder(world);
    return bd != null ? getRandomLocation(world, bd) : null;
  }

  public static Location getRandomLocation(@Nonnull World world, @Nonnull BorderData bd) {
    checkNotNull(world, "world cannot be null.");
    checkNotNull(bd, "border data cannot be null.");

    Block block;
    do {
      int x = (int) Math.round(bd.getX() - bd.getRadiusX() + 2.0D
                                                             * Math.random() * bd.getRadiusX());
      int z = (int) Math.round(bd.getZ() - bd.getRadiusZ() + 2.0D
                                                             * Math.random() * bd.getRadiusZ());
      block = world.getHighestBlockAt(x, z);
    } while (!bd.insideBorder(block.getLocation()));
    return block.getLocation();
  }
}
