package com.supaham.commons.bukkit.modules;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.CommonPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a {@link Module} container that allows for registration and removal of modules.
 */
public class ModuleContainer {

  private final CommonPlugin plugin;
  private final Map<Class<? extends Module>, Module> modules = new HashMap<>();

  public ModuleContainer(@Nonnull CommonPlugin plugin) {
    this.plugin = Preconditions.checkNotNull(plugin, "plugin cannot be null.");
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
    return (T) this.modules.get(clazz);
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
    return Collections.unmodifiableMap(modules);
  }
}
