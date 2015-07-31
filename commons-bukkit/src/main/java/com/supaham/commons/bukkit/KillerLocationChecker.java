package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.bukkit.Language.LocationChecker.OOB_RETURNED;
import static com.supaham.commons.bukkit.Language.LocationChecker.OOB_WARN;

import com.google.common.base.Supplier;

import com.supaham.commons.bukkit.area.Region;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Represents a {@link LocationChecker} extension that kills players (set their health to 0) when
 * they are outside of the given region for too long. To override such behaviour, override {@link
 * #reachedMaxWarnings(Player)}.
 * <p />
 * <b>This task runs every 20 minecraft ticks (every second).</b>
 *
 * @since 0.1
 */
public class KillerLocationChecker extends WarnableLocationChecker<Player> {

  private Sound returnSound;
  private Sound outOfBoundsSound;

  /**
   * Constructs a new killer location checker.
   *
   * @param plugin plugin to own this task
   * @param region region to check for entities in
   * @param supplier supplier of entities to call for each iteration
   * @param maxWarnings maximum amount of warnings (seconds) a player gets before their judgement.
   */
  public KillerLocationChecker(@Nonnull Plugin plugin, @Nonnull Region region,
                               @Nonnull Supplier<Collection<Player>> supplier, int maxWarnings) {
    super(plugin, 20, region, supplier, maxWarnings);
    this.returnSound = new SingleSound(org.bukkit.Sound.NOTE_PLING, 1F, 1.5F);
    this.outOfBoundsSound = new SingleSound(org.bukkit.Sound.NOTE_BASS, 1F, .5F);
  }

  @Override protected void onInBounds(@Nonnull Player entity) {
    boolean returned = this.warnings.remove(entity) != null;
    if (returned) {
      OOB_RETURNED.send(entity);
      this.returnSound.play(entity);
    }
  }

  @Override protected void reachedMaxWarnings(@Nonnull Player entity) {
    entity.setHealth(0D);
  }

  @Override protected void warn(@Nonnull Player entity, int warnings) {
    OOB_WARN.send(entity);
    this.outOfBoundsSound.play(entity);
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
