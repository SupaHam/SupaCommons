package com.supaham.commons.bukkit.players;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.supaham.commons.bukkit.language.Message;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.UUID;

import javax.annotation.Nonnull;

import lombok.NonNull;

/**
 * Represents a common player.
 */
public class CPlayer {

  protected final PlayerManager manager;
  protected final UUID uuid;
  
  protected String name;
  private WeakReference<Player> player = new WeakReference<>(null);
  PlayerStatus status = PlayerStatus.OFFLINE;

  public CPlayer(@Nonnull PlayerManager manager, @Nonnull Player player) {
    checkNotNull(manager, "manager cannot be null.");
    checkNotNull(player, "player cannot be null.");
    this.manager = manager;
    this.uuid = player.getUniqueId();
  }

  /**
   * Sends a message ({@link String}) with optional arguments to this player.
   *
   * @param message message to send
   * @param args arguments, optional
   */
  public void send(@NotNull String message, Object... args) {
    if (getPlayer() == null) {
      return;
    }
    getPlayer().sendMessage(String.format(message, args));
  }

  /**
   * Sends a {@link Message} with optional arguments to this player.
   *
   * @param message message to send
   * @param args arguments, optional
   */
  public void send(@NotNull Message message, Object... args) {
    if (getPlayer() == null) {
      return;
    }
    message.send(getPlayer(), args);
  }
  
  protected void connect() {
    checkState(!isOnline(), "player is already online");
    this.status = PlayerStatus.CONNECTING;
  }
  
  protected void join() {
    checkState(!isOnline(), "player is already online");
    this.status = PlayerStatus.ONLINE;
    
  }
  
  protected void disconnect() {
    checkState(isOnline(), "player is already offline");
    this.status = PlayerStatus.DISCONNECTING;
    // PlayerListener sets online to false, go there for more information.
  }

  public PlayerManager getManager() {
    return this.manager;
  }

  public UUID getUuid() {
    return this.uuid;
  }

  public String getName() {
    return name;
  }

  public Player getPlayer() {
    return player.get();
  }

  public void setPlayer(@NonNull Player player) {
    if (!player.equals(getPlayer())) {
      checkArgument(player.getUniqueId().equals(this.uuid),
                    "UUID of this player and the given Player do not match.");
      this.player = new WeakReference<>(player);
      this.name = player.getName();
    }
  }

  public boolean isOnline() {
    return !status.equals(PlayerStatus.OFFLINE);
  }
}
