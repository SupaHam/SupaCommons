package com.supaham.commons.bungee;

import com.google.common.base.Preconditions;

import com.supaham.commons.bungee.modules.framework.ModuleContainer;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import pluginbase.logging.PluginLogger;

/**
 * Represents a simple implementation of {@link CommonPlugin}. This class throws an
 * {@link AssertionError} when {@link #SimpleCommonPlugin()} is called, so the implementation must
 * not call said constructor and instead call {@link #SimpleCommonPlugin(Class, String)}
 * <p />
 * This class does the following tasks:
 * <br />
 * <ol>
 * <li>Offers two protected fields:
 * <ul><li>pluginAgent: {@link BukkitPluginAgent}; used to hook into the PluginBase framework.</li>
 * <li>moduleContainer; {@link ModuleContainer}; used to assign {@link Module}s to this
 * class</li></ul>
 * </li>
 * <li>Within the only available constructor, this plugin is hooked into {@link CBukkitMain}.</li>
 * <li>During onLoad, onEnable, onDisable, they call the {@code pluginAgent} in their respective
 * order. So, when overriding, ensure to call said super methods.</li>
 * <li>Implements other methods provided </li>
 * </ol>
 * 
 * @since 0.3.6
 */
public abstract class SimpleCommonPlugin<T extends SimpleCommonPlugin> extends Plugin
    implements CommonPlugin {

  protected final ModuleContainer moduleContainer = new ModuleContainer(this);
  private PluginLogger log;

  private SimpleCommonPlugin() {
    throw new AssertionError("No, you may not.");
  }

  public SimpleCommonPlugin(@Nonnull Class<T> pluginClass, @Nonnull String commandPrefix) {
    Preconditions.checkNotNull(commandPrefix, "command prefix cannot be null.");
  }

  @Override public void onLoad() {
    this.log = PluginLogger.getLogger(this);
  }

  @Override public void onEnable() {
  }

  @Override public void onDisable() {
  }

  @Override public Plugin getBungeePlugin() {
    return this;
  }

  @Nonnull @Override public <L extends Listener> L registerEvents(@Nonnull L listener) {
    getProxy().getPluginManager().registerListener(this, listener);
    return listener;
  }

  @Nonnull @Override public <L extends Listener> L unregisterEvents(@Nonnull L listener) {
    getProxy().getPluginManager().unregisterListener(listener);
    return listener;
  }

  @Nonnull @Override public ModuleContainer getModuleContainer() {
    return this.moduleContainer;
  }

  @Nonnull @Override public PluginLogger getLog() {
    return this.log;
  }

  @NotNull @Override public String getName() {
    return getDescription().getName();
  }
}
