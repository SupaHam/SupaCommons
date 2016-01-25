package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.commands.CommonCommandsManager;
import com.supaham.commons.bukkit.modules.Module;
import com.supaham.commons.bukkit.modules.ModuleContainer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

import pluginbase.bukkit.BukkitPluginAgent;
import pluginbase.logging.PluginLogger;
import pluginbase.plugin.PluginBase;

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
 */
public abstract class SimpleCommonPlugin<T extends SimpleCommonPlugin> extends JavaPlugin
    implements CommonPlugin {

  protected final BukkitPluginAgent<T> pluginAgent;
  protected final ModuleContainer moduleContainer = new ModuleContainer(this);

  private SimpleCommonPlugin() {
    throw new AssertionError("No, you may not.");
  }

  public SimpleCommonPlugin(@Nonnull Class<T> pluginClass, @Nonnull String commandPrefix) {
    Preconditions.checkNotNull(commandPrefix, "command prefix cannot be null.");
    CBukkitMain.hook(this);
    this.pluginAgent = BukkitPluginAgent.getPluginAgent(pluginClass, this, commandPrefix);
  }

  protected abstract CommonCommandsManager getCommandsManager();

  @Nonnull public PluginBase<T> getPluginBase() {
    return pluginAgent.getPluginBase();
  }

  @Override public void onLoad() {
    this.pluginAgent.loadPluginBase();
  }

  @Override public void onEnable() {
    this.pluginAgent.enablePluginBase();
  }

  @Override public void onDisable() {
    this.pluginAgent.disablePluginBase();
  }

  @Nonnull @Override public <L extends Listener> L registerEvents(@Nonnull L listener) {
    getServer().getPluginManager().registerEvents(listener, this);
    return listener;
  }

  @Nonnull @Override public <L extends Listener> L unregisterEvents(@Nonnull L listener) {
    HandlerList.unregisterAll(listener);
    return listener;
  }

  @Nonnull @Override public ModuleContainer getModuleContainer() {
    return this.moduleContainer;
  }

  @Nonnull @Override public PluginLogger getLog() {
    return getPluginBase().getLog();
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
    return getCommandsManager().getDefaultExecutor().onCommand(sender, command, alias, args);
  }
}
