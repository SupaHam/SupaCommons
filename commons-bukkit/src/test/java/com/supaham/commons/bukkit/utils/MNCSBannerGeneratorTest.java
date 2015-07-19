package com.supaham.commons.bukkit.utils;

import com.supaham.commons.bukkit.utils.misc.MNCSBannerGenerator;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Ali on 12/07/2015.
 */
public class MNCSBannerGeneratorTest {

  // @Test can't test
  public void testName() throws Exception {
    ItemStack itemStack = MNCSBannerGenerator
        .parseURL("http://www.needcoolshoes.com/banner?=naauhufufkmkkwoCglnnpjit");
  }
}
