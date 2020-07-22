package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.data.type.*;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
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

  private static final Set<Material> interactableBlocks = EnumSet.noneOf(Material.class);
  private static final Set<Class<?>> interactableBlockClasses = new HashSet<>();
  public static final Map<DyeColor, Material> dyeToWool = new EnumMap<>(DyeColor.class);
  public static final Map<DyeColor, Material> dyeToBanner = new EnumMap<>(DyeColor.class);
  public static final Map<DyeColor, Material> dyeToItem = new EnumMap<>(DyeColor.class);
  public static final Map<Material, Material> itemWallableMapping = new EnumMap<>(Material.class);

  static {
    interactableBlocks.add(Material.DISPENSER);
    interactableBlocks.add(Material.NOTE_BLOCK);
    interactableBlocks.add(Material.TNT);
    interactableBlocks.add(Material.CHEST);
    interactableBlocks.add(Material.CRAFTING_TABLE);
    interactableBlocks.add(Material.BEETROOTS);
    interactableBlocks.add(Material.CARROTS);
    interactableBlocks.add(Material.FARMLAND);
    interactableBlocks.add(Material.FURNACE);
    interactableBlocks.add(Material.STONE_BUTTON);
    interactableBlocks.add(Material.JUKEBOX);
    interactableBlocks.add(Material.SOUL_SAND);
    interactableBlocks.add(Material.CAKE);
    interactableBlocks.add(Material.REPEATER);
    interactableBlocks.add(Material.ENCHANTING_TABLE);
    interactableBlocks.add(Material.BREWING_STAND);
    interactableBlocks.add(Material.CAULDRON);
    interactableBlocks.add(Material.END_PORTAL_FRAME);
    interactableBlocks.add(Material.DRAGON_EGG);
    interactableBlocks.add(Material.ENDER_CHEST);
    interactableBlocks.add(Material.BEACON);
    interactableBlocks.add(Material.FLOWER_POT);
    interactableBlocks.add(Material.CARROT);
    interactableBlocks.add(Material.POTATO);
    interactableBlocks.add(Material.ANVIL);
    interactableBlocks.add(Material.TRAPPED_CHEST);
    interactableBlocks.add(Material.COMPARATOR);
    interactableBlocks.add(Material.HOPPER);
    interactableBlocks.add(Material.DROPPER);

    interactableBlockClasses.add(Bed.class);
    interactableBlockClasses.add(CommandBlock.class);
    interactableBlockClasses.add(Door.class);
    interactableBlockClasses.add(Fence.class);
    interactableBlockClasses.add(Gate.class);
    interactableBlockClasses.add(Switch.class);
    interactableBlockClasses.add(TrapDoor.class);

    dyeToWool.put(DyeColor.WHITE, Material.WHITE_WOOL);
    dyeToWool.put(DyeColor.ORANGE, Material.ORANGE_WOOL);
    dyeToWool.put(DyeColor.MAGENTA, Material.MAGENTA_WOOL);
    dyeToWool.put(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_WOOL);
    dyeToWool.put(DyeColor.YELLOW, Material.YELLOW_WOOL);
    dyeToWool.put(DyeColor.LIME, Material.LIME_WOOL);
    dyeToWool.put(DyeColor.PINK, Material.PINK_WOOL);
    dyeToWool.put(DyeColor.GRAY, Material.GRAY_WOOL);
    dyeToWool.put(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_WOOL);
    dyeToWool.put(DyeColor.CYAN, Material.CYAN_WOOL);
    dyeToWool.put(DyeColor.PURPLE, Material.PURPLE_WOOL);
    dyeToWool.put(DyeColor.BLUE, Material.BLUE_WOOL);
    dyeToWool.put(DyeColor.BROWN, Material.BROWN_WOOL);
    dyeToWool.put(DyeColor.GREEN, Material.GREEN_WOOL);
    dyeToWool.put(DyeColor.RED, Material.RED_WOOL);
    dyeToWool.put(DyeColor.BLACK, Material.BLACK_WOOL);

    dyeToBanner.put(DyeColor.WHITE, Material.WHITE_BANNER);
    dyeToBanner.put(DyeColor.ORANGE, Material.ORANGE_BANNER);
    dyeToBanner.put(DyeColor.MAGENTA, Material.MAGENTA_BANNER);
    dyeToBanner.put(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_BANNER);
    dyeToBanner.put(DyeColor.YELLOW, Material.YELLOW_BANNER);
    dyeToBanner.put(DyeColor.LIME, Material.LIME_BANNER);
    dyeToBanner.put(DyeColor.PINK, Material.PINK_BANNER);
    dyeToBanner.put(DyeColor.GRAY, Material.GRAY_BANNER);
    dyeToBanner.put(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_BANNER);
    dyeToBanner.put(DyeColor.CYAN, Material.CYAN_BANNER);
    dyeToBanner.put(DyeColor.PURPLE, Material.PURPLE_BANNER);
    dyeToBanner.put(DyeColor.BLUE, Material.BLUE_BANNER);
    dyeToBanner.put(DyeColor.BROWN, Material.BROWN_BANNER);
    dyeToBanner.put(DyeColor.GREEN, Material.GREEN_BANNER);
    dyeToBanner.put(DyeColor.RED, Material.RED_BANNER);
    dyeToBanner.put(DyeColor.BLACK, Material.BLACK_BANNER);

    dyeToItem.put(DyeColor.WHITE, Material.BONE_MEAL);
    dyeToItem.put(DyeColor.ORANGE, Material.ORANGE_DYE);
    dyeToItem.put(DyeColor.MAGENTA, Material.MAGENTA_DYE);
    dyeToItem.put(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_DYE);
    dyeToItem.put(DyeColor.YELLOW, Material.YELLOW_DYE);
    dyeToItem.put(DyeColor.LIME, Material.LIME_DYE);
    dyeToItem.put(DyeColor.PINK, Material.PINK_DYE);
    dyeToItem.put(DyeColor.GRAY, Material.GRAY_DYE);
    dyeToItem.put(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_DYE);
    dyeToItem.put(DyeColor.CYAN, Material.CYAN_DYE);
    dyeToItem.put(DyeColor.PURPLE, Material.PURPLE_DYE);
    dyeToItem.put(DyeColor.BLUE, Material.LAPIS_LAZULI);
    dyeToItem.put(DyeColor.BROWN, Material.COCOA_BEANS);
    dyeToItem.put(DyeColor.GREEN, Material.GREEN_DYE);
    dyeToItem.put(DyeColor.RED, Material.RED_DYE);
    dyeToItem.put(DyeColor.BLACK, Material.INK_SAC);

    itemWallableMapping.put(Material.WHITE_BANNER, Material.WHITE_WALL_BANNER);
    itemWallableMapping.put(Material.ORANGE_BANNER, Material.ORANGE_WALL_BANNER);
    itemWallableMapping.put(Material.MAGENTA_BANNER, Material.MAGENTA_WALL_BANNER);
    itemWallableMapping.put(Material.LIGHT_BLUE_BANNER, Material.LIGHT_BLUE_WALL_BANNER);
    itemWallableMapping.put(Material.YELLOW_BANNER, Material.YELLOW_WALL_BANNER);
    itemWallableMapping.put(Material.LIME_BANNER, Material.LIME_WALL_BANNER);
    itemWallableMapping.put(Material.PINK_BANNER, Material.PINK_WALL_BANNER);
    itemWallableMapping.put(Material.GRAY_BANNER, Material.GRAY_WALL_BANNER);
    itemWallableMapping.put(Material.LIGHT_GRAY_BANNER, Material.LIGHT_GRAY_WALL_BANNER);
    itemWallableMapping.put(Material.CYAN_BANNER, Material.CYAN_WALL_BANNER);
    itemWallableMapping.put(Material.PURPLE_BANNER, Material.PURPLE_WALL_BANNER);
    itemWallableMapping.put(Material.BLUE_BANNER, Material.BLUE_WALL_BANNER);
    itemWallableMapping.put(Material.BROWN_BANNER, Material.BROWN_WALL_BANNER);
    itemWallableMapping.put(Material.GREEN_BANNER, Material.GREEN_WALL_BANNER);
    itemWallableMapping.put(Material.RED_BANNER, Material.RED_WALL_BANNER);
    itemWallableMapping.put(Material.BLACK_BANNER, Material.BLACK_WALL_BANNER);
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
      case CHEST:
      case DISPENSER:
      case REPEATER:
      case DRAGON_EGG:
      case DROPPER:
      case ENCHANTING_TABLE:
      case ENDER_CHEST:
      case FURNACE:
      case HOPPER:
      case JUKEBOX:
      case COMPARATOR:
      case TRAPPED_CHEST:
      case CRAFTING_TABLE:
      case CHEST_MINECART:
      case FURNACE_MINECART:
      case HOPPER_MINECART:
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
    return material.data.equals(Sign.class) || material.data.equals(WallSign.class);
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
      case GOLDEN_AXE:
      case GOLDEN_SHOVEL:
      case GOLDEN_SWORD:
      case GOLDEN_PICKAXE:
      case GOLDEN_HOE:
        return true;
      default:
        return false;
    }
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
    return material != null && (material.data == Leaves.class);
  }

  /**
   * Returns whether a {@link Material} matches any of the given others.
   *
   * @param material material to test
   * @param any materials to check for
   *
   * @return whether {@code material} is of any of the materials in {@code any}
   */
  public static boolean equals(@Nullable Material material, @Nonnull Material... any) {
    Preconditions.checkNotNull(any, "array of materials cannot be null.");
    for (Material anyMat : any) {
      if (material == null && anyMat == Material.AIR) {
        return true;
      } else if (material == anyMat) {
        return true;
      }
    }
    return false;
  }

  private MaterialUtils() {}
}
