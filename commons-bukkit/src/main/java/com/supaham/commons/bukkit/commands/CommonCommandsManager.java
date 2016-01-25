package com.supaham.commons.bukkit.commands;

import com.google.common.base.Preconditions;

import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.fluent.DispatcherNode;
import com.supaham.commons.bukkit.CommonPlugin;

import javax.annotation.Nonnull;

import lc.vq.exhaust.bukkit.command.CommandManager;

/**
 * Handles the registration and invocation of commands.
 *
 * @since 0.1
 */
public class CommonCommandsManager extends CommandManager {

  private final CommonPlugin plugin;

  public CommonCommandsManager(@Nonnull CommonPlugin plugin) {
    super(plugin);
    this.plugin = plugin;
    this.builder.addExceptionConverter(new CommonExceptionConverter(plugin));
    this.injector.install(new CommonProviders());
    Preconditions.checkNotNull(this.dispatcher, "dispatcher cannot be null.");
  }

  public CommonPlugin getPlugin() {
    return plugin;
  }
}
