package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.bukkit.Language.LocationChecker.OOB_RETURNED;
import static com.supaham.commons.bukkit.Language.LocationChecker.OOB_WARN;

import com.google.common.base.Supplier;

import com.supaham.commons.bukkit.utils.OBCUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

/**
 * Represents a {@link LocationChecker} extension that kills players (set their health to 0) when
 * they are outside of the given region for too long. To override such behaviour, override {@link
 * #reachedMaxWarnings(Player)}.
 * <p />
 * <b>This task runs every 20 minecraft ticks (every second).</b>
 */
public class KillerLocationChecker extends LocationChecker<Player> {

  private final Map<Player, Integer> warnings = new WeakHashMap<>();

  private final int maxWarnings;
  private Sound returnSound;
  private Sound outOfBoundsSound;

  /**
   * Constructs a new location checker.
   *
   * @param plugin plugin to own this task
   * @param min minimum point of a cuboid region
   * @param max maximum point of a cuboid region
   * @param supplier supplier of entities to call for each iteration
   * @param maxWarnings maximum amount of warnings (seconds) a player gets before their judgement.
   */
  public KillerLocationChecker(@Nonnull Plugin plugin, @Nonnull Location min, @Nonnull Location max,
                               @Nonnull Supplier<Collection<Player>> supplier, int maxWarnings) {
    super(plugin, 20, min, max, supplier);
    checkArgument(maxWarnings > 0, "max warnings cannot be smaller than 1.");
    this.maxWarnings = maxWarnings;
    this.returnSound = new SingleSound(OBCUtils.getSound(org.bukkit.Sound.NOTE_PLING), 1F, 1.5F);
    this.outOfBoundsSound = new SingleSound(OBCUtils.getSound(org.bukkit.Sound.NOTE_BASS), 1F, .5F);
  }

  @Override
  boolean preRun(@Nonnull Player entity) {
    if (!entity.isOnline() || entity.isDead()) {
      this.warnings.remove(entity);
      return false;
    }
    return true;
  }

  @Override
  void onOutOfBounds(@Nonnull Player entity) {
    Integer warnings = this.warnings.get(entity);
    if (warnings == null) {
      warnings = 0;
    }
    warnings++;
    if (warnings == this.maxWarnings) {
      reachedMaxWarnings(entity);
      this.warnings.remove(entity);
    } else {
      warn(entity);
      this.warnings.put(entity, warnings);
    }
  }

  @Override
  void onInBounds(@Nonnull Player entity) {
    boolean returned = this.warnings.remove(entity) != null;
    if (returned) {
      OOB_RETURNED.send(entity);
      this.returnSound.play(entity);
    }
  }

  void reachedMaxWarnings(@Nonnull Player entity) {
    entity.setHealth(0D);
  }

  void warn(@Nonnull Player entity) {
    OOB_WARN.send(entity);
    this.outOfBoundsSound.play(entity);
  }

  public Map<Player, Integer> getWarnings() {
    return warnings;
  }

  public int getMaxWarnings() {
    return maxWarnings;
  }

  public Sound getReturnSound() {
    return returnSound;
  }

  public void setReturnSound(@Nonnull Sound returnSound) {
    checkNotNull(returnSound, "sound cannot be null.");
    this.returnSound = returnSound;
  }

  public Sound getOutOfBoundsSound() {
    return outOfBoundsSound;
  }

  public void setOutOfBoundsSound(@Nonnull Sound outOfBoundsSound) {
    checkNotNull(outOfBoundsSound, "sound cannot be null.");
    this.outOfBoundsSound = outOfBoundsSound;
  }
}
