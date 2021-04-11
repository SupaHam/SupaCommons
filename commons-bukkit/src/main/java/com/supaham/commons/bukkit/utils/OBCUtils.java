package com.supaham.commons.bukkit.utils;

import com.supaham.commons.CMain;
import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import org.bukkit.Color;
import org.bukkit.Sound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nullable;

/**
 * Utility methods for working with {@code org.bukkit.craftbukkit}. This class contains methods
 * such
 * as {@link #getSound(Sound)}, and more.
 *
 * @since 0.1
 */
public class OBCUtils {

  private static Map<String, Color> colorsByFieldName = new HashMap<>(17);

  static {
    try {
      colorsByFieldName.put("white", Color.WHITE);
      colorsByFieldName.put("silver", Color.SILVER);
      colorsByFieldName.put("gray", Color.GRAY);
      colorsByFieldName.put("black", Color.BLACK);
      colorsByFieldName.put("red", Color.RED);
      colorsByFieldName.put("maroon", Color.MAROON);
      colorsByFieldName.put("yellow", Color.YELLOW);
      colorsByFieldName.put("olive", Color.OLIVE);
      colorsByFieldName.put("lime", Color.LIME);
      colorsByFieldName.put("green", Color.GREEN);
      colorsByFieldName.put("aqua", Color.AQUA);
      colorsByFieldName.put("teal", Color.TEAL);
      colorsByFieldName.put("blue", Color.BLUE);
      colorsByFieldName.put("navy", Color.NAVY);
      colorsByFieldName.put("fuchsia", Color.FUCHSIA);
      colorsByFieldName.put("purple", Color.PURPLE);
      colorsByFieldName.put("orange", Color.ORANGE);
    } catch (Exception e) {
      CMain.getLogger().log(Level.SEVERE, "Error occurred whilst getting colors by field names", e);
    }
  }

  /**
   * Gets the string name of a {@link Sound}.
   *
   * @param sound sound to get name for
   *
   * @return name of the {@code sound}
   * @deprecated backwards-compatible usage for {@link Sound#getKey()}
   */
  public static String getSound(final Sound sound) {
    return sound.getKey().getKey();
  }

  /**
   * Gets a {@link Color} by name. The Color class does not offer a way to use their preset values,
   * so this method does.
   *
   * @param string name of the color preset to get
   *
   * @return {@link Color} preset, nullable
   */
  @Nullable
  public static Color getColorByName(String string) {
    return colorsByFieldName.get(string.toLowerCase());
  }
}
