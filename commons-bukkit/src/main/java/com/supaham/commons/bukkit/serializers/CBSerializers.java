package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.area.CuboidRegion;
import com.supaham.commons.bukkit.area.Poly2DRegion;
import com.supaham.commons.bukkit.items.ItemEnchantment;
import com.supaham.commons.bukkit.potion.Potion;
import com.supaham.commons.serializers.ListSerializer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.SerializationRegistrar;
import pluginbase.config.serializers.Serializer;

/**
 * Contains Bukkit {@link Serializer} classes such as {@link ListColorStringSerializer}, {@link
 * ListLocationSerializer}, and more.
 *
 * @since 0.1
 */
public class CBSerializers {

  static {
    SerializationRegistrar.registerClass(ColorStringSerializer.class);
    SerializationRegistrar.registerClass(ComplexItemStackSerializer.class);
    SerializationRegistrar.registerClass(ItemEnchantmentSerializer.class);
    SerializationRegistrar.registerClass(LocationSerializer.class);
    SerializationRegistrar.registerClass(MaterialDataSerializer.class);
    SerializationRegistrar.registerClass(VectorSerializer.class);

    SerializationRegistrar.registerClass(FriendlyChatColor.class);

    SerializationRegistrar.registerClass(ListColorStringSerializer.class);
    SerializationRegistrar.registerClass(ListComplexItemStackSerializer.class);
    SerializationRegistrar.registerClass(ListItemEnchantmentSerializer.class);
    SerializationRegistrar.registerClass(ListLocationSerializer.class);
    SerializationRegistrar.registerClass(ListMaterialDataSerializer.class);
    SerializationRegistrar.registerClass(ListVectorSerializer.class);

    SerializationRegistrar.registerClass(Potion.class);
    SerializationRegistrar.registerClass(CuboidRegion.class);
    SerializationRegistrar.registerClass(Poly2DRegion.class);
  }

  /**
   * Serializes the section sign character produced by {@link ChatColor} as an ampersand {@code &}.
   */
  public static class FriendlyChatColor implements Serializer<ChatColor> {

    @Nullable @Override public Object serialize(ChatColor object) throws IllegalArgumentException {
      if (object == null) {
        return null;
      }
      return "&" + object.getChar();
    }

    @Nullable @Override
    public ChatColor deserialize(@Nullable Object serialized, @Nonnull Class wantedType)
        throws IllegalArgumentException {
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

    @Override public Class<ColorStringSerializer> getSerializerClass() {
      return ColorStringSerializer.class;
    }
  }

  public static class ListComplexItemStackSerializer extends ListSerializer<ItemStack> {

    @Override public Class<ComplexItemStackSerializer> getSerializerClass() {
      return ComplexItemStackSerializer.class;
    }
  }

  public static class ListItemEnchantmentSerializer extends ListSerializer<ItemEnchantment> {

    @Override public Class<ItemEnchantmentSerializer> getSerializerClass() {
      return ItemEnchantmentSerializer.class;
    }
  }

  public static class ListLocationSerializer extends ListSerializer<Location> {

    @Override public Class<LocationSerializer> getSerializerClass() {
      return LocationSerializer.class;
    }
  }

  public static class ListMaterialDataSerializer extends ListSerializer<MaterialData> {

    @Override public Class<MaterialDataSerializer> getSerializerClass() {
      return MaterialDataSerializer.class;
    }
  }

  public static class ListVectorSerializer extends ListSerializer<Vector> {

    @Override public Class<VectorSerializer> getSerializerClass() {
      return VectorSerializer.class;
    }
  }
}
