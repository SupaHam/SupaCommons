package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Event} instances. This class contains methods such as
 * {@link #getLivingEntityDamager(EntityDamageByEntityEvent)} and more.
 *
 * @since 0.1
 */
public class EventUtils {

  /**
   * Helper method for calling an {@link Event} and returning it.
   *
   * @param event event to call
   * @param <T> event type
   *
   * @return same exact {@code event} instance after it is called
   */
  public static <T extends Event> T callEvent(T event) {
    Bukkit.getServer().getPluginManager().callEvent(event);
    return event;
  }

  /**
   * Gets the {@link LivingEntity} from a {@link EntityDamageByEntityEvent}. If the {@link
   * EntityDamageByEntityEvent#getDamager()} is a LivingEntity, then that instance is returned.
   * Otherwise, if the damager is a projectile and it's shooter is a LivingEntity, that
   * LivingEntity
   * shooter instance is returned.
   *
   * @param event event to get LivingEntity from
   *
   * @return instance of {@link LivingEntity}, nullable
   */
  public static LivingEntity getLivingEntityDamager(@Nonnull EntityDamageByEntityEvent event) {
    checkNotNull(event, "event cannot be null.");
    // This is the living entity
    if (event.getDamager() instanceof LivingEntity) {
      return ((LivingEntity) event.getDamager());
    }
    // Check for projectile's shooter
    if (event.getDamager() instanceof Projectile) {
      ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
      if (shooter != null && shooter instanceof LivingEntity) {
        return ((LivingEntity) shooter);
      }
    }
    return null;
  }
}
