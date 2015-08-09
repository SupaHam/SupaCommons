package com.supaham.commons.bukkit.serializers;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.items.ItemEnchantment;
import com.supaham.commons.bukkit.utils.ItemUtils;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import pluginbase.config.serializers.Serializer;

/**
 * An {@link ItemEnchantment} serializer that serializes as "x:y" where x is {@link
 * Enchantment#getName()} and y is {@link ItemEnchantment#getLevel()}. The serialization is "x" 
 * alone if the y is less than 2.
 *
 * @since 0.1
 */
public class ItemEnchantmentSerializer implements Serializer<ItemEnchantment> {

  @Nullable
  @Override
  public Object serialize(ItemEnchantment object) throws IllegalArgumentException {
    if (object == null) {
      return null;
    }
    return object.getEnchantment().getName()
           + (object.getLevel() > 1 ? ":" + object.getLevel() : "");
  }

  @Nullable
  @Override
  public ItemEnchantment deserialize(@Nullable Object serialized, @Nonnull Class wantedType)
      throws IllegalArgumentException {
    if (serialized == null) {
      return null;
    }
    String[] split = serialized.toString().split(":");
    Enchantment ench = ItemUtils.getEnchantment(split[0]);
    Preconditions.checkArgument(ench != null, "enchantment '" + split[0] + "' does not exist.");
    return new ItemEnchantment(ench, split.length > 1 ? Integer.parseInt(split[1]) : 1);
  }
}
