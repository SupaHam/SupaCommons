package com.supaham.commons.bukkit;

import com.supaham.commons.CMain;

import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;

/**
 * Commons main bukkit class.
 */
@Getter
public class CBukkitMain {

  private static CBukkitMain instance = null;

  private final Map<String, Plugin> hookedPlugins = new HashMap<>();

  /**
   * Gets the instance of this singleton class.
   *
   * @return {@link CBukkitMain} instance.
   */
  public static CBukkitMain get() {
    if (CBukkitMain.instance == null) {
      CBukkitMain.instance = new CBukkitMain();
    }
    return CBukkitMain.instance;
  }

  public static void hook(@NonNull Plugin plugin) {
    if (CMain.get() == null) {
      CMain.main(plugin.getLogger());
    }
    CBukkitMain inst = CBukkitMain.get();

    if (inst.hookedPlugins.containsValue(plugin)) {
      return;
    }
    inst.hookedPlugins.put(plugin.getName(), plugin);
    plugin.getLogger().fine("Hooked into commons-bukkit.");
  }

  public static boolean unhook(@NonNull Plugin plugin) {
    return CBukkitMain.instance != null 
           && instance.hookedPlugins.remove(plugin.getName()) != null;
  }

  private CBukkitMain() {
  }

  public Map<String, Plugin> getHookedPlugins() {
    return Collections.unmodifiableMap(this.hookedPlugins);
  }
}
