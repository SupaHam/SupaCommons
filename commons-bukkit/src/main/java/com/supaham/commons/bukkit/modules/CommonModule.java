package com.supaham.commons.bukkit.modules;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.CommonPlugin;
import com.supaham.commons.state.State;

import javax.annotation.Nonnull;

/**
 * Represents a Module that may modify the gameplay experience that is typically held in
 * {@link CommonPlugin} for global access.
 */
public class CommonModule implements Module {

  protected final ModuleContainer container;
  protected final CommonPlugin plugin;

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
  public boolean setState(State state) throws UnsupportedOperationException {
    boolean change = this.state.isIdle() != state.isIdle();
    this.state = state;
    return change;
  }
}
