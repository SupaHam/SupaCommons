package com.supaham.commons.bukkit.utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.TrapDoor;

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

  private BlockUtils() {}
}
