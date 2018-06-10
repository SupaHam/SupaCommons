package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.Enums;

import net.kyori.text.format.TextColor;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * A {@link String} {@link Serializer} that specifically serializes minecraft color codes as
 * ampersand (&) rather than section sign (&sect;).
 *
 * @since 0.1
 */
public class TextColorSerializer implements Serializer<TextColor> {

  @Override
  @Nullable
  public Object serialize(@Nullable final TextColor textColor, @Nonnull SerializerSet serializerSet) {
    if (textColor == null) {
      return null;
    }
    return textColor.toString();
  }

  @Override
  @Nullable
  public TextColor deserialize(@Nullable Object serialized, @NotNull Class wantedType,
                            @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    String str = serialized.toString().toLowerCase();
    for (TextColor textColor : TextColor.values()) {
      if (textColor.toString().toLowerCase().equals(str)) {
        return textColor;
      }
    }
    return Enums.findFuzzyByValue(TextColor.class, str);
  }
}
