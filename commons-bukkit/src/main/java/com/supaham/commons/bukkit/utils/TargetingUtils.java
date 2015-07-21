package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TargetingUtils {

  public static Block getTargetBlock(@Nullable Set<Material> transparent, 
                                     @Nonnull Location location, int maxDistance) {
    Preconditions.checkNotNull(location, "location cannot be null.");
    if (maxDistance > 120) {
      maxDistance = 120;
    }
    
    Block result = null;
    BlockIterator itr = new BlockIterator(location, 0, maxDistance);
    while (itr.hasNext()) {
      result = itr.next();
      Material material = result.getType();
      if (transparent == null) {
        if (!material.equals(Material.AIR)) {
          break;
        }
      } else if (!transparent.contains(material)) {
        break;
      }
    }
    return result;
  }

  private TargetingUtils() {
    throw new AssertionError("Why are you... targeting... me? ayyylmao.");
  }
}
