package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.supaham.commons.Pausable;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a ticker task, a task that runs over a set interval. A {@link TickerTask} can be
 * stopped or started at any point in time. As well as paused or resumed at any point in time
 * (assuming the task isn't stopped). This class doesn't implement the actual task call handling,
 * it makes use of Bukkit's {@link BukkitTask}.
 *
 * @since 0.1
 */
public class TickerTask implements Runnable, Pausable {

  private final Plugin plugin;
  private final long delay;
  private final long interval;
  private final Runnable runnable;

  private BukkitTask task;
  private long lastTickMillis;
  private boolean paused = true;
  private long totalTicks;
  private long currentTick;

  public TickerTask(@Nonnull Plugin plugin, long delay, long interval) {
    this(plugin, delay, interval, null);
  }

  public TickerTask(@Nonnull Plugin plugin, long delay, long interval,
                    @Nullable Runnable runnable) {
    checkNotNull(plugin, "plugin cannot be null.");
    this.plugin = plugin;
    this.delay = delay;
    this.interval = Math.max(interval, 0);
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
    run();
    this.lastTickMillis = System.currentTimeMillis();
  }

  /**
   * Starts this {@link TickerTask}. If this task is already started the call is terminated.
   *
   * @return true if the task's state was changed to started
   */
  public boolean start() {
    if (isStarted()) {
      return false;
    }
    this.task = new BukkitRunnable() {
      @Override
      public void run() {
        _run();
      }
    }.runTaskTimer(plugin, delay, interval);
    this.paused = false;
    return true;
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

  public boolean isStarted() {
    return this.task != null;
  }

  public Plugin getPlugin() {
    return plugin;
  }

  public long getDelay() {
    return delay;
  }

  public long getInterval() {
    return interval;
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
