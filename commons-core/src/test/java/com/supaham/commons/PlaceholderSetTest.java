package com.supaham.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Ali on 28/01/2015.
 */
public class PlaceholderSetTest {

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
  public void testPlaceholderFunction() {
    PlaceholderSet<Placeholder> set = new PlaceholderSet<>();
    set.add(simplePlaceholder);
    String input = "Hi, my IGN is {pname}. However, my display name is {pdname}.";
    String expected = input.replace("{pname}", PNAME).replace("{pdname}", PDNAME);
    assertEquals(expected, set.apply(input));
  }
}
