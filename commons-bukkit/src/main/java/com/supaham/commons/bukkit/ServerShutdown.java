package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.bukkit.players.Freeze;
import com.supaham.commons.bukkit.players.Players;
import com.supaham.commons.bukkit.players.Players.PlayersSupplier;
import com.supaham.commons.bukkit.potion.PotionEffectManager;
import com.supaham.commons.bukkit.potion.Potions;
import com.supaham.commons.bukkit.utils.EventUtils;
import com.supaham.commons.state.State;
import com.supaham.commons.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;

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
  private String shutdownMessage;
  private String restartMessage;
  private int shutdownDelay = 0;
  private boolean kickingPlayersOnShutdown = true;
  private String stopPermission;
  private String restartPermission;

  private boolean restarting;

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
        if (kickingPlayersOnShutdown) {
          player.kickPlayer(ServerShutdown.this.kickMessage);
        }
      }

      @Override
      public void onDone() {
        new TickerTask(plugin, shutdownDelay, 0) {
          @Override public void run() {
            if (restarting) {
              Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
            } else {
              Bukkit.getServer().shutdown();
            }
            stop();
          }
        }.start();
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
    run(false);
  }

  public void run(boolean restart) {
    Preconditions.checkState(!isInProgress(), "Shutdown already in progress.");
    this.restarting = restart;
    if (!preRun()) {
      this.state = State.STOPPED;
      this.rerunTask.start();
      return;
    } else {
      this.rerunTask.stop();
    }

    this.state = State.ACTIVE;
    this.delayedIterator.start();
    if (this.restarting) {
      if (this.restartMessage != null) {
        this.plugin.getServer().broadcastMessage(this.restartMessage);
      }
    } else {
      if (this.shutdownMessage != null) {
        this.plugin.getServer().broadcastMessage(this.shutdownMessage);
      }
    }
  }

  @Override
  public boolean setState(State state) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("ServerShutdown does not support state changing.");
  }

  /**
   * This method is executed right before the kicking process begins. This method applies
   * {@link Potions#noJump()} and {@link Potions#noWalk()} to players. This method also fires the
   * {@link ServerShutdownEvent}.
   */
  protected boolean preRun() {
    for (Player player : this.delayedIterator.getSupplier().get()) {
      this.freeze.freeze(player, -1, true);
    }
    return !EventUtils.callEvent(new ServerShutdownEvent(this)).isCancelled();
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
  public String getShutdownMessage() {
    return shutdownMessage;
  }

  /**
   * Sets the message to broadcast when this shutdown task begins. If the message is empty it will
   * be set to null.
   *
   * @param shutdownMessage shutdown message to set, nullable
   */
  public void setShutdownMessage(@Nullable String shutdownMessage) {
    this.shutdownMessage = StringUtils.stripToNull(shutdownMessage);
  }

  /**
   * Returns the message to broadcast when this shutdown task begins.
   *
   * @return broadcast message, nullable but never empty
   */
  @Nullable
  public String getRestartMessage() {
    return restartMessage;
  }

  /**
   * Sets the message to broadcast when this restart task begins. If the message is empty it will
   * be set to null.
   *
   * @param restartMessage restart message to set, nullable
   */
  public void setRestartMessage(@Nullable String restartMessage) {
    this.restartMessage = StringUtils.stripToNull(restartMessage);
  }

  public int getShutdownDelay() {
    return shutdownDelay;
  }

  public void setShutdownDelay(int shutdownDelay) {
    this.shutdownDelay = shutdownDelay;
  }

  public boolean isKickingPlayersOnShutdown() {
    return kickingPlayersOnShutdown;
  }

  public void setKickingPlayersOnShutdown(boolean kickingPlayersOnShutdown) {
    this.kickingPlayersOnShutdown = kickingPlayersOnShutdown;
  }

  public String getStopPermission() {
    return stopPermission;
  }

  public void setStopPermission(String stopPermission) {
    this.stopPermission = stopPermission;
  }

  public String getRestartPermission() {
    return restartPermission;
  }

  public void setRestartPermission(String restartPermission) {
    this.restartPermission = restartPermission;
  }

  private final class ServerShutdownListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerCommandEvent(ServerCommandEvent event) {
      String command = event.getCommand();
      boolean restart = command.startsWith("restart");
      if (restart || command.startsWith("stop")) {
        ServerShutdown.this.run(restart);
        event.setCommand(""); // dont execute stop!
      }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerCommandEvent(PlayerCommandPreprocessEvent event) {
      String message = event.getMessage();
      if (message.startsWith("/restart")) {
        if (!event.getPlayer().hasPermission(getRestartPermission())) {
          return;
        }
        ServerShutdown.this.run(true);
        event.setCancelled(true); // dont execute stop!
      } else if (message.startsWith("/stop")) {
        if (!event.getPlayer().hasPermission(getStopPermission())) {
          return;
        }
        ServerShutdown.this.run(false);
        event.setCancelled(true); // dont execute stop!
      }
    }

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

  /**
   * Called when the server is about to shutdown.
   */
  public static class ServerShutdownEvent extends Event implements Cancellable {

    private final ServerShutdown serverShutdown;
    private boolean cancelled;

    public ServerShutdownEvent(@Nonnull ServerShutdown serverShutdown) {
      this.serverShutdown = Preconditions.checkNotNull(serverShutdown,
                                                       "serverShutdown cannot be null.");
    }

    public ServerShutdown getServerShutdown() {
      return serverShutdown;
    }

    @Override public boolean isCancelled() {
      return cancelled;
    }

    @Override public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
    }

    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() { return handlerList; }

    public static HandlerList getHandlerList() { return handlerList; }
  }
}
