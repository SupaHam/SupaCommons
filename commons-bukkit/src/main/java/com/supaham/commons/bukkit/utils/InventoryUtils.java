package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Inventory} instances. This class contains methods such
 * as
 * {@link #removeMaterial(Inventory, Material, boolean, int)}, and more.
 *
 * @since 0.1
 */
public class InventoryUtils {

  /**
   * Deducts a specific amount of {@link Material} from an {@link Inventory}. This is equivalent to
   * calling {@link #removeMaterial(Inventory, Material, boolean, int)} with the boolean as false.
   *
   * @param inventory inventory to deduct from
   * @param type material to remove
   * @param amount amount of the material to deduct
   *
   * @return count of items that were removed from the inventory
   */
  public static int removeMaterial(@Nonnull Inventory inventory, @Nonnull Material type,
                                   int amount) {
    return removeMaterial(inventory, type, false, amount);
  }

  /**
   * Deducts a specific amount of {@link Material} from an {@link Inventory}.
   *
   * @param inventory inventory to deduct from
   * @param type material to remove
   * @param singleStack whether to stop at one itemstack or keep going
   * @param amount amount of the material to deduct
   *
   * @return count of items that were removed from the inventory
   */
  public static int removeMaterial(@Nonnull Inventory inventory, @Nonnull Material type,
                                   boolean singleStack, int amount) {
    checkNotNull(inventory, "inventory cannot be null.");
    checkNotNull(type, "material cannot be null.");
    if (amount <= 0) {
      return 0;
    }
    int initialAmount = amount;
    for (ItemStack is : inventory.getContents()) {
      if (is != null && is.getType() == type) {
        int newAmount = is.getAmount() - amount;
        if (newAmount > 0) {
          is.setAmount(newAmount);
          break;
        } else { // item is of amount zero
          inventory.remove(is);
          amount -= newAmount;
          // check if we should keep removing items.
          if (singleStack || amount <= 0) {
            break;
          }
        }
      }
    }
    return initialAmount - amount;
  }
}
