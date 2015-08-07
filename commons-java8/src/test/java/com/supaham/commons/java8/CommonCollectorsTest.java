package com.supaham.commons.java8;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ali on 07/08/2015.
 */
public class CommonCollectorsTest {

  @Test
  public void testReverseOrder() throws Exception {
    List<Integer> original = Arrays.asList(1, 3, 2, 6, 9, 7, 5, 4, 8);
    List<Integer> reversed = original.stream().collect(CommonCollectors.inReverse());
    for (int i = 0; i < original.size(); i++) {
      Assert.assertEquals(reversed.get(i), original.get((original.size() - 1) - i));
    }
  }
}
