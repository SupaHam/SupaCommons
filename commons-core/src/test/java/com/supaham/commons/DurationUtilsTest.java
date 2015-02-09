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
}
