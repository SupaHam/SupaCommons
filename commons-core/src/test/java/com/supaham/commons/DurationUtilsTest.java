package com.supaham.commons;

import static com.supaham.commons.utils.DurationUtils.parseDuration;

import com.supaham.commons.utils.DurationUtils;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

/**
 * Created by Ali on 09/02/2015.
 */
public class DurationUtilsTest {

  @Test
  public void testParse() throws Exception {
    Duration duration = parseDuration("1h");
    Assert.assertEquals(3600, duration.getSeconds());
  }

  @Test
  public void testToString() throws Exception {
    Assert.assertEquals("1h", DurationUtils.toString(Duration.ofSeconds(3600), true));
    Assert.assertEquals("1 hour", DurationUtils.toString(Duration.ofSeconds(3600), false));
  }

  @Test
  public void testRandomDuration() throws Exception {
    Duration d1 = Duration.ofMillis(0);
    Duration d2 = Duration.ofMillis(1000);
    for (int i = 0; i < 10000; i++) {
      Duration randomDuration = DurationUtils.randomDuration(d1, d2);
      Assert.assertTrue(randomDuration.toMillis() >= d1.toMillis()
                        && randomDuration.toMillis() <= d2.toMillis());  
    }
  }
}
