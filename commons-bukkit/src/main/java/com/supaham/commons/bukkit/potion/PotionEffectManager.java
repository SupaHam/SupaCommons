package com.supaham.commons.bukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import com.supaham.commons.Pausable;
import com.supaham.commons.bukkit.TickerTask;
import com.supaham.commons.utils.TimeUtils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * Represents a pottion effect manager for {@link LivingEntity} instances. This class
 * utilizes the following {@link Potion} fields to implement usage and functionality:
 * <ul>
 * <li>{@link Potion#getReapplyTicks()}</li>
 * <li>{@link Potion#isHidingDuration()}</li>
 * <li>{@link Potion#isDeathPersistent()}</li>
 * <li>{@link Potion#isSessionPersistent()}</li>
 * </ul>
 * <p />
 * <b>NOTE: ONLY SUPPORTS PLAYERS AT THE MOMENT.</b>
 *
 * @see #apply(Potion, LivingEntity)
 * @since 0.2
 */
public class PotionEffectManager implements Pausable {

  private final Plugin plugin;
  private final TickerTask task;
  private final Listener listener = new PlayerListener();
  private final Table<UUID, Integer, PotionData> entityEffects = HashBasedTable.create();
  private boolean paused;

  /**
   * Constructs a new {@link PotionEffectManager} with a {@link Plugin} as the owner. The newly
   * created {@link PotionEffectManager} automatically calls {@link #start()} to get things
   * rolling.
   *
   * @param plugin plugin to own the manager
   *
   * @see #apply(Potion, LivingEntity)
   */
  public PotionEffectManager(@Nonnull Plugin plugin) {
    this.plugin = Preconditions.checkNotNull(plugin, "plugin cannot be null.");
    this.task = new EffectApplyingTask(0, 1);
    start();
    plugin.getServer().getPluginManager().registerEvents(listener, plugin);
  }

  @Override
  public boolean pause() {
    if (this.paused) {
      return false;
    } else {
      this.paused = true;
      for (PotionData data : this.entityEffects.values()) {
        data.pause();
      }
      return true;
    }
  }

  @Override
  public boolean resume() {
    if (!this.paused) {
      return false;
    } else {
      this.paused = false;
      for (PotionData data : this.entityEffects.values()) {
        data.resume();
      }
      return true;
    }
  }

  @Override
  public boolean isPaused() {
    return this.paused;
  }

  /**
   * Starts this manager. Starting the manager is important in order for it to actually manage
   * effects.
   * <p />
   * <b>Note: This method is already called in the constructor</b>
   *
   * @return whether there was a change in state, true if the task has started, false if it hasn't,
   * which may be due to it already being active
   *
   * @see #stop()
   */
  public boolean start() {
    return this.task.start();
  }

  /**
   * Stops this manager. Please note that this call does not clear any existing potions.
   *
   * @return whether there was a change in state, true if the task has stopped, false if it hasn't,
   * which may be due to it already being inactive
   *
   * @see #start()
   */
  public boolean stop() {
    return this.task.stop();
  }

  /**
   * Clears all registered registered potion effects from this manager.
   *
   * @see #clear(UUID)
   */
  public void clearAll() {
    for (UUID uuid : this.entityEffects.rowKeySet()) {
      clear(uuid);
    }
  }

  /**
   * Clears all effects registered to this manager for a {@link UUID}. If the entity of the given
   * UUID is active and loaded, the registered effects are removed from them.
   *
   * @param uuid uuid to clear from this manager
   *
   * @return whether the entity was located and cleared
   */
  public boolean clear(@Nonnull UUID uuid) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    LivingEntity entity = getEntityByUUID(uuid);
    if (entity == null) {
      this.entityEffects.row(uuid).clear();
      return false;
    }
    Iterator<PotionData> it = this.entityEffects.row(uuid).values().iterator();
    while (it.hasNext()) {
      PotionData next = it.next();
      entity.removePotionEffect(next.type);
      it.remove();
    }
    return true;
  }

  /**
   * Applies a {@link Potion} to a {@link LivingEntity} through this manager. The given
   * {@link Potion} instance is cloned to prevent instability.
   * <p />
   * This is the main method that puts all the gears together, without this method usage, the
   * manager is useless. Existing effects applied by other means may be overwritten by this call.
   *
   * @param potion potion to apply to the {@code entity}
   * @param entity entity to apply the {@code potion} to
   */
  public void apply(@Nonnull Potion potion, @Nonnull LivingEntity entity) {
    Preconditions.checkNotNull(potion, "potion cannot be null.");
    Preconditions.checkNotNull(entity, "entity cannot be null.");

    // Remove old effect
    PotionData old = this.entityEffects.remove(entity.getUniqueId(), potion.getPotionId());
    if (old != null) {
      entity.removePotionEffect(old.type);
    }

    potion = new Potion(potion);
    PotionData data = new PotionData(potion);
    if (this.paused) {
      data.pause();
    }
    data.apply(entity, true); // always force the first application
    this.entityEffects.put(entity.getUniqueId(), potion.getPotionId(), data);
  }

  /**
   * Gets a {@link PotionData} by {@link PotionEffectType} for a {@link LivingEntity}.
   *
   * @param entity entity to get data for
   * @param type type of potion to get data from
   *
   * @return potion data, nullable
   *
   * @see #getPotionData(LivingEntity, int)
   */
  public PotionData getPotionData(@Nonnull LivingEntity entity, @Nonnull PotionEffectType type) {
    Preconditions.checkNotNull(entity, "entity cannot be null.");
    Preconditions.checkNotNull(type, "potion type cannot be null.");
    return getPotionData(entity, type.getId());
  }

  /**
   * Gets a {@link PotionData} by {@link PotionEffectType} for a {@link UUID}.
   *
   * @param uuid uuid to get data for
   * @param type type of potion to get data from
   *
   * @return potion data, nullable
   *
   * @see #getPotionData(UUID, int)
   */
  public PotionData getPotionData(@Nonnull UUID uuid, @Nonnull PotionEffectType type) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    Preconditions.checkNotNull(type, "potion type cannot be null.");
    return getPotionData(uuid, type.getId());
  }

  /**
   * Gets a {@link PotionData} by integral potion id for a {@link LivingEntity}.
   *
   * @param entity entity to get data for
   * @param potionId potion to get data from
   *
   * @return potion data, nullable
   *
   * @see #getPotionData(UUID, int)
   */
  public PotionData getPotionData(@Nonnull LivingEntity entity, int potionId) {
    Preconditions.checkNotNull(entity, "entity cannot be null.");
    Preconditions.checkArgument(potionId >= 0, "potion id cannot be smaller than 0.");
    return getPotionData(entity.getUniqueId(), potionId);
  }

  /**
   * Gets a {@link PotionData} by integral potion id for a {@link UUID}.
   *
   * @param uuid uuid to get data for
   * @param potionId potion to get data from
   *
   * @return potion data, nullable
   */
  public PotionData getPotionData(@Nonnull UUID uuid, int potionId) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    Preconditions.checkArgument(potionId >= 0, "potion id cannot be smaller than 0.");
    return this.entityEffects.get(uuid, potionId);
  }

  /**
   * Gets a {@link Map} of {@link PotionEffectType} and {@link PotionData} for a
   * {@link LivingEntity}.
   *
   * @param entity entity to get potion data from
   *
   * @return unmodifiable map of potion type and potion data
   *
   * @see #getPotionDataBukkit(UUID)
   */
  public Map<PotionEffectType, PotionData> getPotionDataBukkit(@Nonnull LivingEntity entity) {
    Preconditions.checkNotNull(entity, "entity cannot be null.");
    return getPotionDataBukkit(entity.getUniqueId());
  }

  /**
   * Gets a {@link Map} of {@link PotionEffectType} and {@link PotionData} for a
   * {@link UUID}.
   *
   * @param uuid uuid to get data for
   *
   * @return unmodifiable map of potion type and potion data
   */
  public Map<PotionEffectType, PotionData> getPotionDataBukkit(@Nonnull UUID uuid) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    Map<PotionEffectType, PotionData> map = new HashMap<>();
    for (Map.Entry<Integer, PotionData> entry : getPotionData(uuid).entrySet()) {
      map.put(PotionEffectType.getById(entry.getKey()), entry.getValue());
    }
    return Collections.unmodifiableMap(map);
  }

  /**
   * Gets a {@link Map} of {@link PotionEffectType} and {@link PotionData} for a
   * {@link LivingEntity}.
   *
   * @param entity entity to get data for
   *
   * @return unmodifiable map of potion type and potion data
   *
   * @see #getPotionData(UUID)
   */
  public Map<Integer, PotionData> getPotionData(@Nonnull LivingEntity entity) {
    Preconditions.checkNotNull(entity, "entity cannot be null.");
    return getPotionData(entity.getUniqueId());
  }

  /**
   * Gets a {@link Map} of {@link PotionEffectType} and {@link PotionData} for a
   * {@link UUID}.
   *
   * @param uuid uuid to get data for
   *
   * @return unmodifiable map of potion type and potion data
   */
  public Map<Integer, PotionData> getPotionData(@Nonnull UUID uuid) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    return Collections.unmodifiableMap(this.entityEffects.row(uuid));
  }

  private LivingEntity getEntityByUUID(@Nonnull UUID uuid) {
    Preconditions.checkNotNull(uuid, "uuid cannot be null.");
    return plugin.getServer().getPlayer(uuid);
  }

  public static final class PotionData implements Pausable {

    private final Potion potion;
    private final PotionEffectType type;
    private long expires;
    private long firstApply = -1;
    private long lastApply = -1; // don't reapply the potion by default.
    private long pausedAt = -1;

    private PotionData(Potion potion) {
      Preconditions.checkArgument(potion.getPotionId() >= 0, "potion id cannot be smaller than 0.");
      Preconditions.checkArgument(potion.getAmplifier() >= 0,
                                  "amplifier cannot be smaller than 0.");
      Preconditions.checkArgument(potion.getDuration() >= 0, "duration cannot be smaller than 0.");
      this.potion = potion;
      this.type = PotionEffectType.getById(this.potion.getPotionId());

      if (this.potion.getReapplyTicks() > 0) {
        this.lastApply = 0; // allow reapplications of this effect
      }
      this.expires = System.currentTimeMillis() + (potion.getDuration() * 50);
    }

    @Override
    public boolean pause() {
      this.pausedAt = System.currentTimeMillis();
      return true;
    }

    @Override
    public boolean resume() {
      this.expires += this.pausedAt - this.firstApply; // time difference between first and pause
      this.pausedAt = -1;
      return true;
    }

    @Override
    public boolean isPaused() {
      return this.pausedAt > -1;
    }

    // TODO should these methods be public?
    private void apply(LivingEntity entity) {
      apply(entity, false);
    }

    private void apply(LivingEntity entity, boolean force) {
      if (entity == null) {
        return;
      }
      Potion pot = this.potion;
      if (this.firstApply < 0) {
        this.firstApply = System.currentTimeMillis();
      }
      if (force || (this.lastApply > -1
                    && TimeUtils.elapsed(this.lastApply, pot.getReapplyTicks() * 50))) {
        entity.addPotionEffect(getPotionEffect(), true);
        if (this.lastApply > -1) {
          this.lastApply = System.currentTimeMillis();
        }
      } else { // attempt to add by default in case it got removed somehow.
        entity.addPotionEffect(getPotionEffect(), false);
      }
    }

    private PotionEffect getPotionEffect() {
      int duration = this.potion.isHidingDuration()
                     ? Integer.MAX_VALUE
                     : ((int) ((this.expires - System.currentTimeMillis()) / 50));
      try {
        return new PotionEffect(type, duration, this.potion.getAmplifier(), this.potion.isAmbient(),
                                this.potion.isSpawningParticles());
      } catch (Exception e) { // 1.7
        if (!(e instanceof NoSuchMethodException)) {
          e.printStackTrace();
        }
        return new PotionEffect(type, duration, this.potion.getAmplifier(),
                                this.potion.isAmbient());
      }
    }
  }

  private final class EffectApplyingTask extends TickerTask {

    public EffectApplyingTask(long delay, long interval) {
      super(plugin, delay, interval);
    }

    @Override
    public void run() {
      Iterator<Cell<UUID, Integer, PotionData>> it = entityEffects.cellSet().iterator();
      while (it.hasNext()) {
        Cell<UUID, Integer, PotionData> cell = it.next();
        PotionData data = cell.getValue();
        if (System.currentTimeMillis() - data.expires >= 0) {
          it.remove();
          getEntityByUUID(cell.getRowKey()).removePotionEffect(data.type);
        } else {
          data.apply(getEntityByUUID(cell.getRowKey()));
        }
      }
    }
  }

  private final class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
      Iterator<PotionData> it = entityEffects.row(event.getPlayer().getUniqueId()).values()
          .iterator();
      while (it.hasNext()) {
        // Remove potion effects that are not session persistent.
        PotionData data = it.next();
        event.getPlayer().removePotionEffect(data.type); // remove all effects
        if (!data.potion.isSessionPersistent()) {
          it.remove();
        }
      }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
      Iterator<PotionData> it = entityEffects.row(event.getEntity().getUniqueId()).values()
          .iterator();
      while (it.hasNext()) {
        // Remove potion effects that are not death persistent.
        PotionData data = it.next();
        if (!data.potion.isDeathPersistent()) {
          it.remove();
        }
      }
    }
  }
}
