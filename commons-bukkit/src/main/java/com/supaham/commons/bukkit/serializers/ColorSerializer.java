package com.supaham.commons.bukkit.serializers;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.OBCUtils;
import com.supaham.commons.serializers.ListSerializer;
import com.supaham.commons.utils.NumberUtils;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * Represents a Bukkit {@link Color} serializer. This serializer supports four methods of
 * deserialization:
 * <ol>
 * <li>RGB as a single integer. 123456</li>
 * <li>Hexadecimal color e.g. #FFFFFF</li>
 * <li>R,G,B e.g. 255,255,255</li>
 * <li>Using Bukkit's ENUM color names (case-insensitive) e.g. Black, YELLOW, red</li>
 * </ol>
 */
public class ColorSerializer implements Serializer<Color> {

  @Nullable @Override
  public Object serialize(@Nullable Color color, @NotNull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (color == null) {
      return null;
    }
    return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
  }

  @Nullable @Override public Color deserialize(@Nullable Object o, @NotNull Class aClass,
                                               @NotNull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (o == null) {
      return null;
    }

    // There are three cases of deserialization below:
    // #1 RGB as a single integer.
    // #2 Hexadecimal color
    // #3 R,G,B
    // #4 Using Bukkit's ENUM color names.

    if (o instanceof Integer) { // #1
      return Color.fromRGB((Integer) o);
    } else if (o instanceof String) {
      String rawInput = (String) o;
      final String[] split = rawInput.split(",");
      if (split.length == 1) {
        String input = split[0];
        if (input.startsWith("#")) { // #2
          Preconditions.checkArgument(input.length() == 7, // account for the #
                                      "Hexadecimal color must be 6 characters.");
          return Color.fromRGB(Integer.valueOf(input.substring(1, 3), 16),
                               Integer.valueOf(input.substring(3, 5), 16),
                               Integer.valueOf(input.substring(5, 7), 16));
        } else if (NumberUtils.isInteger(input)) { // #1
          return Color.fromRGB(Integer.parseInt(input));
        } else { // #4
          return OBCUtils.getColorByName(input);
        }
      } else if (split.length == 3) { // #3
        final int red = NumberUtils.isInteger(split[0]) ? Integer.parseInt(split[0]) : 0;
        final int green = NumberUtils.isInteger(split[1]) ? Integer.parseInt(split[1]) : 0;
        final int blue = NumberUtils.isInteger(split[2]) ? Integer.parseInt(split[2]) : 0;
        return Color.fromRGB(red, green, blue);
      } else {
        throw new IllegalStateException("R,G,B color must have only 2 commas at most.");
      }
    }
    throw new IllegalStateException("Uhmm, not sure how we got here.");
  }

  public static class ListColorSerializer extends ListSerializer<Color> {

    @Override
    public Class<Color> getTypeClass() {
      return Color.class;
    }
  }
}
