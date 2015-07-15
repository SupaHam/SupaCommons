package com.supaham.commons.bukkit.serializers;

import com.supaham.commons.bukkit.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ali on 06/07/2015.
 */
public class ComplexItemStackSerializerTest {

  public void testName() throws Exception {
    ItemStack item = ItemBuilder.builder(Material.APPLE).name("Xorgon is so smelly right now")
        .lore("Ha. Ha. Ha.").build();
    System.out.println(new ComplexItemStackSerializer().serialize(item));
  }
}
