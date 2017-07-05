package com.supaham.commons.bukkit.commands;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.exceptions.CommonException;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.ExceptionHandler;
import co.aikar.commands.RegisteredCommand;

public class CommonExceptionHandler implements ExceptionHandler {

  private static final Pattern numberFormat = Pattern.compile("^For input string: \"(.*)\"$");

  private final CommonPlugin plugin;

  public CommonExceptionHandler(@Nonnull CommonPlugin plugin) {
    this.plugin = plugin;
  }

  @Override public boolean execute(BaseCommand command, RegisteredCommand registeredCommand, CommandIssuer sender,
                                   List<String> args, Throwable t) {
    if (t instanceof NumberFormatException) {
      final Matcher matcher = numberFormat.matcher(t.getMessage());
      if (matcher.matches()) {
        sender.sendMessage(ChatColor.RED + "Number expected; string \"" + matcher.group(1) + "\" given.");
        return true;
      } else {
        sender.sendMessage(ChatColor.RED + "Number expected; string given.");
        return true;
      }
    } else if (t instanceof CommonException) {
      sender.sendMessage(ChatColor.RED + t.getMessage());
      t.printStackTrace();
      return true;
    }
    return false;
  }
}
