package com.supaham.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.supaham.commons.utils.ArrayUtils;

import org.junit.Test;

public class ArrayUtilsTest {
  
  @Test
  public void removeEmptyStringNoTrim() {
    String[] array = new String[]{"abc", " ", " def    "};
    assertArrayEquals(new String[]{"abc", " ", " def    "}, ArrayUtils
        .removeEmptyStrings(array, false));
  }
  
  @Test
  public void removeEmptyStringTrim() {
    String[] array = new String[]{"abc", " ", " def    "};
    assertArrayEquals(new String[]{"abc", " def    "}, ArrayUtils.removeEmptyStrings(array, true));
  }
  
  @Test
  public void getTotalStringsLengthNoTrim() {
    String[] array = new String[]{"abc", " ", " def    "}; // 3, 1, 8
    assertEquals(12, ArrayUtils.getTotalStringsLength(array, false));
  }
  
  @Test
  public void getTotalStringsLengthTrim() {
    String[] array = new String[]{"abc", " ", " def    "}; // 3, 0, 8
    assertEquals(11, ArrayUtils.getTotalStringsLength(array));
  }
  
  @Test
  public void checkForNullElements() {
    String[] array = new String[]{"abc", ""};
    ArrayUtils.checkForNullElements(array);
  }
  
  @Test(expected = NullPointerException.class)
  public void checkForNullElements2() {
    String[] array = new String[]{"abc", null};
    ArrayUtils.checkForNullElements(array);
  }
}
