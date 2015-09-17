package com.supaham.commons.bungee;

import com.supaham.commons.bungee.modules.framework.ModuleContainer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.Nonnull;

import pluginbase.logging.LoggablePlugin;
import pluginbase.messages.LocalizablePlugin;

/**
 * Simple interface that represents a common plugin. This is used from within the whole commons
 * library.
 *
 * @since 0.3.6
 */
public interface CommonPlugin extends LocalizablePlugin, LoggablePlugin {

  /**
   * Returns the {@link Plugin} instance this {@link CommonPlugin} is extended by.
   *
   * @return bungee plugin
   */
  Plugin getBungeePlugin();

  /**
   * Returns {@link ProxyServer} instance registering this plugin.
   *
   * @return bungee proxy
   */
  ProxyServer getProxy();

  /**
   * Registers a {@link Listener} to this plugin.
   *
   * @param listener listener to register
   * @param <T> listener type
   *
   * @return registered listener (that is the given parameter)
   */
  @Nonnull <T extends Listener> T registerEvents(@Nonnull T listener);

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
  @Nonnull ModuleContainer getModuleContainer();
}
