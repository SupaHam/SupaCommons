package com.supaham.commons.bukkit.entities;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.TickerTask;
import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

public class EntityRemover extends CommonModule {

  private Map<Entity, Long> expiringEntities = new HashMap<>();
  private TickerTask removerTask;
  private Listener listener = new EntityListener();

  /**
   * Constructs a new module. This module should never automatically register to the given
   * {@link ModuleContainer} as that is extremely unproductive and may cause compatibility issues.
   *
   * @param container module container to own this module.
   */
  public EntityRemover(@Nonnull ModuleContainer container) {
    super(container);
    this.removerTask = new RemoverTask(container.getPlugin(), 0, 1);
    registerTask(this.removerTask);
    registerListener(this.listener);
  }

  /**
   * Schedules an {@link Entity} for removal after the given delay in ticks.
   *
   * @param entity entity to remove
   * @param delay delay (in ticks) until the entity should be removed
   */
  public void schedule(@Nonnull Entity entity, int delay) {
    Preconditions.checkNotNull(entity, "entity cannot be null.");
    Preconditions.checkArgument(delay > 0, "delay must be larger than 0.");
    this.expiringEntities.put(entity, System.currentTimeMillis() + ((long) delay * 50));
  }

  public boolean remove(@Nonnull Entity entity) {
    return this.expiringEntities.remove(entity) != null;
  }
  
  public void clear() {
    clear(false);
  }

  public void clear(boolean removeEntities) {
    Iterator<Entity> it = EntityRemover.this.expiringEntities.keySet().iterator();
    while (it.hasNext()) {
      Entity next = it.next();
      if (removeEntities) {
        next.remove();
      }
      it.remove();
    }
  }

  private final class RemoverTask extends TickerTask {

    public RemoverTask(@Nonnull Plugin plugin, long delay, long interval) {
      super(plugin, delay, interval);
    }

    @Override
    public void run() {
      Iterator<Entry<Entity, Long>> it = EntityRemover.this.expiringEntities.entrySet().iterator();
      while (it.hasNext()) {
        Entry<Entity, Long> entry = it.next();
        if (System.currentTimeMillis() >= entry.getValue()) {
          entry.getKey().remove();
          it.remove();
        }
      }
    }
  }

  private final class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
      EntityRemover.this.expiringEntities.remove(event.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityChangeBlockEvent event) {
      EntityRemover.this.expiringEntities.remove(event.getEntity());
    }
    // TODO Do we need to check for more events?
  }
}
