package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.bukkit.SingleSound;
import com.supaham.commons.bukkit.TickerTask;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.material.TrapDoor;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Block} instances. This class contains methods such as
 * {@link #isDoor(Block)}, {@link #openDoor(Block)}, {@link #closeDoor(Block)}, and more.
 *
 * @since 0.1
 */
public final class BlockUtils {

  /**
   * Checks whether a {@link Block} is a door.
   *
   * @param block block to check
   *
   * @return whether the {@code block} is a door
   */
  public static boolean isDoor(Block block) {
    return block.getType().name().contains("DOOR");
  }

  /**
   * Checks whether a {@link Block} is the top part of a door.
   * <br/>
   * <b>NOTE: this doesn't actually test if the block is a door!</b>
   *
   * @param block block to check
   *
   * @return whether the {@code block} is the top part of a door
   */
  public static boolean isTopDoorBlock(Block block) {
    return (block.getData() & 0x8) == 0x8;
  }

  /**
   * Gets the bottom part of a door {@link Block} from a {@link Block}.
   * <br/>
   * <b>NOTE: this doesn't actually test if the block is a door!</b>
   *
   * @param block block to get bottom door part from
   *
   * @return bottom part of a door. The same {@code block} that was passed to this method is
   * returned if it is already the bottom door block.
   */
  public static Block getBottomDoorBlock(Block block) {
    return isTopDoorBlock(block) ? block.getRelative(BlockFace.DOWN) : block;
  }

  /**
   * Checks whether a door {@link Block} is closed. This method supports:
   * <br/>
   * <ul>
   * <li>{@link Material#TRAP_DOOR}</li>
   * <li>All types of two high doors</li>
   * </ul>
   *
   * <br/>
   * <b>NOTE: this doesn't actually test if the block is a door!</b>
   *
   * @param block door block to check
   *
   * @return whether the {@code block} is closed
   */
  public static boolean isDoorClosed(Block block) {
    if (!isDoor(block)) {
      return false;
    }

    if (block.getType() == Material.TRAP_DOOR) {
      TrapDoor trapdoor = (TrapDoor) block.getState().getData();
      return !trapdoor.isOpen();
    } else {
      return ((getBottomDoorBlock(block).getData() & 0x4) == 0);
    }
  }

  /**
   * Toggles a door's state (represents as a {@link Block}). If the door is open it will be closed,
   * vice versa.
   *
   * @param block block where the door is
   *
   * @return true if the door was opened, false if the door was closed
   */
  public static boolean toggleDoor(Block block) {
    boolean closed = isDoorClosed(block);
    if (closed) {
      openDoor(block);
    } else {
      closeDoor(block);
    }
    return closed;
  }

  /**
   * Toggles a door's state (represents as a {@link Block}). Unlike {@link #toggleDoor(Block)}
   * this method takes a boolean, which if set to true calls {@link #openDoor(Block)} and if set to
   * false calls {@link #closeDoor(Block)}. There is no actual automated toggling.
   *
   * @param block block where the door is
   * @param open whether to open or close the door
   */
  public static void toggleDoor(Block block, boolean open) {
    if (open) {
      openDoor(block);
    } else {
      closeDoor(block);
    }
  }

  /**
   * Opens a door {@link Block}. This method takes no action if the given block is not a door, or
   * if it is already open.
   *
   * @param block door block to open
   *
   * @return whether any action was taken. The cases in which it is false is if the block is not a
   * door, or the door is already open.
   */
  public static boolean openDoor(Block block) {
    if (!isDoor(block)) {
      return false;
    }

    if (block.getType() == Material.TRAP_DOOR) {
      BlockState state = block.getState();
      TrapDoor trapdoor = (TrapDoor) state.getData();
      trapdoor.setOpen(true);
      state.update();
      return true;
    } else {
      block = getBottomDoorBlock(block);
      if (isDoorClosed(block)) {
        block.setData((byte) (block.getData() | 0x4), true);
        block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
        return true;
      }
    }
    return false;
  }

  /**
   * Closes a door {@link Block}. This method takes no action if the given block is not a door, or
   * if it is already open.
   *
   * @param block door block to open
   *
   * @return whether any action was taken. The cases in which it is false is if the block is not a
   * door, or the door is already closed.
   */
  public static boolean closeDoor(Block block) {
    if (!isDoor(block)) {
      return false;
    }

    if (block.getType() == Material.TRAP_DOOR) {
      BlockState state = block.getState();
      TrapDoor trapdoor = (TrapDoor) state.getData();
      trapdoor.setOpen(false);
      state.update();
      return true;
    } else {
      block = getBottomDoorBlock(block);
      if (!isDoorClosed(block)) {
        block.setData((byte) (block.getData() & 0xb), true);
        block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
        return true;
      }
    }
    return false;
  }

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
   * Sets up a {@link Block} with the properties of a {@link BannerMeta}.
   *
   * @param block block to modify
   * @param bannerMeta banner metadata to apply to {@code block}
   *
   * @throws IllegalArgumentException if the {@code block} is not {@link Material#STANDING_BANNER}
   * or {@link Material#WALL_BANNER}
   */
  public static void setBlockBanner(Block block, BannerMeta bannerMeta)
      throws IllegalArgumentException {
    setBlockBanner(block.getState(), bannerMeta);
  }

  /**
   * Sets up a {@link BlockState} with the properties of a {@link BannerMeta}. This method finally
   * calls {@link BlockState#update(boolean, boolean)} with the first boolean as {@code true} and
   * the second as {@code false}, causing an update but without any physics checks.
   *
   * @param state block state to modify
   * @param bannerMeta banner metadata to apply to {@code block}
   *
   * @throws IllegalArgumentException if the {@code block} is not {@link Material#STANDING_BANNER}
   * or {@link Material#WALL_BANNER}
   */
  public static void setBlockBanner(BlockState state, BannerMeta bannerMeta) {
    Preconditions.checkArgument(state instanceof Banner, "block state is not of Banner.");
    Banner bannerState = (Banner) state;
    bannerState.setBaseColor(bannerMeta.getBaseColor());
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

    if (!MaterialUtils.equals(block.getType(), Material.GOLD_PLATE, Material.IRON_AXE,
                             Material.WOOD_PLATE, Material.STONE_PLATE)) {
      return false;
    }
    
    block.setData((byte) 1, false);
    if (playSound) {
      new SingleSound("random.click", 0.3f, 0.5f)
          .play(block.getWorld(), block.getLocation().add(0.5, 0.1, 0.5));
    }
    new TickerTask(plugin, pressedTicks) {
      @Override public void run() {
        if (MaterialUtils.equals(block.getType(), Material.GOLD_PLATE, Material.IRON_AXE,
                                 Material.WOOD_PLATE, Material.STONE_PLATE)) {
          block.setData((byte) 0, false);
          if (playSound) {
            new SingleSound("random.click", 0.3f, 0.5f)
                .play(block.getWorld(), block.getLocation().add(0.5, 0.1, 0.5));
          }
        }
      }
    }.start();
    return true;
  }

  private BlockUtils() {}
}
