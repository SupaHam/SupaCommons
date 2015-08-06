package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.utils.StringUtils;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.bukkit.config.YamlConfiguration;
import pluginbase.messages.PluginBaseException;

/**
 * Utility methods for working with PluginBase serialization. This class contains methods such as
 * {@link #loadOrCreateProperties(CommonPlugin, File, Object, String)}, and more.
 *
 * @since 0.3.2
 */
public final class SerializationUtils {

  /**
   * Loads or creates a default properties class built on PluginBase. If the {@code file} does not
   * exist, it is created. Then, when loading, comments option is set to true, causing comment
   * output in the {@code file}. Then, the file is searched for {@code root} yaml object, if it
   * does not exist, it is written to using the {@code defaults}. Finally, the yaml writes the
   * result, whether it be the defaults or the newly loaded class, to the config and writes to the
   * {@code file}.
   *
   * @param plugin plugin to debug to
   * @param file file to load or create
   * @param defaults defaults to use
   * @param root root yaml object in the file, not null
   * @param <T> type of properties
   *
   * @return object of type {@link T}
   */
  @Nullable
  public static <T> T loadOrCreateProperties(@Nonnull CommonPlugin plugin, @Nonnull File file,
                                             @Nonnull T defaults, @Nullable String root) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    Preconditions.checkNotNull(file, "file cannot be null.");
    Preconditions.checkNotNull(defaults, "defaults cannot be null.");
    if (StringUtils.trimToNull(root) == null) {
      root = "settings";
    }
    T result = defaults;
    try {
      YamlConfiguration yaml = YamlConfiguration.loadYamlConfig(file);
      yaml.options().comments(true);
      if (yaml.contains(root)) {
        result = yaml.getToObject(root, result);
      }
      yaml.set(root, result);
      yaml.save(file);
      plugin.getLog().fine("Successfully loaded " + file.getName() + ".");
    } catch (PluginBaseException e) {
      plugin.getLog().severe("Error occurred while loading " + file.getName() + "!");
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      plugin.getLog().severe("Error occurred while saving " + file.getName() + "!");
      e.printStackTrace();
      return null;
    }
    return result;
  }

  private SerializationUtils() {
    throw new AssertionError("Try Weetabix instead...");
  }
}
