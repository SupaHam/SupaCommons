package com.supaham.commons.bukkit.serializers;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pluginbase.config.serializers.Serializer;

/**
 * A {@link String} {@link Serializer} that specifically serializes minecraft color codes as
 * ampersand (&) rather than section sign (&sect;).
 *
 * @since 0.1
 */
public class ColorStringSerializer implements Serializer<String> {

  @Override
  @Nullable
  public Object serialize(@Nullable final String string) {
    if (string == null) {
      return null;
    }
    return string.replaceAll(ChatColor.COLOR_CHAR + "", "&");
  }

  @Override
  @Nullable
  public String deserialize(@Nullable Object serialized, @NotNull Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    return ChatColor.translateAlternateColorCodes('&', serialized.toString());
  }
}
