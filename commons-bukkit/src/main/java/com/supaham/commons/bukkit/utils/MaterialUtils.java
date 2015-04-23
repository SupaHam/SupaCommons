package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility methods for working with {@link Material} instances. This class contains methods such as
 * {@link #isInteractableBlock(Material)}, {@link #isContainer(Material)}, and more.
 *
 * @since 0.1
 */
public final class MaterialUtils {

  private MaterialUtils() {}

  private static final Set<Material> interactableBlocks = EnumSet.noneOf(Material.class);

  static {
    interactableBlocks.clear();
    interactableBlocks.add(Material.DISPENSER);
    interactableBlocks.add(Material.NOTE_BLOCK);
    interactableBlocks.add(Material.BED_BLOCK);
    interactableBlocks.add(Material.TNT);
    interactableBlocks.add(Material.CHEST);
    interactableBlocks.add(Material.WORKBENCH);
    interactableBlocks.add(Material.CROPS);
    interactableBlocks.add(Material.SOIL);
    interactableBlocks.add(Material.FURNACE);
    interactableBlocks.add(Material.BURNING_FURNACE);
    interactableBlocks.add(Material.WOODEN_DOOR);
    interactableBlocks.add(Material.LEVER);
    interactableBlocks.add(Material.IRON_DOOR_BLOCK);
    interactableBlocks.add(Material.STONE_BUTTON);
    interactableBlocks.add(Material.JUKEBOX);
    interactableBlocks.add(Material.FENCE);
    interactableBlocks.add(Material.SOUL_SAND);
    interactableBlocks.add(Material.CAKE_BLOCK);
    interactableBlocks.add(Material.DIODE_BLOCK_OFF);
    interactableBlocks.add(Material.DIODE_BLOCK_ON);
    interactableBlocks.add(Material.TRAP_DOOR);
    interactableBlocks.add(Material.IRON_FENCE);
    interactableBlocks.add(Material.FENCE_GATE);
    interactableBlocks.add(Material.NETHER_FENCE);
    interactableBlocks.add(Material.ENCHANTMENT_TABLE);
    interactableBlocks.add(Material.BREWING_STAND);
    interactableBlocks.add(Material.CAULDRON);
    interactableBlocks.add(Material.ENDER_PORTAL_FRAME);
    interactableBlocks.add(Material.DRAGON_EGG);
    // TODO possibly interactable? interactableBlocks.add(Material.COCOA);
    interactableBlocks.add(Material.ENDER_CHEST);
    interactableBlocks.add(Material.COMMAND);
    interactableBlocks.add(Material.BEACON);
    interactableBlocks.add(Material.FLOWER_POT);
    interactableBlocks.add(Material.CARROT);
    interactableBlocks.add(Material.POTATO);
    interactableBlocks.add(Material.WOOD_BUTTON);
    interactableBlocks.add(Material.ANVIL);
    interactableBlocks.add(Material.TRAPPED_CHEST);
    interactableBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
    interactableBlocks.add(Material.REDSTONE_COMPARATOR_ON);
    interactableBlocks.add(Material.HOPPER);
    interactableBlocks.add(Material.DROPPER);
  }

  /**
   * Checks whether a {@link Material} can be interacted with.
   *
   * @param material material to check
   *
   * @return true if the {@code material} can be interacted with
   */
  public static boolean isInteractableBlock(Material material) {
    return material != null && material.isBlock() && interactableBlocks.contains(material);
  }

  /**
   * Checks whether a {@link Material} is a container. This doesn't necessarily mean it has a
   * persistent inventory.
   *
   * @param material material to check
   *
   * @return whether the {@code material} is a container
   */
  public static boolean isContainer(Material material) {
    switch (material) {
      case BEACON:
      case BREWING_STAND:
      case BURNING_FURNACE:
      case CHEST:
      case DISPENSER:
      case DIODE:
      case DIODE_BLOCK_OFF:
      case DIODE_BLOCK_ON:
      case DRAGON_EGG:
      case DROPPER:
      case ENCHANTMENT_TABLE:
      case ENDER_CHEST:
      case FURNACE:
      case HOPPER:
      case JUKEBOX:
      case REDSTONE_COMPARATOR:
      case REDSTONE_COMPARATOR_OFF:
      case REDSTONE_COMPARATOR_ON:
      case TRAPPED_CHEST:
      case WORKBENCH:
      case STORAGE_MINECART:
      case POWERED_MINECART:
        return true;
      default:
        return false;
    }
  }

  /**
   * Checks whether a {@link Material} is a sign.
   *
   * @param material material to test
   *
   * @return whether the {@code material} is a sign
   */
  public static boolean isSign(Material material) {
    return material == Material.SIGN
           || material == Material.SIGN_POST
           || material == Material.WALL_SIGN;
  }

  /**
   * Checks whether a {@link Block} is the same as a {@link MaterialData}. If either the Block or
   * the MaterialData have a data (damage/durability) value of -1, only the {@link Material} is
   * tested.
   *
   * @param block block to check
   * @param materialData {@link MaterialData} to test against
   *
   * @return whether the {@code block}'s data matches the {@code materialData}
   */
  public static boolean same(Block block, MaterialData materialData) {
    return same(block.getType(), block.getData(), materialData);
  }

  /**
   * Checks whether an {@link ItemStack} is the same as a {@link MaterialData}. If either the
   * ItemStack or the MaterialData have a data (damage/durability) value of -1, only the {@link
   * Material} is tested.
   *
   * @param item item to check
   * @param materialData {@link MaterialData} to test against
   *
   * @return whether the {@code item}'s data matches the {@code materialData}
   */
  public static boolean same(ItemStack item, MaterialData materialData) {
    return same(item.getData(), materialData);
  }

  /**
   * Checks whether a {@link Material} and data value (byte) is the same as a {@link MaterialData}.
   * This simply constructs a {@link MaterialData} out of the given data and calls {@link
   * #same(MaterialData, MaterialData)}. If either the Block or the MaterialData have a data
   * (damage/durability) value of -1, only the {@link Material} is tested.
   *
   * @param type type to check
   * @param data data to check, set to -1 to ignore data checks
   * @param materialData {@link MaterialData} to test against
   *
   * @return whether the {@code block}'s data matches the {@code materialData}
   */
  public static boolean same(Material type, byte data, MaterialData materialData) {
    return same(new MaterialData(type, data), materialData);
  }

  /**
   * Checks whether a {@link MaterialData} is the same as another {@link MaterialData}. If either
   * of
   * the MaterialDatas have a data (damage/durability) value of -1, only the {@link Material} is
   * tested.
   *
   * @param data1 first {@link MaterialData} to test
   * @param data2 first {@link MaterialData} to test
   *
   * @return whether the {@code block}'s data matches the {@code materialData}
   */
  public static boolean same(MaterialData data1, MaterialData data2) {
    if (data1 == null) {
      return data2 == null;
    } else if (data2 == null) {
      return false;
    }
    return data1.getItemType().equals(data2.getItemType())
           && ((data1.getData() == -1 || data2.getData() == -1)
               || data1.getData() == data2.getData());
  }

  /**
   * Checks whether a {@link Material} is a gold tool.
   *
   * @param material material to check
   *
   * @return true if the {@code material} is a gold tool
   */
  public static boolean isGoldTool(@Nonnull Material material) {
    checkNotNull(material, "material cannot be null.");
    switch (material) {
      case GOLD_AXE:
      case GOLD_SPADE:
      case GOLD_SWORD:
      case GOLD_PICKAXE:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns a {@link MaterialData} with the type as the dropped item of the given material data.
   *
   * @param materialData material data to convert to item. This is returned if the type is not an
   * item
   *
   * @return material data
   */
  public static MaterialData toItem(@Nonnull MaterialData materialData) {
    Material material = materialData.getItemType();
    if (!material.isBlock()) {
      return materialData;
    }
    byte data = 0;
    byte mdata = materialData.getData();
    switch (material) {
      case STONE:
        material = Material.COBBLESTONE;
        break;
      case GRASS:
      case SOIL:
      case MYCEL:
        material = Material.DIRT;
        break;
      case GRAVEL:
        material = Material.FLINT;
        break;
      case GOLD_ORE:
        material = Material.GOLD_INGOT;
        break;
      case IRON_ORE:
        material = Material.IRON_INGOT;
        break;
      case COAL_ORE:
        material = Material.COAL;
        break;
      case LOG:
      case LOG_2:
        data = TreeSpecies.getByData((byte) (mdata & 0x3)).getData();
        material = Material.WOOD;
        break;
      case LEAVES:
      case LEAVES_2:
        material = Material.SAPLING;
        data = TreeSpecies.getByData((byte) (mdata & 0x3)).getData();
        break;
      case LAPIS_ORE:
        material = Material.INK_SACK;
        data = 4;
        break;
      case BED:
        material = Material.BED;
        break;
      case PISTON_EXTENSION:
      case PISTON_MOVING_PIECE:
        material = Material.PISTON_BASE;
        break;
      case BOOKSHELF:
        material = Material.BOOK;
        break;
      case REDSTONE_WIRE:
      case REDSTONE_ORE:
      case GLOWING_REDSTONE_ORE:
        material = Material.REDSTONE;
        break;
      case DIAMOND_ORE:
        material = Material.DIAMOND;
        break;
      case CROPS:
        material = Material.SEEDS;
        break;
      case BURNING_FURNACE:
        material = Material.FURNACE;
        break;
      case SIGN_POST:
      case WALL_SIGN:
        material = Material.SIGN;
        break;
      case WOODEN_DOOR:
        material = Material.WOOD_DOOR;
        break;
      case REDSTONE_TORCH_ON:
        material = Material.REDSTONE_TORCH_OFF;
        break;
      case CLAY:
        material = Material.CLAY_BALL;
        break;
      case DIODE_BLOCK_ON:
        material = Material.DIODE;
        break;
      case HUGE_MUSHROOM_1:
        material = Material.RED_MUSHROOM;
        break;
      case HUGE_MUSHROOM_2:
        material = Material.BROWN_MUSHROOM;
        break;
      case IRON_FENCE:
        break;
      case PUMPKIN_STEM:
        material = Material.PUMPKIN_SEEDS;
        break;
      case MELON_STEM:
        material = Material.MELON_SEEDS;
        data = 0;
        break;
      case BREWING_STAND:
        material = Material.BREWING_STAND_ITEM;
        break;
      case CAULDRON:
        material = Material.CAULDRON_ITEM;
        break;
      case DRAGON_EGG:
        break;
      case REDSTONE_LAMP_ON:
        material = Material.REDSTONE_LAMP_OFF;
        break;
      case WOOD_DOUBLE_STEP:
        material = Material.WOOD_STEP;
        break;
      case COCOA:
        material = Material.INK_SACK;
        data = 3;
        break;
      case EMERALD_ORE:
        material = Material.EMERALD;
        break;
      case TRIPWIRE_HOOK:
        material = Material.EMERALD;
        break;
      case TRIPWIRE:
        material = Material.STRING;
        break;
      case CARROT:
        material = Material.CARROT_ITEM;
        break;
      case POTATO:
        material = Material.POTATO_ITEM;
        break;
      case REDSTONE_COMPARATOR_ON:
        material = Material.REDSTONE_COMPARATOR_OFF;
        break;
      case DAYLIGHT_DETECTOR_INVERTED:
        material = Material.DAYLIGHT_DETECTOR;
        break;
      case QUARTZ_ORE:
        material = Material.QUARTZ;
        break;
      case CARPET:
      case DOUBLE_PLANT:
        data = mdata;
        break;

      case STANDING_BANNER:
      case WALL_BANNER:
        material = Material.BANNER;
        break;
      case DOUBLE_STONE_SLAB2:
        material = Material.STONE_SLAB2;
        data = mdata;
        break;
    }
    return new MaterialData(material, data);
  }

  /**
   * Returns whether a {@link Material} is a block of leaves. Useful since Minecraft has multiple
   * leaves blocks with different ids.
   *
   * @param material material to check, nullable
   *
   * @return whether the given material is leaves
   */
  public static boolean isLeaves(@Nullable Material material) {
    return material != null && (material == Material.LEAVES || material == Material.LEAVES_2);
  }
}
