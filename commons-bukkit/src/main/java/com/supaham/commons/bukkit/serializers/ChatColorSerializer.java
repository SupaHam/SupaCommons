package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.Enums;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

import javax.annotation.Nonnull;

/**
 * A {@link String} {@link Serializer} that specifically serializes minecraft color codes as
 * ampersand (&) rather than section sign (&sect;).
 *
 * @since 0.1
 */
public class ChatColorSerializer implements Serializer<ChatColor> {

  @Override
  @Nullable
  public Object serialize(@Nullable final ChatColor textColor, @Nonnull SerializerSet serializerSet) {
    if (textColor == null) {
      return null;
    }
    return textColor.toString();
  }

  @Override
  @Nullable
  public ChatColor deserialize(@Nullable Object serialized, @NotNull Class wantedType,
                            @Nonnull SerializerSet serializerSet)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    String str = serialized.toString().toLowerCase();
    for (ChatColor textColor : ChatColor.values()) {
      if (textColor.toString().toLowerCase().equals(str)) {
        return textColor;
      }
    }
    return ChatColor.of(str);
  }
}
