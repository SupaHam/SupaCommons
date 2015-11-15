package com.supaham.commons;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Function;

import com.supaham.commons.Joiner.FunctionJoiner;

import org.junit.Test;

import java.util.Arrays;

import javax.annotation.Nullable;

public class JoinerTest {

  @Test
  public void testJoiner() throws Exception {
    Joiner joiner = Joiner.on(", ");
    assertEquals("A, B", joiner.join(Arrays.asList("A", "B")));
    
    FunctionJoiner<StringWrapper> newJoiner = joiner
        .function(new Function<StringWrapper, String>() {
          @Nullable @Override public String apply(@Nullable StringWrapper input) {
            return input == null ? null : input.getString();
          }
        });
    assertEquals("A, B",
                 newJoiner.join(Arrays.asList(new StringWrapper("A"), new StringWrapper("B"))));
  }

  private class StringWrapper {

    private final String string;

    public StringWrapper(String string) {
      this.string = string;
    }

    public String getString() {
      return string;
    }

    @Override public String toString() {
      return "I'm not nice!";
    }
  }
}
