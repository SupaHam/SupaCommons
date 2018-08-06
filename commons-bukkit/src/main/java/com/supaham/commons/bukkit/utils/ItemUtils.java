package com.supaham.commons.bukkit.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link ItemStack} instances. This class contains methods such
 * as {@link #getEnchantment(String)}, {@link #isAir(ItemStack)}, and more.
 *
 * @since 0.1
 */
public class ItemUtils {

  /**
   * Gets an {@link Enchantment} by {@link String}. This method check {@link Enchantment#getByKey(NamespacedKey)} if
   * null it tries {@link Enchantment#getByName(String)}.
   *
   * @param string string to get enchantment by
   *
   * @return enchantment identified by the given string, nullable
   */
  @Nullable
  public static Enchantment getEnchantment(String string) {
    Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(string.toLowerCase()));
    if (enchant == null) {
      enchant = Enchantment.getByName(string); 
    }
    return enchant;
  }

  /**
   * Returns whether an {@link ItemStack} is equivalent to {@link Material#AIR}. If the given
   * {@code item} is null, true is returned, meaning the given item is air.
   *
   * @param item itemstack to check
   *
   * @return whether the {@code item} is air
   */
  public static boolean isAir(@Nullable ItemStack item) {
    return item == null || item.getType() == Material.AIR;
  }
}
