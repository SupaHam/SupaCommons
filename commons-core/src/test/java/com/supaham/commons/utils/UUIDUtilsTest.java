package com.supaham.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Ali on 09/08/2015.
 */
public class UUIDUtilsTest {

  private static final UUID RANDOM_UUID = UUID.randomUUID();
  private static final String RANDOM_STRIPPED_UUID;
  static {
    RANDOM_STRIPPED_UUID = UUIDUtils.stripHyphens(RANDOM_UUID);
  }
  
  @Test
  public void testStripped() throws Exception {
    Assert.assertEquals(RANDOM_UUID.toString().replaceAll("-", ""), RANDOM_STRIPPED_UUID);
  }
  
  @Test
  public void testConstruct() throws Exception {
    Assert.assertEquals(RANDOM_UUID, UUIDUtils.constructUUID(RANDOM_STRIPPED_UUID));
  }
}
