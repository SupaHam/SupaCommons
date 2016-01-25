package com.supaham.commons.bukkit.commands.common;

import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;
import com.supaham.commons.bukkit.CommonPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand {

  private final CommonPlugin commonPlugin;

  public DebugCommand(CommonPlugin commonPlugin) {
    this.commonPlugin = commonPlugin;
  }

  @Command(aliases = {"commondebug"}, desc = "Sets the debug level",
      help = "[level] to set the level")
  @Require("commoncommands.debug")
  public void debug(CommandSender sender, @Optional @Range(min = 0, max = 3) Integer level) {
    if (level == null) {
      sender.sendMessage(ChatColor.YELLOW + "Debug level is set to " + commonPlugin.getLog().getDebugLevel());
      return;
    }
    commonPlugin.getLog().setDebugLevel(level);
    sender.sendMessage(ChatColor.YELLOW + "You've successfully set the debug level to " + level + ".");
    commonPlugin.saveSettings();

  }
}
