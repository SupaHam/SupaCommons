package com.supaham.commons.placeholders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Ali on 28/01/2015.
 */
public class SimplePlaceholderTest {

  protected static final String PNAME = "SupaHam";
  protected static final String PDNAME = "[Admin] SupaHam";
  protected static final String WORLD = "That World";
  protected static final String NON_EXISTANT = "non-existent placeholder";

  protected static final Placeholder SIMPLE_PLACEHOLDER = new SimplePlaceholder("pname", "pdname",
                                                                                "world") {
    private List<String> matched = new ArrayList<>();

    @Nullable
    @Override
    public String apply(PlaceholderData data) {
      String placeholder = data.getPlaceholder();
      String matched;
      switch (placeholder.toLowerCase()) {
        case "pname":
          matched = PNAME;
          break;
        case "pdname":
          matched = PDNAME;
          break;
        case "world":
          matched = WORLD;
          break;
        default:
          return null;
      }
      this.matched.add(placeholder);
      return matched;
    }

    @Override
    public void onComplete(String string) {
      System.out.println("We've successfully replaced " + this.matched + " in, \"" + string + "\"");
    }
  };

  @Test
  public void applies() {
    try {
      assertEquals(PNAME, SIMPLE_PLACEHOLDER.apply(PlaceholderData.create("pname")));
      assertEquals(PDNAME, SIMPLE_PLACEHOLDER.apply(PlaceholderData.create("pdname")));
      assertEquals(WORLD, SIMPLE_PLACEHOLDER.apply(PlaceholderData.create("world")));

      assertNotEquals(PNAME, SIMPLE_PLACEHOLDER.apply(PlaceholderData.create("pdname")));

      assertNull(SIMPLE_PLACEHOLDER.apply(PlaceholderData.create(NON_EXISTANT)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void appliesOnComplete() {
    try {
      String message = "";
      for (String s : "Hello, pname . Your display name is pdname ".split(" ")) {
        String apply = SIMPLE_PLACEHOLDER.apply(PlaceholderData.create(s));
        if (apply != null) {
          s = apply;
        }
        message += s + " ";
      }
      SIMPLE_PLACEHOLDER.onComplete(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void isPlaceHolder() {
    assertTrue(SIMPLE_PLACEHOLDER.isPlaceholder("pname"));
    assertTrue(SIMPLE_PLACEHOLDER.isPlaceholder("pdname"));
    assertTrue(SIMPLE_PLACEHOLDER.isPlaceholder("world"));
    assertFalse(SIMPLE_PLACEHOLDER.isPlaceholder(NON_EXISTANT));
  }

  @Test
  public void testPlaceholderFunction() {
    PlaceholderFunction function = new PlaceholderFunction() {
      @Override
      Collection<Placeholder> getPlaceholders() {
        return Arrays.asList(SIMPLE_PLACEHOLDER);
      }
    };
    String input = "Hi, my IGN is {pname}. However, my display name is {pdname} {asd}.";
    String expected = input.replace("{pname}", PNAME).replace("{pdname}", PDNAME);
    assertEquals(expected, function.apply(input));
  }
}
