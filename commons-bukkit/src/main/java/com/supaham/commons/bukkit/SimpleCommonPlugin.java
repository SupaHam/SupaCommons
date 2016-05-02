package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.commands.CommonCommandsManager;
import com.supaham.commons.bukkit.modules.Module;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.bukkit.utils.SerializationUtils;
import com.supaham.commons.state.State;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import pluginbase.config.datasource.yaml.YamlDataSource;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.SendablePluginBaseException;

/**
 * Represents a simple implementation of {@link CommonPlugin}. This class throws an
 * {@link AssertionError} when {@link #SimpleCommonPlugin()} is called, so the implementation must
 * not call said constructor and instead call {@link #SimpleCommonPlugin()}
 * <p />
 * This class does the following tasks:
 * <br />
 * <ol>
 * <li>Offers the following protected fields:
 * <ul>
 * <li>moduleContainer; {@link ModuleContainer}; used to assign {@link Module}s to this class</li>
 * </ul>
 * </li>
 * <li>Within the only available constructor, this plugin is hooked into {@link CBukkitMain}.</li>
 * <li>During onLoad, onEnable, onDisable, they call the {@code pluginAgent} in their respective
 * order. So, when overriding, ensure to call said super methods.</li>
 * <li>Implements other methods provided </li>
 * </ol>
 */
public abstract class SimpleCommonPlugin<T extends SimpleCommonPlugin> extends JavaPlugin
    implements CommonPlugin {

  private State state = State.STOPPED;

  private final PluginLogger pluginLogger = PluginLogger.getLogger(this);
  protected final ModuleContainer moduleContainer = new ModuleContainer(this);
  private final SettingsContainer settingsContainer = new SettingsContainer();
  private final FirstRunContainer firstRunContainer = new FirstRunContainer();

  @Nonnull private CommonCommandsManager commandsManager = new CommonCommandsManager(this);

  private Metrics metrics;

  public SimpleCommonPlugin() {
    CBukkitMain.hook(this);
  }

  @Override
  public void onLoad() {
    if (!this.settingsContainer.init()) {
      getLog().finer("Settings disabled.");
    }
    reloadSettings();
    SimpleCommonPlugin.this.firstRunContainer.init(); // FirstRun runs after reloadSettings
  }

  @Override
  public void onEnable() {
    this.state = State.ACTIVE;
  }

  @Override
  public void onDisable() {
    this.state = State.STOPPED;
    disableMetrics(); // Automatically disable metrics if it is enabled, convenience method.
  }

  @Nonnull
  @Override
  public ModuleContainer getModuleContainer() {
    return this.moduleContainer;
  }

  @Nonnull
  @Override
  public PluginLogger getLog() {
    return this.pluginLogger;
  }

  /*
   * onCommand is overridden in order for bukkit to relay all command calls to our own executor.
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
    return getCommandsManager().getDefaultExecutor().onCommand(sender, command, alias, args);
  }

  @Nonnull
  @Override
  public CommonCommandsManager getCommandsManager() {
    return this.commandsManager;
  }

  protected void setCommandsManager(@Nonnull CommonCommandsManager commandsManager) {
    Preconditions.checkState(this.state.isIdle(), "cannot change CommonCommandsManager when plugin is enabled.");
    Preconditions.checkNotNull(commandsManager, "commandsManager cannot be null.");
    this.commandsManager = commandsManager;
  }

  @Override
  public CommonSettings getSettings() {
    return this.settingsContainer.settings;
  }

  protected void setSettings(Supplier<CommonSettings> settingsSupplier) {
    this.settingsContainer.settingsSupplier = settingsSupplier;
  }

  @Nonnull
  @Override
  public File getSettingsFile() {
    return this.settingsContainer.settingsFile;
  }

  protected void setSettingsFile(@Nonnull File file) {
    Preconditions.checkNotNull(file, "file cannot be null.");
    this.settingsContainer.settingsFile = file;
  }

  @Override
  public boolean reloadSettings() {
    return this.settingsContainer.load();
  }

  @Override
  public boolean saveSettings() {
    return this.settingsContainer.save();
  }

  @Override
  public boolean isFirstRun() {
    return this.firstRunContainer.firstRun;
  }

  public boolean hasFirstRunRunnable(@Nonnull Runnable runnable) {
    Preconditions.checkNotNull(runnable, "runnable cannot be null.");
    return this.firstRunContainer.firstRunRunnables.contains(runnable);
  }

  public boolean addFirstRunRunnable(@Nonnull Runnable runnable) {
    Preconditions.checkNotNull(runnable, "runnable cannot be null.");
    return this.firstRunContainer.firstRunRunnables.add(runnable);
  }

  public boolean removeFirstRunRunnable(@Nonnull Runnable runnable) {
    Preconditions.checkNotNull(runnable, "runnable cannot be null.");
    return this.firstRunContainer.firstRunRunnables.remove(runnable);
  }

  protected boolean enableMetrics() {
    if (this.metrics != null) {
      return false;
    }
    try {
      this.metrics = new Metrics(this);
      this.metrics.enable();
      return true;
    } catch (IOException e) {
      getLog().severe("Failed to reach Metrics server: " + e.getMessage());
      if (getLog().getDebugLevel() > 0) {
        e.printStackTrace();
      }
      return false;
    }
  }

  protected boolean disableMetrics() {
    if (this.metrics == null) {
      return false;
    }
    try {
      this.metrics.disable();
      this.metrics = null;
      return true;
    } catch (IOException e) {
      getLog().severe("Failed to reach Metrics server: " + e.getMessage());
      if (getLog().getDebugLevel() > 0) {
        e.printStackTrace();
      }
      return false;
    }
  }

  private final class SettingsContainer {

    private Supplier<CommonSettings> settingsSupplier = () -> new CommonSettings(SimpleCommonPlugin.this);
    private File settingsFile = new File(getDataFolder(), "config.yml");
    private CommonSettings settings;
    private YamlDataSource yaml;

    private boolean isEnabled() {
      return settingsSupplier != null; // not null = enabled
    }

    private boolean init() {
      if (!isEnabled()) {
        return false;
      }
      this.settings = settingsSupplier.get();
      return true;
    }

    private boolean load() {
      if (!isEnabled()) {
        throw new IllegalStateException("Settings not enabled.");
      }

      try {
        yaml = SerializationUtils.yaml(this.settingsFile).build();
        SerializationUtils.loadOrCreateProperties(getLog(), this.yaml, this.settings);
        save();
        return true;
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }

    public boolean save() {
      if (!isEnabled()) {
        throw new IllegalStateException("Settings not enabled.");
      }

      try {
        this.yaml.save(this.settings);
        return true;
      } catch (SendablePluginBaseException e) {
        e.printStackTrace();
        return false;
      }
    }
  }

  private final class FirstRunContainer {

    private List<Runnable> firstRunRunnables = new ArrayList<>();
    private boolean firstRun = false;

    private void init() {
      if (getSettings().isFirstRun()) {
        getLog().fine(getName() + " first run");
        this.firstRun = true;
        this.firstRunRunnables.forEach(Runnable::run);
        getSettings().setFirstRun(false);
        saveSettings();
      }
      this.firstRunRunnables = null;
    }
  }
}
