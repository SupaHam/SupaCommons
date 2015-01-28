package com.supaham.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Ali on 28/01/2015.
 */
public class SimplePlaceholderTest {

  private SimplePlaceholder simplePlaceholder = new SimplePlaceholder("pname", "pdname", "world") {

    private List<String> matched = new ArrayList<>();

    @Nullable
    @Override
    public String call(String placeholder) throws Exception {
      String matched;
      switch (placeholder.toLowerCase()) {
        case "pname":
          matched = "SupaHam";
          break;
        case "pdname":
          matched = "[Admin] SupaHam";
          break;
        case "world":
          matched = "That World";
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
  public void calls() {
    try {
      assertEquals("SupaHam", simplePlaceholder.call("pname"));
      assertEquals("[Admin] SupaHam", simplePlaceholder.call("pdname"));
      assertEquals("That World", simplePlaceholder.call("world"));

      assertNotEquals("SupaHam", simplePlaceholder.call("pdname"));

      assertNull(simplePlaceholder.call("non-existent placeholder"));
    } catch (Exception e) {
      e.printStackTrace();
    }   
  }

  @Test
  public void callsOnComplete() {
    try {
      String message = "";
      for (String s : "Hello, pname . Your display name is pdname ".split(" ")) {
        String call = simplePlaceholder.call(s);
        if (call != null) {
          s = call;
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
    assertFalse(simplePlaceholder.isPlaceholder("non-existent placeholder"));
  }
}
