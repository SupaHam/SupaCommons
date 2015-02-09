package com.supaham.commons.bukkit.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import org.bukkit.Bukkit;

/**
 * Utility methods for working with the {@link WorldEditPlugin} dependency. This class contains 
 * methods such as {@link #get()}.
 *
 * @since 0.1
 */
public class WorldEditUtils {

  /**
   * Gets the instance of {@link WorldEditPlugin}.
   *
   * @return the plugin instance if it is loaded, otherwise null
   */
  public static WorldEditPlugin get() {
    try {
      Class.forName("com.sk89q.worldedit.bukkit.WorldEditPlugin");
      return ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"));
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
