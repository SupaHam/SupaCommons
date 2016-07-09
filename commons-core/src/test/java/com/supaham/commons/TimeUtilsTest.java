package com.supaham.commons;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.supaham.commons.exceptions.DurationParseException;
import com.supaham.commons.utils.TimeUtils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;

/**
 * Created by Ali on 09/02/2015.
 */
public class TimeUtilsTest {

  @Test
  public void testElapsed() throws Exception {
    assertFalse(TimeUtils.elapsed(System.currentTimeMillis(), 400));
    assertTrue(TimeUtils.elapsed(System.currentTimeMillis() - 1001, 1000));
  }

  @Test
  public void testParse() throws Exception {
    long duration = TimeUtils.parseDuration("1h");
    Assert.assertEquals(3600, duration);
    duration = TimeUtils.parseDuration("1d1h1m1s");
    Assert.assertEquals(86400 + 3600 + 60 + 1, duration);
  }

  @Test
  public void testParseMs() throws Exception {
    long duration = TimeUtils.parseDurationMs("1h");
    Assert.assertEquals(3600 * 1000, duration);
    duration = TimeUtils.parseDurationMs("1d1h1m1s3ms");
    Assert.assertEquals((86400 + 3600 + 60 + 1) * 1000 + 3, duration);
    duration = TimeUtils.parseDurationMs("0.5s");
    Assert.assertEquals((0.5) * 1000, duration, 0.000001);
  }

  @Test(expected = DurationParseException.class)
  public void testParseMsError() throws Exception {
    long duration = TimeUtils.parseDurationMs("1x");
  }

  @Test
  public void testTime() throws Exception {
    Assert.assertEquals(LocalTime.MIDNIGHT, TimeUtils.parseTime("0"));
    Assert.assertEquals(LocalTime.of(1, 0), TimeUtils.parseTime("1"));
    Assert.assertEquals(LocalTime.of(1, 0), TimeUtils.parseTime("1AM"));
    Assert.assertEquals(LocalTime.of(13, 0), TimeUtils.parseTime("1PM"));

    // AM parsing
    Assert.assertEquals(LocalTime.of(0, 0), TimeUtils.parseTime("12AM"));
    Assert.assertEquals(LocalTime.of(0, 0), TimeUtils.parseTime("12:00AM"));
    Assert.assertEquals(LocalTime.of(0, 0), TimeUtils.parseTime("12:00:00AM"));
    Assert.assertEquals(LocalTime.of(0, 34), TimeUtils.parseTime("12:34AM"));
    Assert.assertEquals(LocalTime.of(0, 34, 56), TimeUtils.parseTime("12:34:56AM"));
    Assert.assertEquals(LocalTime.of(0, 0, 30), TimeUtils.parseTime("12:00:30AM"));

    // PM parsing
    Assert.assertEquals(LocalTime.of(12, 0), TimeUtils.parseTime("12PM"));
    Assert.assertEquals(LocalTime.of(12, 0), TimeUtils.parseTime("12:00PM"));
    Assert.assertEquals(LocalTime.of(12, 0), TimeUtils.parseTime("12:00:00PM"));
    Assert.assertEquals(LocalTime.of(12, 34), TimeUtils.parseTime("12:34PM"));
    Assert.assertEquals(LocalTime.of(12, 34, 56), TimeUtils.parseTime("12:34:56PM"));
    Assert.assertEquals(LocalTime.of(12, 0, 30), TimeUtils.parseTime("12:00:30PM"));

    // Insensitive letter casing
    Assert.assertEquals(LocalTime.of(0, 0, 30), TimeUtils.parseTime("12:00:30A.M."));
    Assert.assertEquals(LocalTime.of(0, 0, 30), TimeUtils.parseTime("12:00:30a.m."));
    Assert.assertEquals(LocalTime.of(0, 0, 30), TimeUtils.parseTime("12:00:30A.m."));
    Assert.assertEquals(LocalTime.of(0, 0, 30), TimeUtils.parseTime("12:00:30a.M."));

    Assert.assertEquals(LocalTime.of(12, 0, 30), TimeUtils.parseTime("12:00:30P.M."));
    Assert.assertEquals(LocalTime.of(12, 0, 30), TimeUtils.parseTime("12:00:30p.m."));
    Assert.assertEquals(LocalTime.of(12, 0, 30), TimeUtils.parseTime("12:00:30P.m."));
    Assert.assertEquals(LocalTime.of(12, 0, 30), TimeUtils.parseTime("12:00:30p.M."));

  }
}
