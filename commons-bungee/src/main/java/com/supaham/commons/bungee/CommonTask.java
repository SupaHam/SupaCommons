package com.supaham.commons.bungee;

import com.google.common.base.Preconditions;

import com.supaham.commons.Pausable;
import com.supaham.commons.state.State;
import com.supaham.commons.state.Stateable;

import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a ticker task, a task that runs over a set interval. A {@link CommonTask} can be
 * stopped or started at any point in time. As well as paused or resumed at any point in time
 * (assuming the task isn't stopped). This class doesn't implement the actual task call handling,
 * it makes use of Bukkit's {@link ScheduledTask}.
 *
 * @see #CommonTask(CommonPlugin, long)
 * @see #CommonTask(CommonPlugin, long, long)
 * @see #CommonTask(CommonPlugin, long, Runnable)
 * @see #CommonTask(CommonPlugin, long, long, Runnable)
 * @since 0.3.6
 */
public class CommonTask implements Runnable, Pausable, Stateable {

  private final CommonPlugin plugin;
  private final Runnable runnable;
  private long delay;
  private long interval;
  private TimeUnit unit;

  protected State state = State.STOPPED;
  private ScheduledTask task;
  private long lastTickMillis;
  private boolean paused = true;
  private long totalTicks;
  private long currentTick;

  /**
   * Constructs a new TickerTask that runs once after the given delay (in milliseconds). This is
   * equivalent to {@link #CommonTask(CommonPlugin, long, TimeUnit)} with the {@code unit} being
   * {@link TimeUnit#MILLISECONDS}.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in milliseconds) before this task should initiate
   *
   * @see #CommonTask(CommonPlugin, long, TimeUnit)
   * @see #CommonTask(CommonPlugin, long, TimeUnit, Runnable)
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay) {
    this(plugin, delay, TimeUnit.MILLISECONDS);
  }

  /**
   * Constructs a new TickerTask that runs once after the given delay (in milliseconds). This is
   * equivalent to {@link #CommonTask(CommonPlugin, long, TimeUnit, Runnable)} with the {@code
   * unit} being {@link TimeUnit#MILLISECONDS}.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in milliseconds) before this task should initiate
   *
   * @see #CommonTask(CommonPlugin, long, TimeUnit)
   * @see #CommonTask(CommonPlugin, long, TimeUnit, Runnable)
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, Runnable runnable) {
    this(plugin, delay, TimeUnit.MILLISECONDS, runnable);
  }

  /**
   * Constructs a new TickerTask that runs once after the given delay. This is equivalent to
   * {@link #CommonTask(CommonPlugin, long, TimeUnit, Runnable)} with the last {@code runnable}
   * being null.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   * @param unit time unit the delay is measured in
   *
   * @see #CommonTask(CommonPlugin, long, long, TimeUnit)
   * @see #CommonTask(CommonPlugin, long, TimeUnit, Runnable)
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, @Nonnull TimeUnit unit) {
    this(plugin, delay, unit, null);
  }

  /**
   * Constructs a new TickerTask that runs over a set interact after the given delay (in
   * milliseconds). This is equivalent to calling {@link #CommonTask(CommonPlugin, long, long,
   * TimeUnit, Runnable)} with the {@code runnable} as null.
   *
   * <b>Note:</b> If the interval is -1 this task will only run once.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in milliseconds) before this task should initiate
   * @param interval interval (in milliseconds) between each run
   *
   * @see #CommonTask(CommonPlugin, long, long, TimeUnit)
   * @see #CommonTask(CommonPlugin, long, long, TimeUnit, Runnable)
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, long interval) {
    this(plugin, delay, interval, TimeUnit.MILLISECONDS, null);
  }

  /**
   * Constructs a new TickerTask that runs over a set interact after the given delay (in ticks).
   * This is equivalent to calling {@link #CommonTask(CommonPlugin, long, long, TimeUnit,
   * Runnable)} with the {@code runnable} as null.
   *
   * <b>Note:</b> If the interval is -1 this task will only run once.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   * @param interval interval between each run
   *
   * @see #CommonTask(CommonPlugin, long, long, Runnable)
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, long interval,
                    @Nonnull TimeUnit unit) {
    this(plugin, delay, interval, unit, null);
  }

  /**
   * Constructs a new TickerTask that runs once after the given delay (in ticks). This runnable
   * constructor exists solely for 1.8 support, providing convenience through lambda usage.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   * @param runnable runnable to use for this execution
   *
   * @see #CommonTask(CommonPlugin, long)
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, TimeUnit unit,
                    @Nullable Runnable runnable) {
    this(plugin, delay, -1, unit, runnable);
  }

  /**
   * Constructs a new TickerTask that runs over a set interact after the given delay (in ticks).
   *
   * <b>Note:</b> If the interval is -1 this task will only run once.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, long interval,
                    @Nullable Runnable runnable) {
    this(plugin, delay, interval, TimeUnit.MILLISECONDS, runnable);
  }

  /**
   * Constructs a new TickerTask that runs over a set interact after the given delay (in ticks).
   *
   * <b>Note:</b> If the interval is -1 this task will only run once.
   *
   * @param plugin plugin to own this task
   * @param delay delay (in ticks) before this task should initiate
   */
  public CommonTask(@Nonnull CommonPlugin plugin, long delay, long interval, @Nonnull TimeUnit unit,
                    @Nullable Runnable runnable) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    this.plugin = plugin;
    this.delay = delay;
    setInterval(interval);
    this.unit = unit;
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
   * Starts this {@link CommonTask}. If this task is already started the call is terminated.
   *
   * @return true if the task's state was changed to started
   */
  public boolean start() {
    if (isStarted()) {
      return false;
    }
    this.task = plugin.getProxy().getScheduler().schedule(plugin.getBungeePlugin(), new Runnable() {
      @Override public void run() {
        _run();
      }
    }, this.delay, this.interval, this.unit);
    this.paused = false;
    return true;
  }

  /**
   * Stops this {@link CommonTask}. If this task is already stopped the call is terminated.
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
   * Pauses this {@link CommonTask}. If this task is already paused the call is terminated.
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
   * Resumes this {@link CommonTask}. If this task is not paused the call is terminated.
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

  public CommonPlugin getPlugin() {
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

  public ScheduledTask getTask() {
    return task;
  }

  public long getLastTickMillis() {
    return lastTickMillis;
  }

  /**
   * Sets whether this {@link CommonTask} should be paused. If {@code paused} is true it would be
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
