package com.supaham.commons;

import com.supaham.commons.utils.ExpiringSet;
import com.supaham.commons.utils.ExpiringSet.RemovalListener;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class ExpiringSetTest {

  @Test
  public void testExpiration() throws Exception {

    ExpiringSet<String> strings = new ExpiringSet<>(1, TimeUnit.SECONDS);

    String string = "Hi! :)";
    strings.add(string);

    Thread.sleep(1001);

    Assert.assertTrue(!strings.contains(string));
  }
  @Test
  public void testListener() throws Exception {

    final Set<String> removed = new HashSet<>();
    ExpiringSet<String> strings = new ExpiringSet<>(1, TimeUnit.SECONDS,
                                                    new RemovalListener<String>() {
                                                      @Override
                                                      public void onRemoval(@Nonnull String s) {
                                                        removed.add(s);
                                                      }
                                                    });

    String string = "Hi! :)";
    strings.add(string);

    Thread.sleep(1001);
    strings.cleanUp(); // required to update the set, alternatively you would read/write to update.

    Assert.assertTrue(removed.contains(string));
  }
}
