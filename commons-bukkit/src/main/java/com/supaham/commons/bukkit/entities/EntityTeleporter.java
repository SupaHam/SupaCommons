package com.supaham.commons.bukkit.entities;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.TickerTask;
import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.LinkedHashMap;
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
public class EntityTeleporter extends CommonModule implements Runnable {

  private final LinkedHashMap<Entity, Location> queuedTeleports = new LinkedHashMap<>();
  private final TickerTask tickerTask;
  private final boolean ejecting;

  public EntityTeleporter(@Nonnull ModuleContainer container) {
    this(container, 1);
  }

  public EntityTeleporter(@Nonnull ModuleContainer container, boolean ejecting) {
    this(container, 1, ejecting);
  }

  public EntityTeleporter(@Nonnull ModuleContainer container, long interval) {
    this(container, interval, true);
  }
  
  public EntityTeleporter(@Nonnull ModuleContainer container, long interval, boolean ejecting) {
    super(container);
    this.ejecting = ejecting;
    this.tickerTask = new TickerTask(plugin, 0, interval) {
      @Override public void run() {
        EntityTeleporter.this.run();
      }
    };
    registerTask(this.tickerTask);
  }

  @Override
  public void run() {
    Iterator<Entry<Entity, Location>> it = queuedTeleports.entrySet().iterator();
    if (it.hasNext()) {
      Entry<Entity, Location> entry = it.next();
      teleportRun(entry.getKey(), entry.getValue());
      it.remove();
    }
  }
  
  protected void teleportRun(Entity entity, Location location) {
    if (ejecting && entity.getVehicle() != null) {
      entity.eject();
    }
    entity.teleport(location);
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

  public boolean isEjecting() {
    return ejecting;
  }
}
