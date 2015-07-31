package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;

import com.supaham.commons.bukkit.area.Region;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Represents a customizable {@link TickerTask} that's main purpose is tracking entities. The
 * entities are supplied to this task using a {@link Supplier}.
 *
 * @since 0.1
 */
public abstract class LocationChecker<T extends Entity> extends TickerTask {

  private final Region region;
  private Supplier<Collection<T>> supplier;

  /**
   * Constructs a new location checker.
   *
   * @param plugin plugin to own this task
   * @param interval how often this task should run (in minecraft ticks)
   * @param region region to check for entities in
   * @param supplier supplier of entities to call for each iteration
   */
  public LocationChecker(@Nonnull Plugin plugin, int interval, @Nonnull Region region,
                         @Nonnull Supplier<Collection<T>> supplier) {
    super(plugin, 0, interval);
    checkNotNull(region, "region cannot be null.");
    checkNotNull(supplier, "supplier cannot be null.");
    this.region = region;
    this.supplier = supplier;
  }

  /**
   * This method is called pre entity location check.
   *
   * @param entity entity that is about to be checked
   *
   * @return true to preform the entity location check, false to cancel it
   */
  protected boolean preRun(@Nonnull T entity) {
    return true;
  }

  /**
   * This method is called post entity location check.
   *
   * @param entity entity that has been checked
   */
  protected void postRun(@Nonnull T entity) {}

  /**
   * This method is called if the entity is out of boundaries.
   *
   * @param entity entity that has left the boundaries
   */
  protected abstract void onOutOfBounds(@Nonnull T entity);

  /**
   * This method is called if the entity is inside the boundaries.
   *
   * @param entity entity that is inside the boundaries
   */
  protected abstract void onInBounds(@Nonnull T entity);

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

      if (!this.region.contains(entity.getLocation().toVector())) {
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
