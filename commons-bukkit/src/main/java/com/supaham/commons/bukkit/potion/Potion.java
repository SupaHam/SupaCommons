package com.supaham.commons.bukkit.potion;

import pluginbase.config.annotation.Name;

/**
 * Represents a minecraft potion, with some custom fields utilized by {@link PotionEffectManager}.
 * Each getter and setter for the custom fields say what they actually do for the manager. This
 * class supports serialization through PluginBase. 
 *
 * @see PotionEffectManager
 * @since 0.2
 */
@Name("PotionData")
public class Potion {

  // Minecraft values
  @Name("potion-id")
  private int potionId;
  private int amplifier;
  private int duration;
  private boolean ambient;
  private boolean spawningParticles;

  // Custom values utilized by the PotionEffectManager.
  private int reapplyTicks = -1;
  private boolean hidingDuration;
  private boolean deathPersistent;
  private boolean sessionPersistent;

  protected Potion() {
  }

  /**
   * Constructs a new {@link Potion} instance with the properties of a given {@link Potion}
   * instance.
   *
   * @param potion potion to copy properties from
   *
   * @see #Potion(int, int, int)
   */
  public Potion(Potion potion) {
    this(potion.potionId, potion.duration, potion.amplifier, potion.ambient,
         potion.spawningParticles);
    this.reapplyTicks = potion.reapplyTicks;
    this.hidingDuration = potion.hidingDuration;
    this.deathPersistent = potion.deathPersistent;
    this.sessionPersistent = potion.sessionPersistent;
  }

  /**
   * Constructs a new {@link Potion} instance.
   *
   * @param potionId potion id of the effect
   * @param duration duration (in ticks) the effect should last
   * @param amplifier potion amplifier, inclusive
   */
  public Potion(int potionId, int duration, int amplifier) {
    this(potionId, duration, amplifier, true);
  }

  /**
   * Constructs a new {@link Potion} instance.
   *
   * @param potionId potion id of the effect
   * @param duration duration (in ticks) the effect should last
   * @param amplifier potion amplifier, inclusive
   * @param ambient whether the potion effect should be ambient (typically produced from beacons)
   */
  public Potion(int potionId, int duration, int amplifier, boolean ambient) {
    this(potionId, duration, amplifier, ambient, true);
  }

  /**
   * Constructs a new {@link Potion} instance.
   *
   * @param potionId potion id of the effect
   * @param duration duration (in ticks) the effect should last
   * @param amplifier potion amplifier, inclusive
   * @param ambient whether the potion effect should be ambient (typically produced from beacons)
   * @param particles whether to show any particles at all
   */
  public Potion(int potionId, int duration, int amplifier, boolean ambient, boolean particles) {
    this.potionId = potionId;
    this.duration = duration;
    this.amplifier = amplifier;
    this.ambient = ambient;
    this.spawningParticles = particles;
  }

  /**
   * Returns the integral potion id.
   *
   * @return potion id
   */
  public int getPotionId() {
    return potionId;
  }

  /**
   * Sets the integral potion id.
   *
   * @param potionId potion id to set
   */
  public void setPotionId(int potionId) {
    this.potionId = potionId;
  }

  /**
   * Returns the inclusive amplifier.
   *
   * @return inclusive amplifier
   */
  public int getAmplifier() {
    return amplifier;
  }

  /**
   * Sets the inclusive amplifier.
   *
   * @param amplifier amplifier to set, inclusive
   */
  public void setAmplifier(int amplifier) {
    this.amplifier = amplifier;
  }

  /**
   * Returns the duration (in ticks).
   *
   * @return duration in ticks
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Sets the duration (in ticks).
   *
   * @param duration duration to set
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Returns whether this potion is ambient.
   *
   * @return whether this potion is ambient
   */
  public boolean isAmbient() {
    return ambient;
  }

  /**
   * Sets whether this potion is ambient. This effect is typically produced from beacons.
   *
   * @param ambient whether to make this potion ambient
   */
  public void setAmbient(boolean ambient) {
    this.ambient = ambient;
  }

  /**
   * Returns whether this potion is spawning particles.
   *
   * @return whether this potion spawns particles
   */
  public boolean isSpawningParticles() {
    return spawningParticles;
  }

  /**
   * Sets whether this potion should spawn particles.
   *
   * @param spawningParticles whether to make this potion spawn particles
   */
  public void setSpawningParticles(boolean spawningParticles) {
    this.spawningParticles = spawningParticles;
  }

  /**
   * Returns the reapply ticks. This is mainly utilized by {@link PotionEffectManager} in order to
   * provide the ability to reapply this potion over this set interval.
   *
   * @return reapply ticks
   */
  public int getReapplyTicks() {
    return reapplyTicks;
  }

  /**
   * Sets the reapply ticks. This is mainly utilized by {@link PotionEffectManager} in order to
   * provide the ability to reapply this potion over this set interval.
   *
   * @param reapplyTicks reapply ticks to set
   */
  public void setReapplyTicks(int reapplyTicks) {
    this.reapplyTicks = reapplyTicks;
  }

  /**
   * Returns whether this potion should hide the duration from the client. This functionality works
   * with {@link PotionEffectManager} by setting the actual effect duration to
   * {@link Integer#MAX_VALUE} to make the client render it as {@code **:**}.
   *
   * @return whether to hide this potion duration
   */
  public boolean isHidingDuration() {
    return hidingDuration;
  }

  /**
   * Sets whether this potion should hide the duration from the client. This functionality works
   * with {@link PotionEffectManager} by setting the actual effect duration to
   * {@link Integer#MAX_VALUE} to make the client render it as {@code **:**}.
   *
   * @param hidingDuration whether to set this potion to hide its duration
   */
  public void setHidingDuration(boolean hidingDuration) {
    this.hidingDuration = hidingDuration;
  }

  /**
   * Returns whether this potion should persist after death. This functionality works with
   * {@link PotionEffectManager} by listening to entities dying.
   *
   * @return whether this potion is persistent on death
   */
  public boolean isDeathPersistent() {
    return deathPersistent;
  }

  /**
   * Sets whether this potion should persist after death. This functionality works with
   * {@link PotionEffectManager} by listening to entities dying.
   *
   * @param deathPersistent whether to set this potion to be persistent on death
   */
  public void setDeathPersistent(boolean deathPersistent) {
    this.deathPersistent = deathPersistent;
  }

  /**
   * Returns whether this potion should persist over multiple sessions. This functionality works
   * with {@link PotionEffectManager} by listening to players logging out.
   *
   * @return whether this potion is persistent over sessions
   */
  public boolean isSessionPersistent() {
    return sessionPersistent;
  }

  /**
   * Sets whether this potion should persist over multiple sessions. This functionality works with
   * {@link PotionEffectManager} by listening to players logging out.
   *
   * @param sessionPersistent whether to set this potion to be persistent over multiple sessions
   */
  public void setSessionPersistent(boolean sessionPersistent) {
    this.sessionPersistent = sessionPersistent;
  }
}
