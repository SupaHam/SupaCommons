package com.supaham.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.supaham.commons.placeholders.Placeholder;
import com.supaham.commons.placeholders.PlaceholderFunction;
import com.supaham.commons.placeholders.SimplePlaceholder;

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

  private static final String PNAME = "SupaHam";
  private static final String PDNAME = "[Admin] SupaHam";
  private static final String WORLD = "That World";
  private static final String NON_EXISTANT = "non-existent placeholder";

  private Placeholder simplePlaceholder = new SimplePlaceholder("pname", "pdname", "world") {

    private List<String> matched = new ArrayList<>();

    @Nullable
    @Override
    public String apply(String placeholder) {
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
      assertEquals(PNAME, simplePlaceholder.apply("pname"));
      assertEquals(PDNAME, simplePlaceholder.apply("pdname"));
      assertEquals(WORLD, simplePlaceholder.apply("world"));

      assertNotEquals(PNAME, simplePlaceholder.apply("pdname"));

      assertNull(simplePlaceholder.apply(NON_EXISTANT));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void appliesOnComplete() {
    try {
      String message = "";
      for (String s : "Hello, pname . Your display name is pdname ".split(" ")) {
        String apply = simplePlaceholder.apply(s);
        if (apply != null) {
          s = apply;
        }
        message += s + " ";
      }
      simplePlaceholder.onComplete(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void isPlaceHolder() {
    assertTrue(simplePlaceholder.isPlaceholder("pname"));
    assertTrue(simplePlaceholder.isPlaceholder("pdname"));
    assertTrue(simplePlaceholder.isPlaceholder("world"));
    assertFalse(simplePlaceholder.isPlaceholder(NON_EXISTANT));
  }

  @Test
  public void testPlaceholderFunction() {
    PlaceholderFunction function = new PlaceholderFunction() {
      @Override
      Collection<Placeholder> getPlaceholders() {
        return Arrays.asList(simplePlaceholder);
      }
    };
    String input = "Hi, my IGN is {pname}. However, my display name is {pdname} {asd}.";
    String expected = input.replace("{pname}", PNAME).replace("{pdname}", PDNAME);
    assertEquals(expected, function.apply(input));
  }
}
