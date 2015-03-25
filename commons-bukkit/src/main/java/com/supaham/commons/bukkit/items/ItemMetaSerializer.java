package com.supaham.commons.bukkit.items;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.ItemBuilder;
import com.supaham.commons.bukkit.serializers.ItemEnchantmentSerializer;
import com.supaham.commons.bukkit.utils.OBCUtils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;

/**
 * This is not an actual {@link Serializer}, merely a deserializer for {@link ItemMeta} with
 * additional parameters.
 *
 * @see #deserialize(ItemStack, Map)
 * @see #deserialize(ItemBuilder, Map)
 */
public class ItemMetaSerializer {

  /**
   * Deserializes a {@link Map} of Strings and Objects which represents a serialized {@link
   * ItemMeta} metadata, which is later applied to the given {@link ItemStack}.
   *
   * @param item item to append deserialized data to
   * @param map serialized metadata
   *
   * @return {@link ItemBuilder#build()}
   *
   * @see #deserialize(ItemBuilder, Map)
   */
  public static ItemStack deserialize(ItemStack item, Map<String, Object> map) {
    Preconditions.checkNotNull(item, "item cannot be null.");
    Preconditions.checkNotNull(map, "map cannot be null.");
    if (map.isEmpty() || item.getType() == Material.AIR) {
      return item;
    }
    return deserialize(ItemBuilder.builder(new ItemStack(item.getType(), item.getAmount(),
                                                         item.getDurability())), map);
  }

  /**
   * Deserializes a {@link Map} of Strings and Objects which represents a serialized {@link
   * ItemMeta} metadata, which is later applied to the given {@link ItemBuilder}.
   *
   * @param builder item builder to append deserialized data to
   * @param map serialized metadata
   *
   * @return {@link ItemBuilder#build()}
   */
  public static ItemStack deserialize(ItemBuilder builder, Map<String, Object> map) {
    Preconditions.checkNotNull(builder, "builder cannot be null.");
    Preconditions.checkNotNull(map, "map cannot be null.");
    if (map.isEmpty()) {
      return builder.build();
    }
    for (Entry<String, Object> entry : map.entrySet()) {
      Object val = entry.getValue();
      switch (entry.getKey()) {
        case "name":
          builder.name(val.toString());
          break;
        case "lore":
          if (val instanceof String) {
            builder.lore(val.toString());
          } else if (val instanceof List) {
            builder.lore((List<String>) val);
          } else {
            throw new UnsupportedOperationException("lore is of type " + val.getClass());
          }
          break;
        case "enchants":
          ItemEnchantmentSerializer serializer = Serializers
              .getSerializer(ItemEnchantmentSerializer.class);
          if (val instanceof String) {
            builder.enchant(serializer.deserialize(val.toString(), ItemEnchantment.class));
          } else if (val instanceof List) {
            for (String str : ((List<String>) val)) {
              builder.enchant(serializer.deserialize(str, ItemEnchantment.class));
            }
          } else {
            throw new UnsupportedOperationException("enchants is of type " + val.getClass());
          }
          break;
        case "repairCost":
          builder.repairCost((int) val);
          break;
        case "bookTitle":
          builder.bookTitle(val.toString());
          break;
        case "bookAuthor":
          builder.bookAuthor(val.toString());
          break;
        case "bookPage":
        case "bookPages":
          if (val instanceof String) {
            builder.bookAdd(val.toString());
          } else if (val instanceof List) {
            for (String page : ((List<String>) val)) {
              builder.bookAdd(page);
            }
          } else {
            throw new UnsupportedOperationException("book pages is of type " + val.getClass());
          }
          break;
        case "fw":
          throw new UnsupportedOperationException("not implemented yet"); // TODO implement
        case "color":
          if (val instanceof String) {
            String[] split = ((String) val).split("\\s*,\\s*");
            if (split.length > 1) {
              Preconditions.checkArgument(split.length >= 3,
                                          "Invalid R,G,B syntax '" + val + "'.");
              builder
                  .armorColor(Color.fromRGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
                                            Integer.parseInt(split[2])));
            } else {
              if (StringUtils.isNumeric(split[0])) {
                // in case the single rgb value was defined as a string
                builder.armorColor(Color.fromRGB(Integer.parseInt(split[0])));
              } else { // String representation
                builder.armorColor(OBCUtils.getColorByName(split[0]));
              }
            }
          } else if (val instanceof Integer) {
            builder.armorColor(Color.fromRGB(((int) val)));
          } else {
            throw new UnsupportedOperationException("armor color is of type " + val.getClass());
          }
          break;
        case "mapScale":
          builder.mapScale((boolean) val);
          break;
        case "potion":
          throw new UnsupportedOperationException("not implemented yet"); // TODO implement
        case "skull":
          builder.skull(val.toString());
          break;
        case "banner":
          throw new UnsupportedOperationException("not implemented yet"); // TODO implement
      }
    }
    return builder.build();
  }
}
