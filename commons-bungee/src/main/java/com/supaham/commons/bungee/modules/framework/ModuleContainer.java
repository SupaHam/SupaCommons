package com.supaham.commons.bungee.modules.framework;

import com.google.common.base.Preconditions;

import com.supaham.commons.bungee.CommonPlugin;
import com.supaham.commons.bungee.modules.Bungee;
import com.supaham.commons.state.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link Module} container that allows for registration and removal of modules.
 *
 * @since 0.3.6
 */
public class ModuleContainer {

  private final CommonPlugin plugin;
  private final Map<Class<? extends Module>, Module> modules = new HashMap<>();
  private final ModuleContainer parent;

  public ModuleContainer(@Nonnull CommonPlugin plugin) {
    this.plugin = Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    this.parent = null;
  }

  public ModuleContainer(@Nonnull ModuleContainer parent) {
    this.plugin = parent.plugin;
    this.parent = parent;
  }

  /**
   * Registers all the supa-commons modules to this module container. The {@code activateAll}
   * boolean dictates whether the method should also set the module's state to {@link
   * State#ACTIVE}.
   *
   * @param activateAll whether to activate all registered defaults
   */
  public void registerAll(boolean activateAll) {
    List<Module> modules = new ArrayList<>();

    modules.add(new Bungee(this));

    for (Module module : modules) {
      Module registered = register(module);
      if (registered == null || registered != module) { // registered
        if (activateAll) {
          try {
            module.setState(State.ACTIVE);
          } catch (UnsupportedOperationException ignored) {
          }
        }
      }
    }
  }

  /**
   * Registers a {@link Module} to this container. This is equivalent to {@link #register(Class,
   * Module)} with the class as the given module's class.
   *
   * @param module module to register
   *
   * @return previously registered module to the given module's class, nullable
   */
  @Nullable
  public Module register(@Nonnull Module module) {
    Preconditions.checkNotNull(module, "module cannot be null.");
    return register(module.getClass(), module);
  }

  /**
   * Registers a {@link Module} to this container.
   *
   * @param clazz class of the module to register
   * @param module module to register to
   *
   * @return the previous module registered to the given {@code class}, nullable
   */
  @Nullable
  public Module register(@Nonnull Class<? extends Module> clazz, @Nonnull Module module) {
    Preconditions.checkNotNull(clazz, "class cannot be null.");
    Preconditions.checkNotNull(module, "module cannot be null.");
    Module old = this.modules.put(clazz, module);
    if (old != null && old != module) {
      try {
        old.setState(State.STOPPED);
      } catch (UnsupportedOperationException ignored) {
      }
    }
    return old;
  }

  /**
   * Unregisters a {@link Module} from this container. This is equivalent to
   * {@link #unregister(Class)} with the class as the given module's class. If the given module
   * was never registered to this module, false is always returned.
   *
   * @param module module to unregister
   *
   * @return whether the removed module is equal to the given module
   */
  public boolean unregister(@Nonnull Module module) {
    Preconditions.checkNotNull(module, "module cannot be null.");
    return module.equals(unregister(module.getClass()));
  }

  /**
   * Unregisters a {@link Module} from this container using the module's class. Please keep in mind
   * this method does not respect superclass checks and only explicitly the given class.
   *
   * @param clazz class to unregister
   *
   * @return the unregistered module, nullable
   */
  @Nullable
  public Module unregister(@Nonnull Class<? extends Module> clazz) {
    Preconditions.checkNotNull(clazz, "class cannot be null.");
    Module old = this.modules.remove(clazz);
    if (old != null) {
      try {
        old.setState(State.STOPPED);
      } catch (UnsupportedOperationException ignored) {
      }
    }
    return old;
  }

  /**
   * Gets a {@link Module} by {@link Class}. The returned module is null only if this plugin does
   * not register the class.
   *
   * @param clazz class of the module to get
   * @param <T> type of module
   *
   * @return module instance, nullable
   */
  @Nullable
  public <T extends Module> T getModule(@Nonnull Class<T> clazz) {
    Preconditions.checkNotNull(clazz, "class cannot be null.");
    Module module = this.modules.get(clazz);
    return (T) (module != null ? module : this.parent != null ? this.parent.getModule(clazz)
                                                              : null);
  }

  /**
   * Gets the {@link CommonPlugin} owner of this module container.
   *
   * @return owner of this container
   */
  @Nonnull
  public CommonPlugin getPlugin() {
    return plugin;
  }

  /**
   * Returns an unmodifiable map of all the registered module classes and their instances in this
   * container.
   *
   * @return a map of classes and their module instances
   */
  @Nonnull
  public Map<Class<? extends Module>, Module> getModules() {
    Map<Class<? extends Module>, Module> map;
    if (parent != null) { // Make sure to add parent modules then these modules to override
      map = new HashMap<>(parent.modules);
      map.putAll(modules);
    } else {
      map = modules;
    }
    return Collections.unmodifiableMap(map);
  }
}
