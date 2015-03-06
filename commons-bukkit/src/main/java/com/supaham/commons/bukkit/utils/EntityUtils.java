package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Entity} instances. This class contains methods such as
 * {@link #getFreeLocation(LivingEntity)}, and more.
 *
 * @since 0.1
 */
public class EntityUtils {

  /**
   * Returns a free safe location above or at the given {@link LivingEntity}'s location.
   *
   * @param entity entity to get free space for
   *
   * @return a location with the required free space
   *
   * @see LocationUtils#getFreeLocation(Location, double)
   */
  public static Location getFreeLocation(@Nonnull LivingEntity entity) {
    checkNotNull(entity, "entity cannot be null.");
    return LocationUtils.getFreeLocation(entity.getLocation(), entity.getEyeHeight(true));
  }
}
