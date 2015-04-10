package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.CMain;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

/**
 * A convenient way of teleporting entities over a set tick interval (1 tick by default).
 * <p />
 * Each entity is put into a {@link LinkedHashMap}, upon the next tick interval, the entity is
 * removed from the map and teleported to their designated {@link Location}.
 *
 * @see {@link ##EntityTeleporter(Plugin)}
 * @see {@link ##EntityTeleporter(Plugin, long)}
 * @since 0.2
 */
public class EntityTeleporter extends TickerTask {

  private static EntityTeleporter INSTANCE;

  private final LinkedHashMap<Entity, Location> queuedTeleports = new LinkedHashMap<>();

  public static EntityTeleporter getInstance() {
    if (INSTANCE == null) {
      Map<String, Plugin> hooked = CBukkitMain.get().getHookedPlugins();
      Preconditions.checkArgument(!hooked.isEmpty(),
                                  "No plugins hooked into CBukkitMain to create instance");
      INSTANCE = new EntityTeleporter(hooked.values().iterator().next());
      CMain.getLogger().fine("Created " + INSTANCE + " using plugin "
                             + INSTANCE.getPlugin().getName());
    }
    return INSTANCE;
  }

  public EntityTeleporter(@Nonnull Plugin plugin) {
    this(plugin, 1);
  }

  public EntityTeleporter(@Nonnull Plugin plugin, long interval) {
    super(plugin, 0, interval);
  }

  @Override
  public void run() {
    Iterator<Entry<Entity, Location>> it = queuedTeleports.entrySet().iterator();
    if (it.hasNext()) {
      Entry<Entity, Location> entry = it.next();
      entry.getKey().teleport(entry.getValue());
      it.remove();
    }
  }

  public boolean contains(Entity entity) {
    return queuedTeleports.containsKey(entity);
  }

  public void teleport(Entity entity, Location location) {
    Preconditions.checkNotNull(entity, "entity cannot be null.");
    Preconditions.checkNotNull(location, "location cannot be null.");
    this.queuedTeleports.put(entity, location);
  }

  public LinkedHashMap<Entity, Location> getQueuedTeleports() {
    return queuedTeleports;
  }
}
