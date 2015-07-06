package com.supaham.commons.bukkit.serializers;

import com.google.common.base.Preconditions;

import com.supaham.commons.CMain;
import com.supaham.commons.bukkit.items.ItemMetaSerializer;
import com.supaham.commons.utils.StringUtils;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import pluginbase.bukkit.config.YamlConfiguration;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;

/**
 * An {@link ItemStack} serializer that currently does not serialize fully, but deserializes
 * fully. This serializer depends on {@link MaterialDataSerializer} used through {@link
 * #getBaseItemStack(String)}, and {@link ItemMetaSerializer} used to handle item metadata.
 *
 * @since 0.1
 */
public class ComplexItemStackSerializer implements Serializer<ItemStack> {
  
  private static final Yaml yaml = new Yaml();

  @Nullable
  @Override
  public Object serialize(ItemStack object) throws IllegalArgumentException {
    if (object == null) {
      return null;
    }
    // TODO fully implement
    CMain.getLogger().warning("ComplexItemStackSerializer serialize is not fully implemented.");
    StringBuilder sb = new StringBuilder();
    sb.append(object.getType());
    if (object.getDurability() != 0) {
      sb.append(':').append(object.getDurability());
    }
    sb.append(" ").append(object.getAmount());
    sb.append(" ").append(yaml.dump(ItemMetaSerializer.serialize(object)));
    return sb.toString();
  }

  @Nullable
  @Override
  public ItemStack deserialize(@Nullable Object serialized, Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null || !(serialized instanceof String)) {
      return null;
    }
    String str = (String) serialized;
    String[] split = str.split("\\s+", 3);
    ItemStack item = getBaseItemStack(split[0]);
    if (split.length == 1 || item.getType() == Material.AIR) {
      return item;
    }

    String metadata;
    if (StringUtils.isNumeric(split[1])) { // amount is defined
      item.setAmount(Integer.parseInt(split[1]));
      if (split.length == 2) { // only amount was specified.
        return item;
      }
      metadata = split[2];
    } else { // Amount was not set, but metadata was.
      metadata = split[1] + " " + split[2];
    }

    YamlConfiguration config = new YamlConfiguration();
    try {
      config.loadFromString(metadata);
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
      return null;
    }
    return ItemMetaSerializer.deserialize(item, config.getValues(false));
  }

  protected ItemStack getBaseItemStack(String string) {
    MaterialData data = Serializers.getSerializer(MaterialDataSerializer.class).deserialize
        (string, MaterialData.class);
    Preconditions.checkArgument(data != null, "Invalid material '" + string + "'.");
    ItemStack item = data.toItemStack(1);
    if (item == null) {
      return new ItemStack(Material.AIR);
    }
    return item;
  }
}
