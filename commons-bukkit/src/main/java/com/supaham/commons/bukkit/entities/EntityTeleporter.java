package com.supaham.commons.bukkit.entities;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.TickerTask;
import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.state.State;

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

  public EntityTeleporter(@Nonnull ModuleContainer container) {
    this(container, 1);
  }

  public EntityTeleporter(@Nonnull ModuleContainer container, long interval) {
    super(container);
    this.tickerTask = new TickerTask(plugin, 0, interval) {
      @Override public void run() {
        EntityTeleporter.this.run();
      }
    };
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

  @Override
  public boolean setState(State state) throws UnsupportedOperationException {
    if (super.setState(state)) {
      switch (state) {
        case PAUSED:
          this.tickerTask.pause();
          break;
        case ACTIVE:
          this.tickerTask.start();
          break;
        case STOPPED:
          this.tickerTask.stop();
          break;
      }
      return true;
    }
    return false;
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
