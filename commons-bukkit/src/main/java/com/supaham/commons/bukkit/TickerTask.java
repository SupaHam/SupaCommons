package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;

/**
 * Represents a ticker task, a task that runs over a set interval. A {@link TickerTask} can be
 * stopped or started at any point in time. As well as paused or resumed at any point in time
 * (assuming the task isn't stopped). This class doesn't implement the actual task call handling, it
 * makes use of Bukkit's {@link BukkitTask}.
 *
 * @since 0.1
 */
public abstract class TickerTask implements Runnable {

  private final Plugin plugin;
  private final long delay;
  private final long interval;

  private BukkitTask task;
  private long lastTickMillis;
  private boolean paused = true;
  private long totalTicks;
  private long currentTick;

  public TickerTask(@Nonnull Plugin plugin, long delay, long interval) {
    checkNotNull(plugin, "plugin cannot be null.");
    this.plugin = plugin;
    this.delay = delay;
    this.interval = interval;
  }

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

  /**
   * Resumes this {@link TickerTask}. If this task is not paused the call is terminated.
   *
   * @return true if this task's state was changed to resumed
   */
  public boolean resume() {
    if (!isStarted() || !this.paused) {
      return false;
    }
    this.paused = false;
    return true;
  }

  /**
   * Pauses this {@link TickerTask}. If this task is already paused the call is terminated.
   *
   * @return true if this task's state was changed to paused
   */
  public boolean pause() {
    if (!isStarted() || this.paused) {
      return false;
    }
    this.paused = true;
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

  public boolean isPaused() {
    return paused;
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
