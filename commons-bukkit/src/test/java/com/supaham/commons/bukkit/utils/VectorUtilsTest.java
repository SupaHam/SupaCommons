package com.supaham.commons.bukkit.utils;

import static com.supaham.commons.bukkit.utils.VectorUtils.deserializeRelative;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.bukkit.util.Vector;
import org.junit.Test;

/**
 * Created by Ali on 22/08/2015.
 */
public class VectorUtilsTest {

  @Test
  public void testDeserialize() throws Exception {
    assertEquals(new Vector(1.22, 2, 3), VectorUtils.deserialize("1.22, 2, 3"));
    assertEquals("1,2,3", VectorUtils.serialize(new Vector(1, 2, 3)));
  }

  @Test
  public void testRelative() throws Exception {
    assertEquals(new RelativeVector(1, 2, 3, false, false, false), deserializeRelative("1,2,3"));
    assertEquals(new RelativeVector(1, 2, 3, true, false, false), deserializeRelative("~1,2,3"));
    assertEquals(new RelativeVector(1, 2, 3, true, true, false), deserializeRelative("~1,~2,3"));
    assertEquals(new RelativeVector(1, 2, 3, true, true, true), deserializeRelative("~1,~2,~3"));
    assertEquals(new RelativeVector(1, 2, 3, true, false, true), deserializeRelative("~1,2,~3"));
    assertEquals(new ImmutableVector(1, 2, 3), new RelativeVector(1, 2, 3, false, false, false));
    assertNotEquals(new RelativeVector(1, 2, 4, false, false, true), // z is 4
                    new RelativeVector(1, 2, 3, false, false, true)); // z is 3

    assertEquals(new ImmutableVector(2, 3, 4), new RelativeVector(1, 1, 1, true, true, true)
        .with(new ImmutableVector(1, 2, 3))); // original vector is 1,2,3; add 1,1,1, should equal 2,3,4.
    assertEquals(new ImmutableVector(0, 1, 2), new RelativeVector(-1, -1, -1, true, true, true)
        .with(new ImmutableVector(1, 2, 3))); // original vector is 1,2,3; subtract 1,1,1, should equal 0,1,2.

  }
}
