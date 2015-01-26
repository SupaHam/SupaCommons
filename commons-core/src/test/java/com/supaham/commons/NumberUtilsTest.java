package com.supaham.commons;

import static org.junit.Assert.assertEquals;

import com.supaham.commons.utils.NumberUtils;

import org.junit.Test;

public class NumberUtilsTest {
  
  @Test
  public void roundExact() {
    assertEquals("1.", NumberUtils.roundExact(-1, 1.23D));
    assertEquals("1.", NumberUtils.roundExact(0, 1.23D));
    assertEquals("1.2", NumberUtils.roundExact(1, 1.23D));
    assertEquals("1.23", NumberUtils.roundExact(2, 1.23D));
    assertEquals("1.", NumberUtils.roundExact(-1, 1.23F));
    assertEquals("1.", NumberUtils.roundExact(0, 1.23F));
    assertEquals("1.2", NumberUtils.roundExact(1, 1.23F));
    assertEquals("1.23", NumberUtils.roundExact(2, 1.23F));
  }
}
