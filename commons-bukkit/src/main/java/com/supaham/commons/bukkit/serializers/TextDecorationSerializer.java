package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.Enums;

import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

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
public class TextDecorationSerializer implements Serializer<TextDecoration> {

  @Override
  @Nullable
  public Object serialize(@Nullable final TextDecoration textColor, @Nonnull SerializerSet serializerSet) {
    if (textColor == null) {
      return null;
    }
    return textColor.toString();
  }

  @Override
  @Nullable
  public TextDecoration deserialize(@Nullable Object serialized, @NotNull Class wantedType,
                            @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    String str = serialized.toString().toLowerCase();
    for (TextDecoration textDecoration : TextDecoration.values()) {
      if (textDecoration.toString().toLowerCase().equals(str)) {
        return textDecoration;
      }
    }
    return Enums.findFuzzyByValue(TextDecoration.class, str);
  }
}
