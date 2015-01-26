package com.supaham.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.supaham.commons.utils.StringUtils;

import org.junit.Test;

public class StringUtilsTest {
  
  @Test
  public void asciiASCIIString() {
    assertTrue(StringUtils.isASCII("abc"));
  }
  
  @Test
  public void asciiUTF8String() {
    assertFalse(StringUtils.isASCII("abc§"));
  }
  
  @Test(expected = NullPointerException.class)
  public void asciiNullString() {
    assertFalse(StringUtils.isASCII(null));
  }
  
  @Test
  public void testFormatWhitespace() {
    assertEquals("test", StringUtils.formatWhitespace(32, "test"));
    assertEquals("test", StringUtils.formatWhitespace(32, "test", " "));
    assertEquals("test test", StringUtils.formatWhitespace(9, "test", "test"));
    assertEquals("test  test", StringUtils.formatWhitespace(10, "test", "test"));
    assertEquals("test      test", StringUtils.formatWhitespace(14, "test", "    ", "test"));
    assertEquals("test   t  test", StringUtils.formatWhitespace(14, "test", "  t ", "test"));
  }
}
