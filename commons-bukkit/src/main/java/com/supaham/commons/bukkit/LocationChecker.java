package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;

import com.supaham.commons.bukkit.utils.VectorUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Represents a customizable {@link TickerTask} that's main purpose is tracking entities. The
 * entities are supplied to this task using a {@link Supplier}.
 *
 * @since 0.1
 */
public abstract class LocationChecker<T extends Entity> extends TickerTask {

  private final World world;
  private final Vector min;
  private final Vector max;
  private Supplier<Collection<T>> supplier;

  /**
   * Constructs a new location checker.
   *
   * @param plugin plugin to own this task
   * @param interval how often this task should run (in minecraft ticks)
   * @param min minimum point of a cuboid region
   * @param max maximum point of a cuboid region
   * @param supplier supplier of entities to call for each iteration
   */
  public LocationChecker(@Nonnull Plugin plugin, long interval, @Nonnull Location min,
                         @Nonnull Location max, @Nonnull Supplier<Collection<T>> supplier) {
    super(plugin, 0, interval);
    checkNotNull(min, "min location cannot be null.");
    checkNotNull(max, "max location cannot be null.");
    checkArgument(min.getWorld().equals(max.getWorld()), "min and max worlds don't match.");
    checkNotNull(supplier, "supplier cannot be null.");
    this.world = min.getWorld();
    this.min = Vector.getMinimum(min.toVector(), max.toVector());
    this.max = Vector.getMaximum(min.toVector(), max.toVector());
    this.supplier = supplier;
  }

  /**
   * This method is called pre entity location check.
   *
   * @param entity entity that is about to be checked
   *
   * @return true to preform the entity location check, false to cancel it
   */
  boolean preRun(@Nonnull T entity) {
    return true;
  }

  /**
   * This method is called post entity location check.
   *
   * @param entity entity that has been checked
   */
  void postRun(@Nonnull T entity) {}

  /**
   * This method is called if the entity is out of boundaries.
   *
   * @param entity entity that has left the boundaries
   */
  abstract void onOutOfBounds(@Nonnull T entity);

  /**
   * This method is called if the entity is inside the boundaries.
   *
   * @param entity entity that is inside the boundaries
   */
  abstract void onInBounds(@Nonnull T entity);

  @Override
  public void run() {
    Collection<T> entities = this.supplier.get();
    for (T entity : entities) {
      if (entity == null) {
        continue;
      }
      if (!preRun(entity)) {
        continue;
      }

      if (!VectorUtils.isWithin(entity.getLocation().toVector(), this.min, this.max)) {
        onOutOfBounds(entity);
      } else {
        onInBounds(entity);
      }

      postRun(entity);
    }
  }

  public Supplier<Collection<T>> getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier<Collection<T>> supplier) {
    this.supplier = supplier;
  }
}
