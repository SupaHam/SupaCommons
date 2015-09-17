package com.supaham.commons.bungee.modules.framework;

import com.supaham.commons.state.Stateable;

/**
 * Represents a Module that may modify the gameplay experience that is typically held in a
 * {@link ModuleContainer} for access.
 *
 * @since 0.3.6
 */
public interface Module extends Stateable {

  /**
   * Gets the {@link ModuleContainer} of this module.
   *
   * @return module container
   */
  ModuleContainer getContainer();
}
