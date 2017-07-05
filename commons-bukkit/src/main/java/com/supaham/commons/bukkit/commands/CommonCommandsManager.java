package com.supaham.commons.bukkit.commands;

import com.supaham.commons.bukkit.CommonPlugin;

import javax.annotation.Nonnull;

import co.aikar.commands.BukkitCommandManager;

/**
 * Handles the registration and invocation of commands.
 *
 * @since 0.1
 */
public class CommonCommandsManager extends BukkitCommandManager {

  private final CommonPlugin plugin;

  public CommonCommandsManager(@Nonnull CommonPlugin plugin) {
    super(plugin);
    this.plugin = plugin;

    CommonProviders.registerAll(getCommandContexts());
    setDefaultExceptionHandler(new CommonExceptionHandler(plugin));
  }

  public CommonPlugin getPlugin() {
    return plugin;
  }
}
