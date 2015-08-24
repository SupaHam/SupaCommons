package com.supaham.commons.bukkit;

import com.supaham.commons.bukkit.modules.ModuleContainer;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

import pluginbase.messages.LocalizablePlugin;

/**
 * Simple interface that represents a common plugin. This is used from within the whole commons
 * library.
 *
 * @since 0.1
 */
public interface CommonPlugin extends LocalizablePlugin, Plugin {

  /**
   * Registers a {@link Listener} to this plugin.
   *
   * @param listener listener to register
   * @param <T> listener type
   *
   * @return registered listener (that is the given parameter)
   */
  @Nonnull
  <T extends Listener> T registerEvents(@Nonnull T listener);
  
  /**
   * Unregisters a {@link Listener} to this plugin.
   *
   * @param listener listener to unregister
   * @param <T> listener type
   *
   * @return unregistered listener (that is the given parameter)
   */
  @Nonnull <T extends Listener> T unregisterEvents(@Nonnull T listener);

  /**
   * Returns this plugin's {@link ModuleContainer}.
   *
   * @return module container instance
   */
  @Nonnull
  ModuleContainer getModuleContainer();
}
