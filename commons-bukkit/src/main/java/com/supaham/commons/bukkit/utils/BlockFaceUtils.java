package com.supaham.commons.bukkit.utils;

import static org.bukkit.block.BlockFace.DOWN;
import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.SOUTH;
import static org.bukkit.block.BlockFace.UP;
import static org.bukkit.block.BlockFace.WEST;

import com.google.common.base.Preconditions;

import com.supaham.commons.utils.ArrayUtils;

import org.bukkit.block.BlockFace;

import java.util.Arrays;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link BlockFace} instances. This class contains methods such
 * as
 * {@link #getLeft(BlockFace)}, {@link #getRight(BlockFace)}, and more.
 *
 * @since 0.2
 */
public class BlockFaceUtils {

  private static final BlockFace[] adjacents = new BlockFace[]{NORTH, EAST, WEST, SOUTH, UP, DOWN};

  /**
   * Returns whether a {@link BlockFace} is vertical. That is, {@link BlockFace#UP} or
   * {@link BlockFace#DOWN}.
   *
   * @param face block face to check
   *
   * @return true if the {@code face} is vertical
   *
   * @see #isHorizontal(BlockFace)
   */
  public static boolean isVertical(BlockFace face) {
    return face == UP || face == DOWN;
  }

  /**
   * Returns whether a {@link BlockFace} is horizontal. That is, NOT {@link BlockFace#UP} or
   * {@link BlockFace#DOWN}.
   *
   * @param face block face to check
   *
   * @return true if the {@code face} is horizontal
   *
   * @see #isVertical(BlockFace)
   */
  public static boolean isHorizontal(BlockFace face) {
    return !isVertical(face);
  }

  /**
   * Gets the left side of a {@link BlockFace}. This is a strict 90 degree anti-clockwise rotation.
   *
   * @param face block face to rotate
   *
   * @return rotated block face
   */
  public static BlockFace getLeft(BlockFace face) {
    Preconditions.checkArgument(isHorizontal(face), "block face must be horizontal.");
    return BlockFace.values()[(face.ordinal() + 3) % 4]; // n -> w -> s -> e -> n
  }

  /**
   * Gets the right side of a {@link BlockFace}. This is a strict 90 degree clockwise rotation.
   *
   * @param face block face to rotate
   *
   * @return rotated block face
   */
  public static BlockFace getRight(BlockFace face) {
    Preconditions.checkArgument(isHorizontal(face), "block face must be horizontal.");
    return BlockFace.values()[(face.ordinal() + 1) % 4]; // n -> e -> s -> w -> n
  }

  /**
   * Returns an array of adjacent {@link BlockFace}s. List:
   * <p />
   * <ul>
   * <li>{@link BlockFace#NORTH}</li>
   * <li>{@link BlockFace#EAST}</li>
   * <li>{@link BlockFace#SOUTH}</li>
   * <li>{@link BlockFace#WEST}</li>
   * <li>{@link BlockFace#UP}</li>
   * <li>{@link BlockFace#DOWN}</li>
   * </ul>
   *
   * @return an array
   */
  public static BlockFace[] getAdjacents() {
    return Arrays.copyOf(adjacents, 6);
  }

  /**
   * Returns the axis a {@link BlockFace} is on.
   *
   * @param blockFace block face to get axis from
   *
   * @return axis the {@code blockFace} lies upon
   */
  public static Axis getAxis(@Nonnull BlockFace blockFace) {
    Preconditions.checkNotNull(blockFace, "block face cannot be null.");
    Preconditions.checkArgument(ArrayUtils.contains(adjacents, blockFace),
                                "block face has multiple axis.");
    return blockFace.getModY() != 0 ? Axis.Y : blockFace.getModX() != 0 ? Axis.X : Axis.Z;
  }

  public static float getYaw(@Nonnull BlockFace blockFace) {
    Preconditions.checkNotNull(blockFace, "block face cannot be null.");
    Preconditions.checkArgument(isHorizontal(blockFace), "block face must be horizontal.");
    switch (blockFace) {
      case SOUTH:
        return 0;
      case WEST:
        return 90;
      case NORTH:
        return 180;
      case EAST:
        return 270;
      default:
        return -1; // impossible
    }
  }

  /**
   * Represents an axis in a 3D space.
   */
  public enum Axis {
    X, Y, Z
  }

  private BlockFaceUtils() {}
}
