package com.supaham.commons.bukkit.worldedit;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.CommandCompleter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the names of connected {@link Player}s as suggestions.
 *
 * @since 0.1
 */
public class PlayerCommandCompleter implements CommandCompleter {

  @Override
  public List<String> getSuggestions(String arguments, CommandLocals locals)
      throws CommandException {
    String l = arguments.toLowerCase().trim();
    List<String> list = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.getName().toLowerCase().startsWith(l)) {
        list.add(player.getName());
      }
    }
    return list;
  }
}
