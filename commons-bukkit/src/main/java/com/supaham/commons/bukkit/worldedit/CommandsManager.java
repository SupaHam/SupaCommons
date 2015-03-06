package com.supaham.commons.bukkit.worldedit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;

import com.sk89q.bukkit.util.CommandInfo;
import com.sk89q.bukkit.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.util.command.InvalidUsageException;
import com.sk89q.worldedit.util.command.parametric.LegacyCommandsHandler;
import com.sk89q.worldedit.util.command.parametric.ParametricBuilder;
import com.sk89q.worldedit.util.formatting.ColorCodeBuilder;
import com.sk89q.worldedit.util.formatting.component.CommandUsageBox;
import com.supaham.commons.bukkit.CommonPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Handles the registration and invocation of commands.
 *
 * @since 0.1
 */
public abstract class CommandsManager {

  private final CommonPlugin plugin;
  private final Dispatcher dispatcher;
  private CommandRegistration dynamicCommands;

  protected static ParametricBuilder getDefaultParametricBuilder(@Nonnull CommonPlugin plugin) {
    ParametricBuilder builder = new ParametricBuilder();
    builder.setAuthorizer(new CommandAuthorizer());
    builder.setDefaultCompleter(new PlayerCommandCompleter());
    builder.addBinding(new CBinding(plugin));
    builder.addExceptionConverter(new CommonExceptionConverter(plugin));
    builder.addInvokeListener(new LegacyCommandsHandler());
    return builder;
  }

  public CommandsManager(@Nonnull CommonPlugin plugin) {
    this.plugin = plugin;
    dynamicCommands = new CommandRegistration(plugin);

    this.dispatcher = addCommands();
    checkNotNull(this.dispatcher, "dispatcher cannot be null.");
  }

  @Nonnull
  protected abstract Dispatcher addCommands();

  /**
   * This method is called when this {@link CommandsManager} is ready to add local objects before
   * executing.
   *
   * @param sender command sender
   * @param arguments arguments the {@code sender} provided
   */
  public void addLocals(CommandSender sender, String arguments, @Nonnull CommandLocals locals) {
  }

  public void handleCommand(CommandSender sender, String arguments) {
    String[] split = arguments.split(" ");

    // No command found!
    if (!dispatcher.contains(split[0])) {
      return;
    }

    CommandLocals locals = new CommandLocals();

    boolean isPlayer = sender instanceof Player;
    String RED = isPlayer ? ChatColor.RED.toString() : "";

    locals.put(CommandSender.class, sender);
    if (isPlayer) {
      locals.put(Player.class, sender);
    }
    addLocals(sender, arguments, locals);

    try {
      dispatcher.call(Joiner.on(" ").join(split), locals, new String[0]);
    } catch (CommandPermissionsException e) {
      sender.sendMessage(RED + "You don't have permission to do this.");
    } catch (InvalidUsageException e) {
      if (e.isFullHelpSuggested()) {
        sender.sendMessage(ColorCodeBuilder.asColorCodes(
            new CommandUsageBox(e.getCommand(), e.getCommandUsed("/", ""), locals)));
        String message = e.getMessage();
        if (message != null) {
          sender.sendMessage(RED + message);
        }
      } else {
        String message = e.getMessage();
        sender.sendMessage(RED + (message != null ? message :
                                  "The command was not used properly (no more help available)."));
        sender.sendMessage(RED + "Usage: " + e.getSimpleUsageString("/"));
      }
    } catch (WrappedCommandException e) {
      Throwable t = e.getCause();
      sender.sendMessage(
          RED + "An unexpected error has occurred, please report this to a staff member.");
      sender.sendMessage(RED + t.getMessage());
      plugin.getLog().severe("An unexpected error while handling a CoreCubed command: ");
      t.printStackTrace();
    } catch (CommandException e) {
      String message = e.getMessage();
      if (message != null) {
        sender.sendMessage(RED + e.getMessage());
      } else {
        if (isPlayer) {
          sender.sendMessage(RED + "An unknown error has occurred! Please report this to a staff " +
                             "member immediately.");
        }
        plugin.getLog().severe("An unknown error occurred: ");
        e.printStackTrace();
      }
    }
  }

  public List<String> handleCommandSuggestion(CommandSender sender, String arguments) {
    try {
      CommandLocals locals = new CommandLocals();
      locals.put(CommandSender.class, sender);
      if (sender instanceof Player) {
        locals.put(Player.class, sender);
      }
      return dispatcher.getSuggestions(arguments, locals);
    } catch (CommandException e) {
      sender.sendMessage(((sender instanceof Player) ? ChatColor.RED : "") + e.getMessage());
      return Collections.emptyList();
    }
  }

  public void registerCommands() {
    List<CommandInfo> toRegister = new ArrayList<>();
    BukkitCommandInspector inspector = new BukkitCommandInspector(plugin, dispatcher);

    for (CommandMapping command : dispatcher.getCommands()) {
      Description description = command.getDescription();
      List<String> permissions = description.getPermissions();
      String[] permissionsArray = new String[permissions.size()];
      permissions.toArray(permissionsArray);

      toRegister.add(new CommandInfo(description.getUsage(), description.getShortDescription(),
                                     command.getAllAliases(), inspector, permissionsArray));
    }

    dynamicCommands.register(toRegister);
  }

  public CommonPlugin getPlugin() {
    return plugin;
  }

  public Dispatcher getDispatcher() {
    return dispatcher;
  }

  public CommandRegistration getDynamicCommands() {
    return dynamicCommands;
  }
}
