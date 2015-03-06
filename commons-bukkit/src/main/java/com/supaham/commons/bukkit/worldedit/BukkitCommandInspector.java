package com.supaham.commons.bukkit.worldedit;

import com.google.common.base.Preconditions;

import com.sk89q.bukkit.util.CommandInspector;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.supaham.commons.bukkit.CommonPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * @since 0.1
 */
class BukkitCommandInspector implements CommandInspector {

  private final CommonPlugin plugin;
  private final Dispatcher dispatcher;

  public BukkitCommandInspector(@Nonnull CommonPlugin plugin, @Nonnull Dispatcher dispatcher) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    Preconditions.checkNotNull(dispatcher, "dispatcher cannot be null.");
    this.plugin = plugin;
    this.dispatcher = dispatcher;
  }

  @Override
  public String getShortText(Command command) {
    CommandMapping mapping = dispatcher.get(command.getName());
    if (mapping != null) {
      return mapping.getDescription().getShortDescription();
    } else {
      plugin.getLog()
          .warning("BukkitCommandInspector doesn't know how about the command '" + command + "'");
      return "Help text not available";
    }
  }

  @Override
  public String getFullText(Command command) {
    CommandMapping mapping = dispatcher.get(command.getName());
    if (mapping != null) {
      Description description = mapping.getDescription();
      return "Usage: " + description.getUsage() 
             + (description.getHelp() != null ? "\n" + description.getHelp() : "");
    } else {
      plugin.getLog()
          .warning("BukkitCommandInspector doesn't know about the command '" + command + "'");
      return "Help text not available";
    }
  }

  @Override
  public boolean testPermission(CommandSender sender, Command command) {
    CommandMapping mapping = dispatcher.get(command.getName());
    if (mapping != null) {
      CommandLocals locals = new CommandLocals();
      locals.put(CommandSender.class, sender);
      return mapping.getCallable().testPermission(locals);
    } else {
      plugin.getLog()
          .warning("BukkitCommandInspector doesn't know about the command '" + command + "'");
      return false;
    }
  }
}
