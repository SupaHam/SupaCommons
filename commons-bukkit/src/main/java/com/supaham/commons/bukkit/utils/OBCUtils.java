package com.supaham.commons.bukkit.utils;

import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import org.bukkit.Sound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility methods for working with {@code org.bukkit.craftbukkit}. This class contains methods such
 * as {@link #getSound(Sound)}, and more.
 *
 * @since 0.1
 */
public class OBCUtils {

  private static Method getSoundMethod = null;

  static {
    try {
      Class<?> clazz = PackageType.CRAFTBUKKIT.getClass("CraftSound");
      getSoundMethod = clazz.getDeclaredMethod("getSound", Sound.class);
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the string name of a {@link Sound}.
   *
   * @param sound sound to get name for
   *
   * @return name of the {@code sound}
   */
  public static String getSound(final Sound sound) {
    try {
      return (String) getSoundMethod.invoke(null, sound);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }
}
