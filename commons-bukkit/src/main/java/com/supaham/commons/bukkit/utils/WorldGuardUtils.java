package com.supaham.commons.bukkit.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * Utility methods for working with the {@link WorldGuardPlugin} dependency. This class contains 
 * methods such as {@link #get()}.
 *
 * @since 0.1
 */
public class WorldGuardUtils {

  /**
   * Gets the instance of {@link WorldGuardPlugin}.
   *
   * @return the plugin instance if it is loaded, otherwise null
   */
  public static WorldGuardPlugin get() {
    try {
      Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
      return WorldGuardPlugin.inst();
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
