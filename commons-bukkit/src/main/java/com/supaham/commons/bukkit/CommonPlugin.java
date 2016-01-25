package com.supaham.commons.bukkit;

import com.supaham.commons.bukkit.commands.CommonCommandsManager;
import com.supaham.commons.bukkit.modules.ModuleContainer;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

import javax.annotation.Nonnull;

import pluginbase.logging.LoggablePlugin;
import pluginbase.messages.LocalizablePlugin;

/**
 * Simple interface that represents a common plugin. This is used from within the whole commons
 * library.
 *
 * @since 0.1
 */
public interface CommonPlugin extends LocalizablePlugin, LoggablePlugin, Plugin {

  /**
   * Registers a {@link Listener} to this plugin.
   *
   * @param listener listener to register
   * @param <T> listener type
   *
   * @return registered listener (that is the given parameter)
   */
  @Nonnull
  default <T extends Listener> T registerEvents(@Nonnull T listener) {
    getServer().getPluginManager().registerEvents(listener, this);
    return listener;
  }

  /**
   * Unregisters a {@link Listener} to this plugin.
   *
   * @param listener listener to unregister
   * @param <T> listener type
   *
   * @return unregistered listener (that is the given parameter)
   */
  @Nonnull
  default <T extends Listener> T unregisterEvents(@Nonnull T listener) {

    HandlerList.unregisterAll(listener);
    return listener;
  }

  /**
   * Returns this plugin's {@link ModuleContainer}.
   *
   * @return module container instance
   */
  @Nonnull ModuleContainer getModuleContainer();

  /**
   * Returns this plugin's {@link CommonCommandsManager}. This may be a subclass of {@link CommonCommandsManager} if
   * the plugin has set it so.
   *
   * @return CommonCmmandsManager
   */
  @Nonnull CommonCommandsManager getCommandsManager();

  /**
   * Returns this plugin's {@link CommonSettings}.
   *
   * @return settings
   */
  CommonSettings getSettings();

  @Nonnull
  File getSettingsFile();

  boolean reloadSettings();

  boolean saveSettings();

  boolean isFirstRun();
}
