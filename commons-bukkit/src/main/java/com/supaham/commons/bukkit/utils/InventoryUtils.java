package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import com.supaham.commons.utils.ArrayUtils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Inventory} instances. This class contains methods such
 * as
 * {@link #removeMaterial(Inventory, Material, boolean, int)}, and more.
 *
 * @since 0.1
 */
public class InventoryUtils {

  public static InventoryModificationResult addItem(@Nonnull Inventory inventory,
                                                    @Nonnull ItemStack item, int... slots) {
    checkNotNull(inventory, "inventory cannot be null.");
    checkNotNull(item, "item cannot be null.");

    item = item.clone();
    boolean anySlot = false;

    // If we should add to any and all available slots, just let bukkit handle it
    for (int slot : slots) {
      if (slot == -1) {
        anySlot = true;
      }
    }

    InventoryModificationResult result = new InventoryModificationResult();
    outer:
    for (int slot = 0; slot < inventory.getSize(); slot++) {
      if (!anySlot) {
        for (int i : slots) {
          if (slot != i) {
            continue outer;
          }
        }
      }

      // TODO should the success inserts insert the exact increase to said slot instead?
      ItemStack currItem = inventory.getItem(slot);
      if (!ItemUtils.isAir(currItem)) {
        int diff = currItem.getMaxStackSize()
                   - currItem.getAmount(); // how much we can add to this itemstack
        if (diff > 0 && item.isSimilar(currItem)) { // we can add to this stack
          if (diff >= item.getAmount()) { // currItem can handle the whole 'item' itemstack
            currItem.setAmount(currItem.getAmount() + item.getAmount());
            item.setAmount(0);
            result.success.put(slot, currItem.clone());
            inventory.setItem(slot, currItem); // Update the itemstack in the inventory
            break; // The given item stack is depleted. Mission accomplished.
          } else {
            item.setAmount(item.getAmount() - diff);
            currItem.setAmount(currItem.getAmount() + diff);
            result.success.put(slot, currItem.clone());
            inventory.setItem(slot, currItem); // Update the itemstack in the inventory
          }
        }
      } else {
        inventory.setItem(slot, item); // add the given itemstack to the inventory
        result.success.put(slot, item);
      }
    }

    if (item.getAmount() > 0) {
      result.failure.put(-1, item);
    }
    return result;
  }

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
    if (inventory instanceof PlayerInventory) {
      int heldItemSlot = ((PlayerInventory) inventory).getHeldItemSlot();
      ItemStack held = inventory.getItem(heldItemSlot);
      if (held != null && held.getType() == type) {
        int newAmount = held.getAmount() - amount; // new amount of the held itemstack
        amount -= held.getAmount();
        if (newAmount > 0) { // The itemstack is only to be deducted, not removed (check else)
          held.setAmount(newAmount);
          return initialAmount - amount;
        } else { // The held itemstack is depleted, remove it.
          inventory.setItem(heldItemSlot, null);
          // check if we should keep removing items.
          if (singleStack || amount <= 0) {
            return initialAmount - amount;
          }
        }
      }
    }
    ItemStack[] contents = inventory.getContents();
    
    // Try deduct from the held item first
    if (inventory instanceof PlayerInventory) {
      int heldItemSlot = ((PlayerInventory) inventory).getHeldItemSlot();
      ItemStack[] held = new ItemStack[]{inventory.getItem(heldItemSlot)};
      contents = (ItemStack[]) ArrayUtils.addAll(held, contents);
    }
    
    for (int slot = 0; slot < contents.length; slot++) {
      ItemStack is = contents[slot];
      if (is != null && is.getType() == type) {
        int newAmount = is.getAmount() - amount; // new amount of the held itemstack
        amount -= is.getAmount();
        if (newAmount > 0) { // The itemstack is only to be deducted, not removed (check else)
          is.setAmount(newAmount);
          break;
        } else { // The itemstack is depleted, remove it.
          inventory.setItem(slot, null);
          // check if we should keep removing items.
          if (singleStack || amount <= 0) {
            break;
          }
        }
      }
    }
    return initialAmount - amount;
  }

  public static class InventoryModificationResult {

    private final Map<Integer, ItemStack> success = new LinkedHashMap<>();
    private final Map<Integer, ItemStack> failure = new LinkedHashMap<>();

    @Override public String toString() {
      return "InventoryModificationResult{" +
             "success=" + success +
             ", failure=" + failure +
             '}';
    }

    public Map<Integer, ItemStack> getSuccesses() {
      return ImmutableMap.copyOf(success);
    }

    public Map<Integer, ItemStack> getFailures() {
      return ImmutableMap.copyOf(failure);
    }

    public Collection<ItemStack> getSuccessItems() {
      return Collections.unmodifiableCollection(success.values());
    }

    public Optional<ItemStack> getFirstSuccess() {
      return Optional.fromNullable(Iterables.getFirst(success.values(), null));
    }

    public Optional<ItemStack> getLastSuccess() {
      return Optional.fromNullable(Iterables.getLast(success.values(), null));
    }

    public Collection<ItemStack> getFailureItems() {
      return Collections.unmodifiableCollection(failure.values());
    }

    public Optional<ItemStack> getFirstFailure() {
      return Optional.fromNullable(Iterables.getFirst(failure.values(), null));
    }

    public Optional<ItemStack> getLastFailure() {
      return Optional.fromNullable(Iterables.getLast(failure.values(), null));
    }
  }
}
