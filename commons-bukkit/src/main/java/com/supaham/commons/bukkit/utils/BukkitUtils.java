package com.supaham.commons.bukkit.utils;

import com.supaham.commons.CMain;

import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Utility methods for working with the {@code org.bukkit} package. This class contains methods 
 * such as {@link #getChatColorIntCode(ChatColor)}, and more.
 *
 * @since 0.2
 */
public class BukkitUtils {

  private static final Map<ChatColor, Integer> chatColorIntCodes = new HashMap<>();

  static {
    try {
      Field field = ChatColor.class.getDeclaredField("intCode");
      field.setAccessible(true);
      for (ChatColor color : ChatColor.values()) {
        chatColorIntCodes.put(color, (int) field.get(color));
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      CMain.getLogger().log(Level.SEVERE, "intCode field not found", e);
    }
  }

  /**
   * Gets the privatized intCode value belonging to a {@link ChatColor}. This accesses a private
   * map in this class, which is populated during static initialization.
   *
   * @param chatColor chat color to get intcode for
   *
   * @return int code, as of 1.8, 0 - 15
   */
  public static int getChatColorIntCode(ChatColor chatColor) {
    return chatColorIntCodes.get(chatColor);
  }
}
