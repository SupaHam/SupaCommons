package com.supaham.commons.bukkit.events;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.players.CommonPlayer;

import org.bukkit.event.Event;

import javax.annotation.Nonnull;

/**
 * Represents an {@link Event} which includes a {@link CommonPlayer}.
 *
 * @since 0.2.7
 */
public abstract class CPlayerEvent extends Event {

  private final CommonPlayer commonPlayer;

  public CPlayerEvent(@Nonnull CommonPlayer commonPlayer) {
    Preconditions.checkNotNull(commonPlayer, "player cannot be null.");
    this.commonPlayer = commonPlayer;
  }

  public CommonPlayer getCommonPlayer() {
    return commonPlayer;
  }
}
