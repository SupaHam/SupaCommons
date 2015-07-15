package com.supaham.commons.bukkit.utils;

import org.bukkit.ChatColor;
import org.junit.Test;

/**
 * Created by Ali on 07/02/2015.
 */
public class ChatColorUtilsTest {

  @Test
  public void testDeserialize() throws Exception {
    System.out.println(ChatColorUtils.deserialize("ABC"));
    System.out.println(ChatColorUtils.deserialize("&1ABC"));
  }
  
  @Test
  public void testSerialize() throws Exception {
    System.out.println(ChatColorUtils.serialize("ABC"));
    System.out.println(ChatColorUtils.serialize(ChatColor.GOLD + "ABC"));
  }
}
