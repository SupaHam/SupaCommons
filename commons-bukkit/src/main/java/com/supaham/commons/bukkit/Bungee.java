package com.supaham.commons.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;

/**
 * Utility class for working with Bungee. This class contains methods such as
 * {@link #sendPlayerTo(Player, String)}, and more.
 *
 * @since 0.1
 */
@Getter
public class Bungee {

  private final Plugin owner;
  private final List<UUID> connecting = new ArrayList<>();
  private int connectCooldown;

  /**
   * Constructs a {@link Bungee} instance with a {@link Plugin} as the owner and a 40 tick
   * connecting cooldown.
   * <br />
   * If the {@code connectCooldown} is <b>40</b> and a player is to be sent to a server, then
   * <b>30</b> ticks later the same player is asked to leave the server again and still hasn't left
   * the code terminates.
   *
   * @param owner plugin to own this session.
   *
   * @see #Bungee(Plugin, int)
   */
  public Bungee(@NonNull Plugin owner) {
    this(owner, 40);
  }

  /**
   * Constructs a {@link Bungee} instance with a {@link Plugin} as the owner.
   * <br />
   * If the {@code connectCooldown} is <b>40</b> and a player is to be sent to a server, then
   * <b>30</b> ticks later the same player is asked to leave the server again and still hasn't left
   * the code terminates.
   *
   * @param owner plugin to own this session.
   * @param connectCooldown cooldown in ticks that a player should be able to connect to a server.
   */
  public Bungee(@NonNull Plugin owner, int connectCooldown) {
    this.owner = owner;
    owner.getServer().getMessenger().registerOutgoingPluginChannel(owner, "BungeeCord");
    this.connectCooldown = connectCooldown;
  }

  /**
   * Sends all online players to a specific bungee server.
   *
   * @param serverName server to send all online players to
   *
   * @see #sendPlayerTo(Player, String)
   */
  public void sendAllTo(String serverName) {
    for (Player player : owner.getServer().getOnlinePlayers()) {
      sendPlayerTo(player, serverName);
    }
  }

  /**
   * Sends a {@link Player} to a specific bungee server.
   * <br />
   * The one and only case where the returned boolean would be false is if the {@code player} was 
   * already being sent within the {@link #getConnectCooldown()} ticks.
   *
   * @param player player to send
   * @param serverName bungee server to send player to
   * @return whether the player is being sent to the specified {@code serverName}.
   */
  public boolean sendPlayerTo(final Player player, String serverName) {
    if (this.connecting.contains(player.getUniqueId())) {
      return false;
    }
    
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    try (DataOutputStream out = new DataOutputStream(bout)) {
      out.writeUTF("Connect");
      out.writeUTF(serverName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    player.sendPluginMessage(this.owner, "BungeeCord", bout.toByteArray());
    this.connecting.add(player.getUniqueId());
    new BukkitRunnable() {
      @Override
      public void run() {
        connecting.remove(player.getUniqueId());
      }
    }.runTaskLater(this.owner, this.connectCooldown);
    return true;
  }
  
  public List<UUID> getConnecting() {
    return Collections.unmodifiableList(this.connecting);
  }
}
