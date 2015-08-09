package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.utils.StringUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Chat silencing module. Supports global and per player chatting mute. This module allows for the
 * ability to assign a message response as to why the player was muted whenever they try to chat.
 * <p/>
 * Please note that while this module does support global chat silence, it does NOT override
 * per-player silencing. Meaning if {@link #unsilenceAll(boolean)} is called with the boolean as
 * true, meaning only lift the global silence, and a player is specifically muted per
 * {@link #silence(Player)} methods, they will still be silenced until their silence duration is 
 * over or they are unsilenced using {@link #unsilence(Player)}, or {@link #unsilenceAll()}
 *
 * @see #ChatSilencer(ModuleContainer)
 * @see #silence(Player)
 * @see #silenceAll()
 * @since 0.2.3
 */
public class ChatSilencer extends CommonModule {

  private SilenceData globalMuteData;
  private Map<Player, SilenceData> silenced = new HashMap<>();
  private Listener listener = new ChatListener();

  public ChatSilencer(@Nonnull ModuleContainer container) {
    super(container);
    registerListener(this.listener);
  }

  /**
   * Silences a {@link Player}'s chat events indefinitely with no response-reason. This is
   * equivalent to calling {@link #silence(Player, int)} with the {@code int} as -1.
   *
   * @param player player to silence
   *
   * @return whether the silence was successful
   */
  public boolean silence(@Nonnull Player player) {
    return silence(player, -1);
  }

  /**
   * Silences a {@link Player}'s chat events, indefinitely, with no response-reason. This is
   * equivalent to calling {@link #silence(Player, int, String)} with the {@code String} as null.
   *
   * @param player player to silence
   * @param duration duration of the silence to set, -1 for infinite
   *
   * @return whether the silence was successful
   */
  public boolean silence(@Nonnull Player player, int duration) {
    return silence(player, duration, null);
  }

  /**
   * Silences a {@link Player}'s chat events, indefinitely, and gives a reason as to why when the
   * player attempts to chat.
   *
   * @param player player to silence
   * @param duration duration of the silence to set, -1 for infinite
   *
   * @return whether the silence was successful
   */
  public boolean silence(@Nonnull Player player, int duration, @Nullable String muteMessage) {
    Preconditions.checkNotNull(player, "player cannot be null.");
    SilenceData data = new SilenceData(duration, muteMessage);
    if (!data.isDone()) {
      this.silenced.put(player, data);
      return true;
    }
    return false;
  }

  /**
   * Silences all chat events, indefinitely, with no response-reason. This is equivalent to calling
   * {@link #silenceAll(int)} with the {@code int} as -1.
   *
   * @return whether the silence was successful
   */
  public boolean silenceAll() {
    return silenceAll(-1);
  }

  /**
   * Silences all chat events, for a certain duration, with no response-reason. This is
   * equivalent to calling {@link #silenceAll(int, String)} with the {@code String} as null.
   *
   * @param duration duration of the silence to set, -1 for infinite
   *
   * @return whether the silence was successful
   */
  public boolean silenceAll(int duration) {
    return silenceAll(duration, null);
  }

  /**
   * Silences all chat events, for a certain duration, and gives a reason as to why when each
   * player attempts to chat.
   *
   * @param duration duration (in ticks) of the silence to set, -1 for infinite
   * @param muteMessage the message response for the global silence
   *
   * @return whether the silence was successful
   */
  public boolean silenceAll(int duration, @Nullable String muteMessage) {
    SilenceData data = new SilenceData(duration, muteMessage);
    if (!data.isDone()) {
      this.globalMuteData = data;
      return true;
    }
    return false;
  }

  /**
   * Unsilences a {@link Player}.
   *
   * @param player player to unsilence
   *
   * @return whether the player was previously silenced
   */
  public boolean unsilence(@Nonnull Player player) {
    Preconditions.checkNotNull(player, "player cannot be null.");
    return this.silenced.remove(player) != null;
  }

  /**
   * Unsilences all future chat events, globally and per player. This is equivalent to calling
   * {@link #unsilenceAll(boolean)} with the boolean as false.
   */
  public void unsilenceAll() {
    unsilenceAll(false);
  }

  /**
   * Removes the global silence. It is important to note that, if {@code onlyGlobal} is true, and a
   * player is specifically silenced, they will still be silenced as global chat silence is
   * separate from per-player.
   *
   * @param onlyGlobal whether to only unsilence global mute
   */
  public void unsilenceAll(boolean onlyGlobal) {
    this.globalMuteData = null;
    if (!onlyGlobal) {
      this.silenced.clear();
    }
  }

  protected void handleEvent(AsyncPlayerChatEvent event) {
    // The global mute takes power over the player mute, so the order of these events matter.
    SilenceData data = ChatSilencer.this.globalMuteData;
    SilenceData playerData = null;
    if (data == null) {
      data = playerData = ChatSilencer.this.silenced.get(event.getPlayer());
    }

    if (data != null) {
      if (check(data)) {
        event.setCancelled(true);
        if (data.muteMessage != null) {
          event.getPlayer().sendMessage(data.muteMessage);
        }
      } else if (playerData != null) { // The player is no longer muted.
        unsilence(event.getPlayer());
      }
    }
  }

  protected void handleEvent(PlayerQuitEvent event) {
    unsilence(event.getPlayer());
  }

  private boolean check(SilenceData data) {
    if (getState().isIdle()) {
      return false;
    }
    boolean muted = false;
    if (data != null) {
      if (!data.isDone()) {
        muted = true;
      } else {
        if (data == globalMuteData) {
          this.globalMuteData = null;
        }
      }
    }
    return muted;
  }

  private class SilenceData {

    private long expires;
    private String muteMessage;

    public SilenceData(int duration, String muteMessage) {
      this.muteMessage = StringUtils.stripToNull(muteMessage);
      setExpires(duration);
    }

    public boolean isDone() {
      return this.expires > -1 && System.currentTimeMillis() - this.expires >= 0;
    }

    public void setExpires(int duration) {
      if (duration >= 0 && duration != Integer.MAX_VALUE) {
        this.expires = System.currentTimeMillis() + ((long) duration * 50);
      } else {
        this.expires = -1;
      }
    }
  }

  private final class ChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
      handleEvent(event);
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
      handleEvent(event);
    }
  }
}
