package com.supaham.commons.bukkit.players;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Preconditions;

import com.supaham.commons.Uuidable;
import com.supaham.commons.bukkit.language.Message;
import com.supaham.commons.bukkit.title.Title;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.UUID;

import javax.annotation.Nonnull;


/**
 * Represents a common player.
 *
 * @since 0.2.7
 */
public class CommonPlayer implements Uuidable {

  protected final UUID uuid;

  private String name;
  private WeakReference<Player> player = new WeakReference<>(null);
  PlayerStatus status = PlayerStatus.OFFLINE;

  public CommonPlayer(@Nonnull Player player) {
    checkNotNull(player, "player cannot be null.");
    this.uuid = player.getUniqueId();
    setPlayer(player);
  }

  @Override public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    } else if (obj instanceof CommonPlayer) {
      return uuid.equals(((CommonPlayer) obj).uuid);
    }
    return false;
  }

  @Override public int hashCode() {
    return this.uuid.hashCode();
  }

  /**
   * Sends a message ({@link String}) with optional arguments to this player.
   *
   * @param message message to send
   * @param args arguments, optional
   */
  public void message(@Nonnull String message, Object... args) {
    Preconditions.checkNotNull(message, "message cannot be null.");
    if (getPlayer() != null) {
      getPlayer().sendMessage(String.format(message, args));
    }
  }

  /**
   * Sends a {@link Message} with optional arguments to this player.
   *
   * @param message message to send
   * @param args arguments, optional
   */
  public void message(@Nonnull Message message, Object... args) {
    Preconditions.checkNotNull(message, "message cannot be null.");
    if (getPlayer() != null) {
      message.send(getPlayer(), args);
    }
  }

  public void message(@Nonnull BaseComponent component) {
    Preconditions.checkNotNull(component, "component cannot be null.");
    if (getPlayer() != null) {
      getPlayer().spigot().sendMessage(component);
    }
  }

  public void message(@Nonnull BaseComponent[] components) {
    Preconditions.checkNotNull(components, "component cannot be null.");
    if (getPlayer() != null) {
      getPlayer().spigot().sendMessage(components);
    }
  }

  protected void connect() {
    checkState(!isOnline(), "player is already online.");
    this.status = PlayerStatus.CONNECTING;
  }

  protected void join() {
    checkState(!isOnline(), "player is already online.");
    this.status = PlayerStatus.ONLINE;

  }

  protected void disconnect() {
    checkState(isOnline(), "player is already offline.");
    this.status = PlayerStatus.DISCONNECTING;
    // PlayerListener sets online to false, go there for more information.
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

  public void setPlayer(@Nonnull Player player) {
    Preconditions.checkNotNull(player, "player cannot be null.");
    if (player != getPlayer()) { // Only set if we have to
      checkArgument(player.getUniqueId().equals(this.uuid), "UUID of this player and the given Player do not match.");
      this.player = new WeakReference<>(player);
      this.name = player.getName();
    }
  }

  public boolean isOnline() {
    return !status.equals(PlayerStatus.OFFLINE);
  }

  public PlayerStatus getStatus() {
    return status;
  }

  protected void setStatus(@Nonnull PlayerStatus status) {
    Preconditions.checkNotNull(status, "status cannot be null.");
    this.status = status;
  }

  /**
   * @see Title#sendActionBarMessage(Player, BaseComponent)
   * @deprecated Use the Spigot API.
   */
  public void sendActionBar(@Nonnull BaseComponent messagePart) {
    getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, messagePart);
  }

  /**
   * @see Title#sendTimes(Player, int, int, int)
   * @deprecated Use the Spigot API.
   */
  public void sendTitleTimes(int fadeIn, int stay, int fadeOut) {
    Title.sendTimes(getPlayer(), fadeIn, stay, fadeOut);
  }

  /**
   * @see Title#sendTitle(Player, BaseComponent)
   * @deprecated Use the Spigot API.
   */
  public void sendTitle(@Nonnull BaseComponent title) {
    Title.sendTitle(getPlayer(), title);
  }

  public void sendTitle(@Nonnull BaseComponent title, @Nonnull BaseComponent subtitle, int fadeIn, int stay, int fadeOut) {
      getPlayer().sendTitle(title.toLegacyText(), subtitle.toLegacyText(), fadeIn, stay, fadeOut);
  }

  /**
   * @see Title#sendSubtitle(Player, BaseComponent)
   * @deprecated Use the Spigot API.
   */
  public void sendSubtitle(@Nonnull BaseComponent subtitle) {
    Title.sendSubtitle(getPlayer(), subtitle);
  }

  /**
   * @see Title#sendSubtitle(Player, BaseComponent, BaseComponent)
   * @deprecated Use the Spigot API.
   */
  public void sendSubtitle(@Nonnull BaseComponent title, @Nonnull BaseComponent subtitle) {
    Title.sendSubtitle(getPlayer(), title, subtitle);
  }
}
