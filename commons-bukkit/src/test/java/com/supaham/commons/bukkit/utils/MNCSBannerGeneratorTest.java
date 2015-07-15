package com.supaham.commons.bukkit.utils;

import com.supaham.commons.bukkit.utils.misc.MNCSBannerGenerator;

import org.bukkit.inventory.ItemStack;
import org.junit.Test;

/**
 * Created by Ali on 12/07/2015.
 */
public class MNCSBannerGeneratorTest {

  @Test
  public void testName() throws Exception {
    ItemStack itemStack = MNCSBannerGenerator
        .parseURL("http://www.needcoolshoes.com/banner?=naauhufufkmkkwoCglnnpjit");
  }
}
