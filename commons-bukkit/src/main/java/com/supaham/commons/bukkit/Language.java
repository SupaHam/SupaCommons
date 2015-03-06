package com.supaham.commons.bukkit;

import com.supaham.commons.bukkit.language.Message;
import com.supaham.commons.bukkit.language.MessageManager;
import com.supaham.commons.bukkit.language.Theme;

import org.bukkit.ChatColor;

public class Language {

  protected static MessageManager manager;

  static {
    manager = new MessageManager();
    manager.addTheme(new Theme('+', ChatColor.YELLOW.toString()));
    manager.addTheme(new Theme('-', ChatColor.RED.toString()));
    manager.addTheme(new Theme('v', ChatColor.BLUE.toString()));
    manager.addTheme(new Theme('!', ChatColor.BOLD.toString()));
    manager.addTheme(new Theme('_', ChatColor.UNDERLINE.toString()));
    manager.addTheme(new Theme('G', ChatColor.DARK_GREEN.toString()));
    manager.addTheme(new Theme('=', ChatColor.STRIKETHROUGH.toString()));
    manager.addTheme(new Theme('i', ChatColor.DARK_GRAY.toString()));
  }

  protected Language() {}

  protected static Message m(String node, String message) {
    Message m = new Message(manager, node, message);
    manager.addMessage(m);
    return m;
  }

  public static final class World {

    private World() {}

    public static final Message NOT_FOUND =
        m("world.not_found", "$-'$v%1$%$-' is not a valid world.");
  }

  public static final class LocationChecker {

    public static final Message OOB_WARN =
        m("out_of_bounds.warn",
          "$-You are out of bounds! You've got $v%1$s$- seconds before you die."),
        OOB_RETURNED = m("out_of_bounds.returned", "$+You are now back in bounds.");
  }

  public static final class WorldEdit {

    private WorldEdit() {}

    public static final Message SELECTION_NOT_SUPPORTED =
        m("plugins.worldedit.selection_not_supported",
          "$v%1$%$- selection is not supported.");
  }
}
