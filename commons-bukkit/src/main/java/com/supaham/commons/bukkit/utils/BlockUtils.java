package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.bukkit.SingleSound;
import com.supaham.commons.bukkit.TickerTask;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Block} instances.
 *
 * @since 0.1
 */
public final class BlockUtils {

  /**
   * Returns whether snow may be placed under a {@link Block}.
   *
   * @param block block to check
   *
   * @return whether snow may be placed
   */
  public static boolean canPlaceSnowUnderneath(@Nonnull Block block) {
    Preconditions.checkNotNull(block, "block cannot be null.");
    block = block.getRelative(BlockFace.DOWN);
    byte d = block.getData();
    Material type = block.getType();
    return type != Material.ICE && type != Material.PACKED_ICE
           && (MaterialUtils.isLeaves(type) || (type == Material.SNOW && d >= 7 || type.isSolid()));
  }

  /**
   * Sets up a {@link BlockState} with the properties of a {@link ItemStack}, including its {@link BannerMeta} for
   * patterns. This method finally calls {@link BlockState#update(boolean, boolean)} forcefully and quietly, causing an
   * update but without any physics checks.
   *
   * @param state block state to modify
   * @param itemStack banner itemstack to apply to {@code block}
   *
   * @throws IllegalArgumentException if the {@code block} is not a banner
   */
  public static void setBlockBanner(BlockState state, ItemStack itemStack) {
    Preconditions.checkArgument(state instanceof Banner, "block state is not of Banner.");
    Preconditions.checkArgument(itemStack.getItemMeta() instanceof BannerMeta, "ItemStack not BannerMeta");
    Banner bannerState = (Banner) state;
    BannerMeta bannerMeta = ((BannerMeta) itemStack.getItemMeta());
    
    if (state.getType() != itemStack.getType()) {
      Material mat = itemStack.getType();
      if (state.getType().name().contains("WALL")) {
        mat = MaterialUtils.itemWallableMapping.get(mat);
      }
      bannerState.setType(mat);
    }
    bannerState.setPatterns(bannerMeta.getPatterns());
    bannerState.update(true, false);
  }

  public static boolean pressPressurePlate(@Nonnull CommonPlugin plugin, @Nonnull Block block) {
    return pressPressurePlate(plugin, block, true);
  }

  public static boolean pressPressurePlate(@Nonnull CommonPlugin plugin, @Nonnull Block block,
                                           boolean playSound) {
    return pressPressurePlate(plugin, block, 10, playSound);
  }

  public static boolean pressPressurePlate(@Nonnull CommonPlugin plugin, @Nonnull final Block block,
                                           int pressedTicks) {
    return pressPressurePlate(plugin, block, pressedTicks, true);
  }

  public static boolean pressPressurePlate(@Nonnull CommonPlugin plugin, @Nonnull final Block block,
                                           int pressedTicks, final boolean playSound) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    Preconditions.checkNotNull(block, "block cannot be null.");
    Preconditions.checkArgument(pressedTicks >= 0, "pressed ticks cannot be less than 0.");

    if (!block.getType().name().endsWith("PRESSURE_PLATE")) {
      return false;
    }
    {
      BlockData blockData = block.getBlockData();
      if (blockData instanceof Powerable) {
        ((Powerable) blockData).setPowered(true);
      } else if (blockData instanceof AnaloguePowerable) {
        ((AnaloguePowerable) blockData).setPower(1);
      } else {
        throw new IllegalArgumentException("Unknown block type.");
      }
      block.setBlockData(blockData, false);
    }
    if (playSound) {
      new SingleSound(Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, 0.3f, 0.6f)
          .play(block.getWorld(), block.getLocation().add(0.5, 0.1, 0.5));
    }
    new TickerTask(plugin, pressedTicks) {
      @Override public void run() {
        if (block.getType().name().endsWith("PRESSURE_PLATE")) {
          BlockData blockData = block.getBlockData();
          if (blockData instanceof Powerable) {
            ((Powerable) blockData).setPowered(false);
          } else if (blockData instanceof AnaloguePowerable) {
            ((AnaloguePowerable) blockData).setPower(0);
          }
          block.setBlockData(blockData, false);
          if (playSound) {
            new SingleSound(Sound.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, 0.3f, 0.5f)
                .play(block.getWorld(), block.getLocation().add(0.5, 0.1, 0.5));
          }
        }
      }
    }.start();
    return true;
  }

  private BlockUtils() {}
}
