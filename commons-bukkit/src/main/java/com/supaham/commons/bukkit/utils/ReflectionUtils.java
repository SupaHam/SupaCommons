package com.supaham.commons.bukkit.utils;

import org.bukkit.Bukkit;

/**
 * Utility methods for working with {@code java.reflect}. This class contains methods such as
 * {@link #getServerVersion()}, and more, as well as the {@link PackageType} class for version packages such {@code nms} and {@code obc}.
 *
 * @since 0.1
 */
public class ReflectionUtils {

  public static String getServerVersion() {
    return Bukkit.getServer().getClass().getPackage().getName().substring(23);
  }

  /**
   * An enumeration with versioned packages such as {@code nms} and {@code obc}.
   */
  public enum PackageType {
    /**
     * {@code net.minecraft.server.server_version} package.
     */
    MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
    /**
     * {@code org.bukkit.craftbukkit.server_version} package.
     */
    CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),;

    private final String path;

    private PackageType(String path) {
      this.path = path;
    }

    @Override
    public String toString() {
      return this.path;
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
      return Class.forName(this + "." + className);
    }

    public String getPath() {
      return path;
    }
  }
}
