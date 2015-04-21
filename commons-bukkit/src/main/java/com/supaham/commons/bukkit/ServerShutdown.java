package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.bukkit.players.Freeze;
import com.supaham.commons.bukkit.players.Players;
import com.supaham.commons.bukkit.players.Players.PlayersSupplier;
import com.supaham.commons.bukkit.potion.PotionEffectManager;
import com.supaham.commons.bukkit.potion.Potions;
import com.supaham.commons.state.State;
import com.supaham.commons.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Server shutdown task with a {@link DelayedIterator} of all online players that kicks them with
 * a nice message.
 *
 * @see #ServerShutdown(ModuleContainer)
 * @see #run()
 * @since 0.2
 */
public class ServerShutdown extends CommonModule implements Runnable {

  private final DelayedIterator<Player> delayedIterator;
  private final PotionEffectManager potionEffectManager;
  private final Freeze freeze;
  private final Listener listener = new ServerShutdownListener();

  private TickerTask rerunTask; // privately used to retry to shutdown if cancelled by preRun.
  private String kickMessage;
  private String broadcastMessage;

  /**
   * Constructs a new {@link ServerShutdown} in charge of slowly kicking players and stopping the
   * server. This is equivalent to calling {@link #ServerShutdown(ModuleContainer, int)} with the
   * {@code int} as 5.
   *
   * @param container module container to own this module
   *
   * @see #run()
   */
  public ServerShutdown(@Nonnull ModuleContainer container) {
    this(container, 5);
  }

  /**
   * Constructs a new {@link ServerShutdown} in charge of slowly kicking players and stopping the
   * server. This is equivalent to calling {@link #ServerShutdown(ModuleContainer, int,
   * PlayersSupplier)} with the PlayersSupplier as null, which then defaults to {@link
   * Players#serverPlayers()}.
   *
   * @param container module container to own this module
   * @param interval interval between each player kick
   *
   * @see #run()
   */
  public ServerShutdown(@Nonnull ModuleContainer container, int interval) {
    this(container, interval, null);
  }

  /**
   * Constructs a new {@link ServerShutdown} in charge of slowly kicking players and stopping the
   * server. If the given {@link PlayersSupplier} is null, it defaults to {@link
   * Players#serverPlayers()}.
   *
   * @param container module container to own this module
   * @param interval interval between each player kick
   * @param supplier supplier of players to kick
   *
   * @see #run()
   */
  public ServerShutdown(@Nonnull ModuleContainer container, int interval,
                        @Nullable PlayersSupplier supplier) {
    super(container);
    plugin.registerEvents(listener);

    ModuleContainer cont = plugin.getModuleContainer();
    PotionEffectManager pem = cont.getModule(PotionEffectManager.class);
    this.potionEffectManager = pem == null ? new PotionEffectManager(container) : pem;
    if (pem == null) { // we created our own potion effect manager.
      this.potionEffectManager.setState(State.ACTIVE);
    }
    Freeze freeze = cont.getModule(Freeze.class);
    this.freeze = freeze == null ? new Freeze(container) : freeze;
    if (freeze == null) { // we created our own freeze.
      this.freeze.setState(State.ACTIVE);
    }

    if (supplier == null) {
      supplier = Players.serverPlayers();
    }

    this.delayedIterator = new DelayedIterator<Player>(plugin, supplier, interval) {
      @Override
      public void onRun(Player player) {
        player.kickPlayer(ServerShutdown.this.kickMessage);
      }

      @Override
      public void onDone() {
        Bukkit.getServer().shutdown();
      }
    };

    this.rerunTask = new TickerTask(plugin, 0, 1) {
      @Override
      public void run() {
        if (!ServerShutdown.this.isInProgress()) { // This should always be true!
          ServerShutdown.this.run();
        } else { // this task is still running for some reason. Better safe than sorry.
          stop();
        }
      }
    };
    setKickMessage(null);
  }

  /**
   * Used to start a kicking process followed by server shutdown assuming {@link #preRun()} returns
   * true, if it does not, the task retries one tick later.
   */
  @Override
  public void run() {
    Preconditions.checkState(!isInProgress(), "Shutdown already in progress.");

    if (!preRun()) {
      this.state = State.STOPPED;
      this.rerunTask.start();
      return;
    } else {
      this.rerunTask.stop();
    }

    this.state = State.ACTIVE;
    this.delayedIterator.start();
    if (this.broadcastMessage != null) {
      this.plugin.getServer().broadcastMessage(this.broadcastMessage);
    }
  }

  @Override
  public boolean setState(State state) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("ServerShutdown does not support state changing.");
  }

  /**
   * This method is executed right before the kicking process begins. This method applies
   * {@link Potions#noJump()} and {@link Potions#noWalk()} to players.
   */
  protected boolean preRun() {
    for (Player player : this.delayedIterator.getSupplier().get()) {
      this.freeze.freeze(player, -1, true);
    }
    return true;
  }

  /**
   * Returns whether this task is in the progress of shutting down the server.
   *
   * @return whether this task is in progress
   */
  public boolean isInProgress() {
    return this.delayedIterator.isStarted();
  }

  /**
   * Returns the kick message. If this message was never set, it defaults to {@link
   * Server#getShutdownMessage()}.
   *
   * @return kick message
   */
  @Nonnull
  public String getKickMessage() {
    return kickMessage;
  }

  /**
   * Sets the kick message to send to players, attempting to join, or when kicking them. The
   * message can never be null or empty, if that is passed to this method, it sets the message to
   * {@link Server#getShutdownMessage()}.
   *
   * @param kickMessage kick message to set
   */
  public void setKickMessage(@Nullable String kickMessage) {
    this.kickMessage = StringUtils.stripToNull(kickMessage) == null ? Bukkit.getShutdownMessage()
                                                                    : kickMessage;
  }

  /**
   * Returns the message to broadcast when this shutdown task begins.
   *
   * @return broadcast message, nullable but never empty
   */
  @Nullable
  public String getBroadcastMessage() {
    return broadcastMessage;
  }

  /**
   * Sets the message to broadcast when this shutdown task begins. If the message is empty it will
   * be set to null.
   *
   * @param broadcastMessage broadcast message to set, nullable
   */
  public void setBroadcastMessage(@Nullable String broadcastMessage) {
    this.broadcastMessage = StringUtils.stripToNull(broadcastMessage);
  }

  private final class ServerShutdownListener implements Listener {

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
      if (isInProgress()) {
        event.disallow(Result.KICK_OTHER, ServerShutdown.this.kickMessage);
      }
    }

    // If a player happened to get to the joining stage when the server is shutting down after login.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
      if (isInProgress()) {
        event.getPlayer().kickPlayer(ServerShutdown.this.kickMessage);
      }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
      if (isInProgress()) {
        event.setCancelled(true);
      }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
      if (isInProgress()) {
        event.setCancelled(true);
      }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
      if (isInProgress()) {
        event.setCancelled(true);
      }
    }
  }
}
