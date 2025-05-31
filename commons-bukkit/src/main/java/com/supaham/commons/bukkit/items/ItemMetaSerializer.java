package com.supaham.commons.bukkit.items;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import com.supaham.commons.bukkit.ItemBuilder;
import com.supaham.commons.bukkit.serializers.ItemEnchantmentSerializer;
import com.supaham.commons.bukkit.utils.ChatColorUtils;
import com.supaham.commons.bukkit.utils.OBCUtils;
import com.supaham.commons.bukkit.utils.SerializationUtils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

/**
 * This is not an actual {@link Serializer}, merely a deserializer for {@link ItemMeta} with
 * additional parameters.
 * <p />
 * The deserialization basically checks for keys with {@link ItemBuilder}'s method names, such as
 * {@link ItemBuilder#name(String)}, {@link ItemBuilder#lore(String...)}, {@link
 * ItemBuilder#bookTitle(String)}, etc.
 * <p />
 * The following table describes what properties can be of what type: <br />
 *
 * <table>
 * <thead>
 * <tr>
 * <th>Property</th>
 * <th>Applicable Type(s)</th>
 * </tr>
 * </thead>
 *
 * <tr>
 * <td>name</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>lore</td>
 * <td>String<br>List&lt;String&gt;</td>
 * </tr>
 * <tr>
 * <td>glow</td>
 * <td>boolean</td>
 * </tr>
 * <tr>
 * <td>unbreakable</td>
 * <td>boolean</td>
 * </tr>
 * <tr>
 * <td>flags</td>
 * <td>String<br>List&lt;String&gt;<br>See: {@link ItemFlag#valueOf(String)}</td>
 * </tr>
 * <tr>
 * <td>enchant<br>enchants</td>
 * <td>String<br>List&lt;String&gt;<br>See: {@link ItemEnchantmentSerializer}</td>
 * </tr>
 * <tr>
 * <td>repairCost</td>
 * <td>int</td>
 * </tr>
 * <tr>
 * <td>bookTitle</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>bookAuthor</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>bookPage<br>bookPages</td>
 * <td>String<br>List&lt;String&gt;</td>
 * </tr>
 * <tr>
 * <td>fw</td>
 * <td>not implemented yet.</td>
 * </tr>
 * <tr>
 * <td>color</td>
 * <td>String<br>int</td>
 * </tr>
 * <tr>
 * <td>mapScale</td>
 * <td>boolean</td>
 * </tr>
 * <tr>
 * <td>potion</td>
 * <td>not implemented yet.</td>
 * </tr>
 * <tr>
 * <td>skull</td>
 * <td>String</td>
 * </tr>
 * <tr>
 * <td>banner</td>
 * <td>not implemented yet.</td>
 * </tr>
 * </table>
 *
 * @see #deserialize(ItemStack, Map)
 * @see #deserialize(ItemBuilder, Map)
 * @since 0.1
 */
public class ItemMetaSerializer {

  /**
   * Serializes an {@link ItemStack} into a {@link Map} of {@link String} and {@link Object}s.
   *
   * @param item itemstack to serialize
   *
   * @return map of serialized data
   *
   * @see #deserialize(ItemBuilder, Map)
   */
  public static Map<String, Object> serialize(ItemStack item) {
    Map<String, Object> map = new LinkedHashMap<>();
    ItemMeta im = item.getItemMeta();
    if (im.hasDisplayName()) {
      map.put("name", ChatColorUtils.serialize(im.getDisplayName()));
    }
    if (im.hasLore()) {
      List<String> result = new ArrayList<>();
      for (String s : im.getLore()) {
        result.add(ChatColorUtils.serialize(s).toString());
      }
      map.put("lore", result.size() == 1 ? result.get(0) : result);
    }

    if (im.getEnchantmentGlintOverride()) {
      map.put("glow", true);
    }

    if (im.isUnbreakable()) {
      map.put("unbreakable", true);
    }

    if (im.getItemFlags().size() > 0) {
      Set<ItemFlag> flags = im.getItemFlags();
      map.put("flags", flags.size() == 1 ? Iterables.get(flags, 0) : flags);
    }

    {
      ItemEnchantmentSerializer ser =
          SerializationUtils.getSerializer(ItemEnchantmentSerializer.class);
      List<Object> result = new ArrayList<>();
      for (Entry<Enchantment, Integer> entry : im.getEnchants().entrySet()) {
        result.add(ser.serialize(new ItemEnchantment(entry.getKey(), entry.getValue()),
                                 SerializerSet.defaultSet()));
      }
      if (!result.isEmpty()) {
        map.put("enchants", result.size() == 1 ? result.get(0) : result);
      }
    }
    if (((Repairable) im).getRepairCost() != 0) {
      map.put("repairCost", ((Repairable) im).getRepairCost());
    }
    if (im instanceof BookMeta) {
      BookMeta bm = ((BookMeta) im);
      map.put("bookTitle", bm.getTitle());
      map.put("bookAuthor", bm.getAuthor());
      map.put("bookPages", bm.getPages());
    }
    if (im instanceof LeatherArmorMeta) {
      LeatherArmorMeta lam = ((LeatherArmorMeta) im);
      map.put("color", lam.getColor().asRGB());
    }
    if (im instanceof MapMeta) {
      map.put("mapScale", ((MapMeta) im).isScaling());
    }
    if (im instanceof SkullMeta) {
      map.put("skull", ((SkullMeta) im).getOwner());
    }
    return map;
  }

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
          builder.name(ChatColorUtils.deserialize(val.toString()));
          break;
        case "lore":
          if (val instanceof String) {
            builder.lore(ChatColorUtils.deserialize(val.toString()));
          } else if (val instanceof List) {
            List<String> result = new ArrayList<>();
            for (String s : (List<String>) val) {
              result.add(ChatColorUtils.deserialize(s));
            }
            builder.lore(result);
          } else {
            throw new UnsupportedOperationException("lore is of type " + val.getClass());
          }
          break;
        case "glow":
          builder.glow(Boolean.valueOf(val.toString()));
          break;
        case "unbreakable":
          builder.unbreakable(Boolean.valueOf(val.toString()));
          break;
        case "flags":
          if (val instanceof String) {
            builder.flag(ItemFlag.valueOf(val.toString().toUpperCase()));
          } else if (val instanceof List) {
            for (Object o : ((List) val)) {
              builder.flag(ItemFlag.valueOf(o.toString()));
            }
          } else {
            throw new UnsupportedOperationException("flags is of type " + val.getClass());
          }
          break;
        case "enchants":
          ItemEnchantmentSerializer serializer =
              SerializationUtils.getSerializer(ItemEnchantmentSerializer.class);
          if (val instanceof String) {
            builder.enchant(serializer.deserialize(val.toString(), ItemEnchantment.class,
                                                   SerializerSet.defaultSet()));
          } else if (val instanceof List) {
            for (String str : ((List<String>) val)) {
              builder.enchant(serializer.deserialize(str, ItemEnchantment.class,
                                                     SerializerSet.defaultSet()));
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
