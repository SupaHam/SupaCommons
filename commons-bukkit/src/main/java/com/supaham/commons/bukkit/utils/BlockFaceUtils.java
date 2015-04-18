package com.supaham.commons.bukkit.utils;

import static org.bukkit.block.BlockFace.DOWN;
import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.SOUTH;
import static org.bukkit.block.BlockFace.UP;
import static org.bukkit.block.BlockFace.WEST;

import com.google.common.base.Preconditions;

import org.bukkit.block.BlockFace;

import java.util.Arrays;

/**
 * Utility methods for working with {@link BlockFace} instances. This class contains methods such
 * as
 * {@link #getLeft(BlockFace)}, {@link #getRight(BlockFace)}, and more.
 *
 * @since 0.2
 */
public class BlockFaceUtils {

  private static BlockFace[] adjacents = new BlockFace[]{NORTH, EAST, WEST, SOUTH, UP, DOWN};

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
   *   <li>{@link BlockFace#NORTH}</li>
   *   <li>{@link BlockFace#EAST}</li>
   *   <li>{@link BlockFace#SOUTH}</li>
   *   <li>{@link BlockFace#WEST}</li>
   *   <li>{@link BlockFace#UP}</li>
   *   <li>{@link BlockFace#DOWN}</li>
   * </ul>
   *
   * @return an array
   */
  public static BlockFace[] getAdjacents() {
    return Arrays.copyOf(adjacents, 6);
  }

  private BlockFaceUtils() {}
}
