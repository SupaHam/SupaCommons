package com.supaham.commons.bukkit.commands.common;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.bukkit.commands.utils.CommonCommandData;
import com.supaham.commons.bukkit.commands.utils.CommonCommandData.Builder;

import java.util.Arrays;

/**
 * Represents a class with common commands, such as debug. 
 */
public enum CommonCommands {

  DEBUG("%sdebug") {
    @Override public Builder builder(CommonPlugin commonPlugin) {
      return CommonCommandData.builder()
          .manager(commonPlugin.getCommandsManager())
          .classIntance(new DebugCommand(commonPlugin));
    }
  };

  private final String[] aliases;

  CommonCommands(String... aliases) {
    this.aliases = aliases;
  }

  public abstract Builder builder(CommonPlugin commonPlugin);

  public Builder builder(CommonPlugin commonPlugin, String commandPrefix) {
    return builder(commonPlugin).aliases(Arrays.asList(getAliases(commandPrefix)));
  }

  public String[] getAliases(String prefix) {
    if (this.aliases == null) {
      return null;
    }

    String[] aliases = new String[this.aliases.length];
    for (int i = 0; i < this.aliases.length; i++) {
      aliases[i] = String.format(this.aliases[i], prefix);
    }
    return aliases;
  }
}
