package com.supaham.commons.placeholders;

import static com.supaham.commons.placeholders.SimplePlaceholderTest.PDNAME;
import static com.supaham.commons.placeholders.SimplePlaceholderTest.PNAME;
import static com.supaham.commons.placeholders.SimplePlaceholderTest.SIMPLE_PLACEHOLDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ali on 28/01/2015.
 */
public class PlaceholderSetTest {
  
  PlaceholderSet<Placeholder> set = new PlaceholderSet<>();
  
  @Before
  public void before() {
    set.add(SIMPLE_PLACEHOLDER);
  }

  @Test
  public void testPlaceholderFunction() {
    String input = "Hi, my IGN is {pname}. However, my display name is {pdname}.";
    String expected = input.replace("{pname}", PNAME).replace("{pdname}", PDNAME);
    assertEquals(expected, set.apply(input));
  }
  
  @Test
  public void testContains() {
    assertTrue(set.contains(SIMPLE_PLACEHOLDER));
  }
  
  @Test
  public void testRemove() {
    PlaceholderSet<Placeholder> set = new PlaceholderSet<>();
    set.remove(SIMPLE_PLACEHOLDER);
    assertFalse(set.contains(SIMPLE_PLACEHOLDER));
  }
}
