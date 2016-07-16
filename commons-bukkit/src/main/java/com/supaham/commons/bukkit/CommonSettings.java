package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Name;
import pluginbase.config.annotation.SerializableAs;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.DependentField;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.config.field.Validators;
import pluginbase.logging.PluginLogger;

/**
 * Represents common settings that provide useful functionality.
 * <ul>
 *   <li>debugLevel</li>
 *   <li>firstRun</li>
 * </ul>
 */
@SerializableAs("CommonSettings")
public class CommonSettings {
  
  @Name("config-version")
  @Comment("Configuration version - DO NOT CHANGE THIS")
  private int version = 1;

  /*
   * THE FOLLOWING CODE WAS TAKEN FROM https://github.com/dumptruckman/PluginBase/blob/be0cfa0e1e8d794e914594b6fbf5a711b608e4de/pluginbase-core/plugin/src/main/java/pluginbase/plugin/Settings.java
   * 
   */
  @Comment("0 = off, 1-3 display debug info with increasing granularity.")
  @ValidateWith(DebugLevelValidator.class)
  private final VirtualDebug debugLevel = new VirtualDebug();

  @Comment("Will make the plugin perform tasks only done on a first run (if any.)")
  private boolean firstRun = true;

  protected CommonSettings() {} // PB initializer

  public CommonSettings(@Nonnull CommonPlugin plugin) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    this.debugLevel.setLogger(plugin.getLog());
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public int getDebugLevel() {
    Integer level = debugLevel.get();
    return level != null ? level : 0;
  }

  public void setDebugLevel(int debugLevel) throws PropertyVetoException {
    Integer value = Validators.getValidator(DebugLevelValidator.class).validateChange(debugLevel, getDebugLevel());
    this.debugLevel.set(value != null ? value : 0);
  }

  public boolean isFirstRun() {
    return firstRun;
  }

  public void setFirstRun(boolean firstRun) {
    this.firstRun = firstRun;
  }

  private static class DebugLevelValidator implements Validator<Integer> {
    @Nullable
    @Override
    public Integer validateChange(@Nullable Integer newValue, @Nullable Integer oldValue) throws PropertyVetoException {
      if (newValue == null) {
        return oldValue == null ? 0 : oldValue;
      } else if (newValue >= 0 && newValue <= 3) {
        return newValue;
      } else {
        throw new IllegalArgumentException("Invalid debug level.  Please use number 0-3. (3 being many many messages!)");
      }
    }
  }

  private static class VirtualDebug extends DependentField<Integer, PluginLogger> {

    private PluginLogger logger = null;

    VirtualDebug() {
      super(0);
    }

    public void setLogger(PluginLogger logger) {
      this.logger = logger;
    }

    protected PluginLogger getDependency() {
      return logger;
    }

    @Override
    protected Integer getDependentValue() {
      return getDependency().getDebugLevel();
    }

    @Override
    protected void setDependentValue(@Nullable Integer value) {
      getDependency().setDebugLevel(value != null ? value : 0);
    }
  }
}
