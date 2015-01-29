package com.supaham.commons.placeholders;

import static com.supaham.commons.placeholders.SimplePlaceholderTest.PDNAME;
import static com.supaham.commons.placeholders.SimplePlaceholderTest.PNAME;
import static com.supaham.commons.placeholders.SimplePlaceholderTest.SIMPLE_PLACEHOLDER;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created by Ali on 28/01/2015.
 */
public class PlaceholderSetTest {

  @Test
  public void testPlaceholderFunction() {
    PlaceholderSet<Placeholder> set = new PlaceholderSet<>();
    set.add(SIMPLE_PLACEHOLDER);
    String input = "Hi, my IGN is {pname}. However, my display name is {pdname}.";
    String expected = input.replace("{pname}", PNAME).replace("{pdname}", PDNAME);
    assertEquals(expected, set.apply(input));
  }
}
