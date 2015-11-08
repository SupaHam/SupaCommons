package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;

import com.supaham.commons.Pausable;
import com.supaham.commons.state.State;
import com.supaham.commons.state.Stateable;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a ticker task, a task that runs over a set interval. A {@link TickerTask} can be
 * stopped or started at any point in time. As well as paused or resumed at any point in time
 * (assuming the task isn't stopped). This class doesn't implement the actual task call handling,
 * it makes use of Bukkit's {@link BukkitTask}.
 *
 * @see #TickerTask(Plugin, long)
 * @see #TickerTask(Plugin, long, long)
 * @see #TickerTask(Plugin, long, Runnable)
 * @see #TickerTask(Plugin, long, long, Runnable)
 * @since 0.1
 */
public class TickerTask implements Runnable, Pausable, Stateable {

  private final Plugin plugin;
  private final Runnable runnable;
  private long delay;
  private long interval;
  private boolean async;

  protected State state = State.STOPPED;
  private BukkitTask task;
  private long lastTickMillis;
  private boolean paused = true;
  private long totalTicks;
  private long currentTick;

  /**
   * Constructs a new TickerTask that runs once after the given delay (in ticks). This is
   * equivalent to {@link #TickerTask(Plugin, long, long)} with the last {@code runnable} being -1.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   *
   * @see #TickerTask(Plugin, long, long)
   * @see #TickerTask(Plugin, long, Runnable)
   */
  public TickerTask(@Nonnull Plugin plugin, long delay) {
    this(plugin, delay, null);
  }

  /**
   * Constructs a new TickerTask that runs over a set interact after the given delay (in ticks).
   * This is equivalent to calling {@link #TickerTask(Plugin, long, Runnable)} with the
   * {@code runnable} as null.
   *
   * <b>Note:</b> If the interval is -1 this task will only run once.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   * @param interval interval between each run
   *
   * @see #TickerTask(Plugin, long, long, Runnable)
   */
  public TickerTask(@Nonnull Plugin plugin, long delay, long interval) {
    this(plugin, delay, interval, null);
  }

  /**
   * Constructs a new TickerTask that runs once after the given delay (in ticks). This runnable
   * constructor exists solely for 1.8 support, providing convenience through lambda usage.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   * @param runnable runnable to use for this execution
   *
   * @see #TickerTask(Plugin, long)
   */
  public TickerTask(@Nonnull Plugin plugin, long delay, @Nullable Runnable runnable) {
    this(plugin, delay, -1, runnable);
  }

  /**
   * Constructs a new TickerTask that runs over a set interact after the given delay (in ticks).
   *
   * <b>Note:</b> If the interval is -1 this task will only run once.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   */
  public TickerTask(@Nonnull Plugin plugin, long delay, long interval,
                    @Nullable Runnable runnable) {
    checkNotNull(plugin, "plugin cannot be null.");
    this.plugin = plugin;
    this.delay = delay;
    setInterval(interval);
    this.runnable = runnable == null ? this : runnable;
  }

  /**
   * This method does not need to be overridden if a {@link Runnable} was passed to the
   * constructor. If no runnable was passed to the constructor, this task will do nothing.
   */
  @Override public void run() {}

  private void _run() {
    totalTicks++;
    if (isPaused()) {
      return;
    }
    currentTick++;
    try {
      this.runnable.run();
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.lastTickMillis = System.currentTimeMillis();
  }

  /**
   * Starts this {@link TickerTask} synchronously. If this task is already started the call is
   * terminated.
   *
   * @return true if the task's state was changed to started
   */
  public boolean start() {
    if (isStarted()) {
      return false;
    }
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        _run();
      }
    };
    if (isAsync()) { // Asynchronous
      this.task = plugin.getServer().getScheduler()
          .runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    } else { // Synchronous
      this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }
    this.paused = false;
    return true;
  }

  /**
   * Starts this {@link TickerTask} asynchronously. If this task is already started the call is
   * terminated.
   *
   * @return true if the task's state was changed to started
   */
  public boolean startAsync() {
    if (isStarted()) {
      return false;
    }
    setAsync();
    start();
    return start();
  }

  /**
   * Starts this {@link TickerTask} synchronously. If this task is already started the call is
   * terminated.
   *
   * @return true if the task's state was changed to started
   */
  public boolean startSync() {
    if (isStarted()) {
      return false;
    }
    setAsync(false);
    return start();
  }

  /**
   * Stops this {@link TickerTask}. If this task is already stopped the call is terminated.
   *
   * @return true if the task's state was changed to stopped
   */
  public boolean stop() {
    if (!isStarted()) {
      return false;
    }
    this.task.cancel();
    this.task = null;
    this.paused = true;
    return true;
  }

  @Override
  public boolean isPaused() {
    return paused;
  }

  /**
   * Pauses this {@link TickerTask}. If this task is already paused the call is terminated.
   *
   * @return true if this task's state was changed to paused
   */
  @Override
  public boolean pause() {
    if (!isStarted() || this.paused) {
      return false;
    }
    this.paused = true;
    return true;
  }

  /**
   * Resumes this {@link TickerTask}. If this task is not paused the call is terminated.
   *
   * @return true if this task's state was changed to resumed
   */
  @Override
  public boolean resume() {
    if (!isStarted() || !this.paused) {
      return false;
    }
    this.paused = false;
    return true;
  }

  @Nonnull @Override public State getState() {
    return this.state;
  }

  @Override public boolean setState(@Nonnull State state) throws UnsupportedOperationException {
    Preconditions.checkNotNull(state, "state cannot be null.");
    State old = this.state;
    boolean change = this.state.isIdle() != state.isIdle();
    if (change) {
      this.state = state;
      switch (state) {
        case PAUSED:
          pause();
          break;
        case ACTIVE:
          if (old == State.PAUSED) { // Only resume if it was previously paused
            resume();
          } else {
            start();
          }
          break;
        case STOPPED:
          stop();
          break;
      }
    }
    return change;
  }

  public boolean isStarted() {
    return this.task != null;
  }

  public Plugin getPlugin() {
    return plugin;
  }

  public long getDelay() {
    return delay;
  }

  public void setDelay(long delay) {
    this.delay = delay;
  }

  public long getInterval() {
    return interval;
  }

  public void setInterval(long interval) {
    this.interval = Math.max(interval, -1);
  }

  public boolean isAsync() {
    return async;
  }

  public TickerTask setAsync() {
    return setAsync(true);
  }

  public TickerTask setAsync(boolean async) {
    Preconditions.checkState(!isStarted(), "task is already started.");
    this.async = async;
    return this;
  }

  public BukkitTask getTask() {
    return task;
  }

  public long getLastTickMillis() {
    return lastTickMillis;
  }

  /**
   * Sets whether this {@link TickerTask} should be paused. If {@code paused} is true it would be
   * equivalent to calling {@link #pause()}, otherwise {@link #resume()}.
   *
   * @param paused whether to pause the task
   *
   * @return whether any action was taken, a case where this would return false if {@code paused} is
   * true and this task is already paused
   *
   * @see #pause()
   * @see #resume()
   */
  public boolean setPaused(boolean paused) {
    return paused ? pause() : resume();
  }

  public long getTotalTicks() {
    return totalTicks;
  }

  public long getCurrentTick() {
    return currentTick;
  }
}
