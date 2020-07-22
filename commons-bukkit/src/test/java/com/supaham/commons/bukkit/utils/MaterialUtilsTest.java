package com.supaham.commons.bukkit.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.Material;
import org.junit.Test;

public class MaterialUtilsTest {
  @Test
  public void isInteractable() {
    assertTrue(MaterialUtils.isInteractableBlock(Material.ANVIL));
    assertFalse(MaterialUtils.isInteractableBlock(Material.AIR));
  }
  
  @Test
  public void isContainer() {
    assertTrue(MaterialUtils.isContainer(Material.CHEST));
    assertTrue(MaterialUtils.isContainer(Material.DISPENSER));
    assertTrue(MaterialUtils.isContainer(Material.CHEST_MINECART));
  }


  @Test
  public void isSign() {
    assertTrue(MaterialUtils.isSign(Material.ACACIA_SIGN));
    assertTrue(MaterialUtils.isSign(Material.ACACIA_WALL_SIGN));
    assertFalse(MaterialUtils.isSign(Material.DIRT));
  }
}
