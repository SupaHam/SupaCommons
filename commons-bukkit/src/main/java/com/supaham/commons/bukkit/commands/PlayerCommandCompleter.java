package com.supaham.commons.bukkit.commands;


import com.sk89q.intake.CommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.completion.CommandCompleter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the names of connected {@link Player}s as suggestions.
 *
 * @since 0.1
 */
public class PlayerCommandCompleter implements CommandCompleter {

  @Override
  public List<String> getSuggestions(String arguments, Namespace namespace)
      throws CommandException {
    String l = arguments.toLowerCase().trim();
    return Bukkit.getOnlinePlayers().stream()
        .filter(player -> player.getName().toLowerCase().startsWith(l)).map(Player::getName)
        .collect(Collectors.toList());
  }
}
