package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Inventory} instances. This class contains methods such as
 * {@link #removeMaterial(Inventory, Material, boolean, int)}, and more.
 *
 * @since 0.1
 */
public class InventoryUtils {

  /**
   * Deducts a specific amount of {@link Material} from an {@link Inventory}.
   *
   * @param inv inventory to deduct from
   * @param type material to remove
   * @param singleItem whether to stop at one item or keep going
   * @param amount amount of the material to deduct
   *
   * @return true if anything was deducted
   */
  public static boolean removeMaterial(@Nonnull Inventory inv, @Nonnull Material type,
                                       boolean singleItem, int amount) {
    checkNotNull(inv, "inventory cannot be null.");
    checkNotNull(type, "material cannot be null.");
    if (amount <= 0) {
      return false;
    }
    boolean removedAny = false;
    for (ItemStack is : inv.getContents()) {
      if (is != null && is.getType() == type) {
        int newAmount = is.getAmount() - amount;
        removedAny = true;
        if (newAmount > 0) {
          is.setAmount(newAmount);
          break;
        } else { // item is of amount zero
          inv.remove(is);
          amount -= newAmount;
          // check if we should keep removing items.
          if (singleItem || amount <= 0) {
            break;
          }
        }
      }
    }
    return removedAny;
  }
}
