package com.supaham.commons.bukkit.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link ItemStack} instances. This class contains methods such as
 * {@link #getEnchantment(String)}, and more.
 *
 * @since 0.1
 */
public class ItemUtils {
  
  /**
   * Gets an {@link Enchantment} by {@link String}. This method check if the given string is
   * numeric, if it is, {@link Enchantment#getById(int)} is called with the string parsed as
   * integer. Otherwise, {@link Enchantment#getByName(String)} is called.
   *
   * @param string string to get enchantment by
   *
   * @return enchantment identified by the given string, nullable
   */
  @Nullable
  public static Enchantment getEnchantment(String string) {
    return StringUtils.isNumeric(string) ? Enchantment.getById(Integer.parseInt(string))
                                         : Enchantment.getByName(string);
  }
}
