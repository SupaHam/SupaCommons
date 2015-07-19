package com.supaham.commons.bukkit.utils;

import com.supaham.commons.bukkit.utils.BlockFaceUtils.Axis;

import org.bukkit.block.BlockFace;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ali on 19/07/2015.
 */
public class BlockFaceUtilsTest {

  @Test
  public void testAxis() throws Exception {
    Assert.assertEquals(Axis.Z, BlockFaceUtils.getAxis(BlockFace.SOUTH));
    Assert.assertEquals(Axis.Z, BlockFaceUtils.getAxis(BlockFace.NORTH));
    Assert.assertEquals(Axis.X, BlockFaceUtils.getAxis(BlockFace.WEST));
    Assert.assertEquals(Axis.X, BlockFaceUtils.getAxis(BlockFace.EAST));
    Assert.assertEquals(Axis.Y, BlockFaceUtils.getAxis(BlockFace.UP));
    Assert.assertEquals(Axis.Y, BlockFaceUtils.getAxis(BlockFace.DOWN));

  }
}
