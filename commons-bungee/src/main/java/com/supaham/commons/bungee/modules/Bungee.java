package com.supaham.commons.bungee.modules;

import com.google.common.base.Preconditions;

import com.supaham.commons.bungee.CommonTask;
import com.supaham.commons.bungee.modules.framework.CommonModule;
import com.supaham.commons.bungee.modules.framework.Module;
import com.supaham.commons.bungee.modules.framework.ModuleContainer;
import com.supaham.commons.state.State;
import com.supaham.commons.utils.StringUtils;
import com.supaham.commons.utils.TimeUtils;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Module} container that allows for registration and removal of modules.
 * 
 * @since 0.3.6
 */
public class Bungee extends CommonModule {

  private final Map<UUID, Long> connecting = new HashMap<>();
  private final int connectCooldown;

  private CommonTask cooldownRemover = new CooldownRemover();

  /**
   * Constructs a {@link Bungee} instance with a {@link Plugin} as the owner and 2000 milliseconds
   * connecting cooldown.
   * <br />
   * If the {@code connectCooldown} is <b>2000</b> and a player is to be sent to a server, then
   * <b>1500</b> ticks later the same player is asked to leave the server again and still hasn't
   * left the code terminates.
   *
   * @param container module container to own this module.
   *
   * @see #Bungee(ModuleContainer, int)
   */
  public Bungee(@Nonnull ModuleContainer container) {
    this(container, 2000);
  }

  /**
   * Constructs a {@link Bungee} instance with a {@link Plugin} as the owner.
   * <br />
   * If the {@code connectCooldown} is <b>40</b> and a player is to be sent to a server, then
   * <b>1500</b> ticks later the same player is asked to leave the server again and still hasn't
   * left the code terminates.
   *
   * @param container module container to own this module.
   * @param connectCooldown cooldown (in milliseconds) that a player should be able to connect to a
   * server.
   */
  public Bungee(@Nonnull ModuleContainer container, int connectCooldown) {
    super(container);
    this.connectCooldown = connectCooldown;
    registerTask(this.cooldownRemover);
  }

  public boolean setState(@Nonnull State state) throws UnsupportedOperationException {
    boolean change = super.setState(state);
    if (change) {
      switch (state) {
        case STOPPED:
          this.connecting.clear();
          break;
      }
    }
    return change;
  }

  /**
   * Sends all online players to a specific bungee server.
   *
   * @param serverName server to send all online players to
   *
   * @see #sendPlayerTo(ProxiedPlayer, String)
   */
  public void sendAllTo(String serverName) {
    for (ProxiedPlayer player : this.plugin.getProxy().getPlayers()) {
      sendPlayerTo(player, serverName);
    }
  }

  /**
   * Sends a {@link ProxiedPlayer} to a specific bungee server.
   * <br />
   * The one and only case where the returned boolean would be false is if the {@code player} was
   * already being sent within the {@link #getConnectCooldown()} ticks.
   *
   * @param player player to send
   * @param serverName bungee server to send player to
   *
   * @return whether the player is being sent to the specified {@code serverName}.
   */
  public boolean sendPlayerTo(final ProxiedPlayer player, @Nonnull String serverName) {
    StringUtils.checkNotNullOrEmpty(serverName, "server name");
    Preconditions.checkState(getState().equals(State.ACTIVE), "Bungee module not active.");

    ServerInfo serverInfo = this.plugin.getProxy().getServerInfo(serverName);
    Preconditions.checkArgument(serverInfo != null, serverName + " is not a valid server.");

    Long previous = this.connecting.get(player.getUniqueId());
    if (previous != null && !TimeUtils.elapsed(previous, this.connectCooldown)) {
      return false;
    }
    player.connect(serverInfo);
    this.connecting.put(player.getUniqueId(), System.currentTimeMillis());
    return true;
  }

  public int getConnectCooldown() {
    return connectCooldown;
  }

  public Set<UUID> getConnecting() {
    return Collections.unmodifiableSet(this.connecting.keySet());
  }

  private final class CooldownRemover extends CommonTask {

    public CooldownRemover() {
      super(Bungee.this.plugin, 0, 1);
    }

    @Override public void run() {
      Iterator<Long> it = Bungee.this.connecting.values().iterator();
      while (it.hasNext()) {
        if (it.next() <= System.currentTimeMillis()) {
          it.remove();
        }
      }
    }
  }
}
