package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.supaham.commons.bukkit.utils.OBCUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a single sound with a volume and pitch.
 *
 * @since 0.1
 */
public class SingleSound extends Sound {

  private String sound;
  private float volume;
  private float pitch;

  /**
   * Constructs a single sound with a volume of 1 and pitch of 1.
   *
   * @param sound sound name to play
   *
   * @see #SingleSound(String, float, float)
   * @see OBCUtils#getSound(org.bukkit.Sound)
   */
  public SingleSound(@Nonnull String sound) {
    this(sound, 1f, 1f);
  }

  /**
   * Constructs a single sound.
   *
   * @param sound sound name to play
   * @param volume volume to play the sound at
   * @param pitch pitch to play the sound at
   *
   * @see OBCUtils#getSound(org.bukkit.Sound)
   */
  public SingleSound(@Nonnull String sound, float volume, float pitch) {
    checkNotNull(sound, "sound cannot be null.");
    this.sound = sound;
    this.volume = volume;
    this.pitch = pitch;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof SingleSound)) {
      return false;
    }
    SingleSound other = (SingleSound) obj;
    return sound.equals(other.sound) && volume == other.volume && pitch == other.pitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("deprecation")
  public void play(@Nonnull Player player, @Nonnull Location location) {
    player.playSound(location, this.sound, this.volume, this.pitch);
  }

  public String getSound() {
    return sound;
  }

  public void setSound(String sound) {
    this.sound = sound;
  }

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }
}
