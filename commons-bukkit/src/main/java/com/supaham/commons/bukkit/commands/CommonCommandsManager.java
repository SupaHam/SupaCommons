package com.supaham.commons.bukkit.commands;

import com.google.common.base.Preconditions;

import com.sk89q.intake.parametric.ParametricException;
import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.bukkit.commands.utils.CommandUtils;
import com.supaham.commons.bukkit.commands.utils.CommonCommandData;

import net.ellune.exhaust.bukkit.command.CommandManager;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;

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

  public void registerMethod(CommonCommandData.Builder dataBuilder) throws ParametricException {
    dataBuilder.manager(this);
    CommandUtils.registerMethod(dataBuilder.build());
  }
}
