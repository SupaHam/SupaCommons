package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with input and output using Bukkit. This class contains methods such
 * as {@link #resourceAsString(Plugin, String)}, and more.
 *
 * @since 0.1
 */
public class IOUtils {

  private IOUtils() {}

  /**
   * Reads a resource in a {@link Plugin}'s jar file and returns a String of it.
   *
   * @param plugin plugin to get resource from
   * @param resourceName name of the resource to read
   *
   * @return resource if found, otherwise null if it does not exist, or if an error occurred
   */
  public static String resourceAsString(@Nonnull Plugin plugin, @Nonnull String resourceName) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    Preconditions.checkNotNull(resourceName, "resource name cannot be null.");
    try (InputStream resource = plugin.getResource(resourceName)) {
      return resource == null ? null : CharStreams.toString(new InputStreamReader(resource));
    } catch (IOException e) {
      plugin.getLogger().severe("Error occurred while reading '" + resourceName + "'.");
      e.printStackTrace();
      return null;
    }
  }
}
