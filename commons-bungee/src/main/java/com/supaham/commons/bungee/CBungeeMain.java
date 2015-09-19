package com.supaham.commons.bungee;

import com.google.common.base.Preconditions;

import com.supaham.commons.CMain;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Commons main Bungeecord class.
 *
 * @since 0.3.6
 */
public class CBungeeMain {

  private static CBungeeMain instance = null;

  private final Map<String, Plugin> hookedPlugins = new HashMap<>();

  /**
   * Gets the instance of this singleton class.
   *
   * @return {@link CBungeeMain} instance.
   */
  public static CBungeeMain get() {
    if (CBungeeMain.instance == null) {
      CBungeeMain.instance = new CBungeeMain();
    }
    return CBungeeMain.instance;
  }

  public static void hook(@Nonnull Plugin plugin) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    if (CMain.get() == null) {
      CMain.main(plugin.getLogger());
    }
    CBungeeMain inst = CBungeeMain.get();

    if (inst.hookedPlugins.containsValue(plugin)) {
      return;
    }
    inst.hookedPlugins.put(plugin.getDescription().getName(), plugin);
    plugin.getLogger().fine("Hooked into commons-bukkit.");
  }

  public static boolean unhook(@Nonnull Plugin plugin) {
    Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    return CBungeeMain.instance != null
           && instance.hookedPlugins.remove(plugin.getDescription().getName()) != null;
  }

  private CBungeeMain() {
  }

  public Map<String, Plugin> getHookedPlugins() {
    return Collections.unmodifiableMap(this.hookedPlugins);
  }
}
