package com.supaham.commons.bukkit.utils;

import static com.supaham.commons.bukkit.utils.LocationUtils.serialize;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Ali on 07/02/2015.
 */
public class LocationUtilsTest {

  @Test
  public void testSerialize() throws Exception {
    World world = Mockito.mock(World.class);
    Mockito.when(world.getName()).thenReturn("world");
    
    // test with no direction
    String serialize = serialize(new Location(world, 123D, 64D, 123.5D));
    Assert.assertEquals("world 123,64,123.5", serialize);
    
    // test yaw
    serialize = serialize(new Location(world, 123D, 64D, 123.5D, 1F, 0F));
    Assert.assertEquals("world 123,64,123.5 1", serialize);
    
    // test yaw with pitch
    serialize = serialize(new Location(world, 123D, 64D, 123.5D, 1F, 123F));
    Assert.assertEquals("world 123,64,123.5 1 123", serialize);
    
    // Test with no yaw and pitch
    serialize = serialize(new Location(world, 123D, 64D, 123.5D, 0F, 123F));
    Assert.assertEquals("world 123,64,123.5 0 123", serialize);
  }
}
