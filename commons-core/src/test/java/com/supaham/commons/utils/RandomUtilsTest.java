package com.supaham.commons.utils;

import org.junit.Assert;
import org.junit.Test;

public class RandomUtilsTest {

  @Test
  public void testNextDouble() throws Exception {
    for (int i = 0; i < 10000; i++) {
      Assert.assertTrue(RandomUtils.nextDouble(10) <= 10);
      Assert.assertTrue(RandomUtils.nextDouble(20, 200) >= 20);
    }

  }
}
