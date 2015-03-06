package com.supaham.commons;

import static com.supaham.commons.utils.DurationUtils.parseDuration;

import com.supaham.commons.utils.DurationUtils;

import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ali on 09/02/2015.
 */
public class DurationUtilsTest {

  @Test
  public void testParse() throws Exception {
    Duration duration = parseDuration("1h");
    Assert.assertEquals(3600, duration.getStandardSeconds());
  }

  @Test
  public void testToString() throws Exception {
    Assert.assertEquals("1h", DurationUtils.toString(Duration.standardSeconds(3600), true));
    Assert.assertEquals("1 hour", DurationUtils.toString(Duration.standardSeconds(3600), false));
  }

  @Test
  public void testRandomDuration() throws Exception {
    Duration d1 = new Duration(0);
    Duration d2 = new Duration(1000);
    for (int i = 0; i < 10000; i++) {
      Duration randomDuration = DurationUtils.randomDuration(d1, d2);
      Assert.assertTrue(randomDuration.getMillis() >= d1.getMillis()
                        && randomDuration.getMillis() <= d2.getMillis());  
    }
  }
}
