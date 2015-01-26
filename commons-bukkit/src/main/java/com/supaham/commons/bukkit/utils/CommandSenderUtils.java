package com.supaham.commons.bukkit.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Utility methods for working with {@link CommandSender} instances. This class contains methods 
 * such as {@link #getName(CommandSender)}, and more.
 *
 * @since 0.1
 */
public final class CommandSenderUtils {

  private CommandSenderUtils() {}

  /**
   * Gets the name of a {@link CommandSender}. If the sender is a {@link Player}, their display
   * name is returned. If the the sender is the {@link ConsoleCommandSender}, "CONSOLE" is
   * returned. Otherwise, {@link CommandSender#getName()} is returned.
   *
   * @param sender sender to get name for
   *
   * @return {@code sender}'s name
   */
  public static String getName(CommandSender sender) {
    if (sender instanceof Player) {
      return ((Player) sender).getDisplayName();
    } else if (sender instanceof ConsoleCommandSender) {
      return "CONSOLE";
    }
    return sender.getName();
  }
}
