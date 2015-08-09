package com.supaham.commons.bukkit.modules;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.bukkit.TickerTask;
import com.supaham.commons.state.State;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Represents a Module that may modify the gameplay experience that is typically held in
 * {@link CommonPlugin} for global access.
 */
public class CommonModule implements Module {

  protected final ModuleContainer container;
  protected final CommonPlugin plugin;
  private final List<Listener> listeners = new ArrayList<>();
  private final List<TickerTask> tasks = new ArrayList<>();

  protected State state = State.STOPPED;

  /**
   * Constructs a new module. This module should never automatically register to the given
   * {@link ModuleContainer} as that is extremely unproductive and may cause compatibility issues.
   *
   * @param container module container to own this module.
   */
  public CommonModule(@Nonnull ModuleContainer container) {
    this.container = Preconditions.checkNotNull(container, "container cannot be null.");
    this.plugin = container.getPlugin();
  }

  protected boolean registerListener(@Nonnull Listener listener) {
    boolean added = this.listeners.add(listener);
    if (added && this.state.equals(State.ACTIVE)) {
      this.plugin.registerEvents(listener);
    }
    return added;
  }

  protected boolean unregisterListener(@Nonnull Listener listener) {
    boolean removed = this.listeners.remove(listener);
    if (removed && this.state.equals(State.ACTIVE)) {
      HandlerList.unregisterAll(listener);
    }
    return removed;
  }

  protected boolean registerTask(@Nonnull TickerTask tickerTask) {
    boolean added = this.tasks.add(tickerTask);
    if (added && this.state.equals(State.ACTIVE)) {
      tickerTask.start();
    }
    return added;
  }

  protected boolean unregisterTask(@Nonnull TickerTask tickerTask) {
    boolean removed = this.tasks.remove(tickerTask);
    if (removed && this.state.equals(State.ACTIVE)) {
      tickerTask.stop();
    }
    return removed;
  }

  @Override
  public ModuleContainer getContainer() {
    return this.container;
  }

  @Nonnull
  @Override
  public State getState() {
    return this.state;
  }

  @Override
  public boolean setState(@Nonnull State state) throws UnsupportedOperationException {
    Preconditions.checkNotNull(state, "state cannot be null.");
    boolean change = this.state.isIdle() != state.isIdle();
    this.state = state;
    if (change) {
      for (TickerTask task : this.tasks) {
        task.setState(state);
      }
      if (state.equals(State.ACTIVE)) {
        for (Listener listener : this.listeners) {
          this.plugin.registerEvents(listener);
        }
      } else if (state.equals(State.STOPPED)) {
        for (Listener listener : this.listeners) {
          HandlerList.unregisterAll(listener);
        }
      }
    }
    return change;
  }
}
