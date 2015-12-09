package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.items.ItemEnchantment;
import com.supaham.commons.serializers.ListSerializer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * Contains Bukkit {@link Serializer} classes such as {@link ListColorStringSerializer}, {@link
 * ListLocationSerializer}, and more.
 *
 * @since 0.1
 */
public class CBSerializers {

  /**
   * Serializes the section sign character produced by {@link ChatColor} as an ampersand {@code &}.
   */
  public static class FriendlyChatColor implements Serializer<ChatColor> {

    @Nullable @Override public Object serialize(ChatColor object,
                                                @Nonnull SerializerSet serializerSet) {
      return object == null ? null : "&" + object.getChar();
    }

    @Nullable @Override
    public ChatColor deserialize(@Nullable Object serialized, @Nonnull Class wantedType,
                                 @Nonnull SerializerSet serializerSet) {
      if (serialized == null) {
        return null;
      }
      return ChatColor.getByChar(serialized.toString().replace("&", ""));
    }
  }
  
  /* ================================
   * >> List serializers
   * ================================ */

  public static class ListColorStringSerializer extends ListSerializer<String> {

    @Override public Class<String> getTypeClass() {
      return String.class;
    }
  }

  public static class ListComplexItemStackSerializer extends ListSerializer<ItemStack> {

    @Override public Class<ItemStack> getTypeClass() {
      return ItemStack.class;
    }
  }

  public static class ItemEnchantment extends ListSerializer<ItemEnchantment> {

    @Override public Class<ItemEnchantment> getTypeClass() {
      return ItemEnchantment.class;
    }
  }

  public static class ListLocationSerializer extends ListSerializer<Location> {

    @Override public Class<Location> getTypeClass() {
      return Location.class;
    }
  }

  public static class ListMaterialDataSerializer extends ListSerializer<MaterialData> {

    @Override public Class<MaterialData> getTypeClass() {
      return MaterialData.class;
    }
  }

  public static class ListVectorSerializer extends ListSerializer<Vector> {

    @Override public Class<Vector> getTypeClass() {
      return Vector.class;
    }
  }
}
