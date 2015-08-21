package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Supplier;

import com.supaham.commons.bukkit.area.Region;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

/**
 * Represents a {@link LocationChecker} extension that kills players (set their health to 0) when
 * they are outside of the given region for too long. To override such behaviour, override {@link
 * #reachedMaxWarnings(T)}.
 * <p />
 * <b>This task runs every 20 minecraft ticks (every second).</b>
 *
 * @since 0.3.1
 */
public abstract class WarnableLocationChecker<T extends Entity> extends LocationChecker<T> {

  private final int maxWarnings;
  protected final Map<T, Integer> warnings = new WeakHashMap<>();

  /**
   * Constructs a new warnable location checker.
   *
   * @param plugin plugin to own this task
   * @param interval interval (in minecraft ticks) to run this task at
   * @param region region to check for entities in
   * @param supplier supplier of entities to call for each iteration
   * @param maxWarnings maximum amount of warnings (seconds) a player gets before their judgement.
   */
  public WarnableLocationChecker(@Nonnull Plugin plugin, int interval, @Nonnull Region region,
                                 @Nonnull Supplier<Collection<T>> supplier, int maxWarnings) {
    super(plugin, interval, region, supplier);
    checkArgument(maxWarnings > 0, "max warnings cannot be smaller than 1.");
    this.maxWarnings = maxWarnings;
  }

  @Override protected boolean preRun(@Nonnull T entity) {
    if (entity.isDead() || (entity instanceof Player && !((Player) entity).isOnline())) {
      this.warnings.remove(entity);
      return false;
    }
    return true;
  }

  @Override protected void onOutOfBounds(@Nonnull T entity) {
    Integer warnings = this.warnings.get(entity);
    if (warnings == null) {
      warnings = 0;
    }
    if (warnings == this.maxWarnings) {
      reachedMaxWarnings(entity);
    } else {
      if (!warn(entity, warnings)) {
        return;
      }
      warnings++;
      this.warnings.put(entity, warnings);
    }
  }

  @Override protected void onInBounds(@Nonnull T entity) {
    this.warnings.remove(entity);
  }

  protected void reachedMaxWarnings(@Nonnull T entity) {
    this.warnings.remove(entity);
  }

  protected abstract boolean warn(@Nonnull T entity, int warnings);

  public Map<T, Integer> getWarnings() {
    return warnings;
  }

  public int getMaxWarnings() {
    return maxWarnings;
  }
}
