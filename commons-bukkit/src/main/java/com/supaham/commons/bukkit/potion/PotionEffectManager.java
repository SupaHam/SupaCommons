package com.supaham.commons.bukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.supaham.commons.Pausable;
import com.supaham.commons.bukkit.TickerTask;
import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.state.State;
import com.supaham.commons.utils.TimeUtils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
public class PotionEffectManager extends CommonModule {

  private final TickerTask expiryTask;
  private final Listener listener = new PlayerListener();
  private final Table<UUID, Integer, PotionData> entityEffects = HashBasedTable.create();

  /**
   * Constructs a new {@link PotionEffectManager}.
   *
   * @param container module container to own this module.
   *
   * @see #apply(Potion, LivingEntity)
   */
  public PotionEffectManager(@Nonnull ModuleContainer container) {
    super(container);
    this.expiryTask = new ExpiryTask(0, 1);
    registerTask(this.expiryTask);
    registerListener(this.listener);
  }

  @Override
  public boolean setState(@Nonnull State state) throws UnsupportedOperationException {
    State old = this.state;
    boolean change = super.setState(state);
    if (change) {
      switch (state) {
        case PAUSED:
          for (PotionData data : this.entityEffects.values()) {
            data.pause();
          }
          break;
        case ACTIVE:
          if (old == State.PAUSED) { // Only resume if it was previously paused
            for (PotionData data : this.entityEffects.values()) {
              data.resume();
            }
          }
          break;
      }
    }
    return change;
  }

  /**
   * Clears a specific effect registered to this manager for a {@link LivingEntity}.
   *
   * @param entity entity to clear effect from in this manager
   * @param type type of the effect to remove
   *
   * @return whether the entity was located and cleared
   *
   * @see #clear(UUID, PotionEffectType)
   */
  public boolean clear(@Nonnull LivingEntity entity, PotionEffectType type) {
    return clear(entity.getUniqueId(), type);
  }
  
  /**
   * Clears a specific effect registered to this manager for a {@link LivingEntity}.
   *
   * @param entity entity to clear effect from in this manager
   * @param potionId potion id of the effect to remove
   *
   * @return whether the entity was located and cleared
   *
   * @see #clear(UUID, int)
   */
  public boolean clear(@Nonnull LivingEntity entity, int potionId) {
    return clear(entity.getUniqueId(), potionId);
  }

  /**
   * Clears a specific effect registered to this manager for a {@link UUID}. If the entity of the
   * given UUID is active and loaded, the registered effect is removed from them.
   *
   * @param uuid uuid to clear effect from in this manager
   * @param type type of the effect to remove
   *
   * @return whether the entity was located and cleared
   *
   * @see #clear(UUID, int)
   */
  public boolean clear(@Nonnull UUID uuid, PotionEffectType type) {
    return clear(uuid, type.getId());
  }

  /**
   * Clears a specific effect registered to this manager for a {@link UUID}. If the entity of the
   * given UUID is active and loaded, the registered effect is removed from them.
   *
   * @param uuid uuid to clear effect from in this manager
   * @param potionId potion id of the effect to remove
   *
   * @return whether the entity was located and cleared
   */
  public boolean clear(@Nonnull UUID uuid, int potionId) {
    PotionData data = this.entityEffects.remove(uuid, potionId);
    if (data == null) {
      return false;
    }
    LivingEntity entity = getEntityByUUID(uuid);
    if (entity == null) {
      return false;
    }
    entity.removePotionEffect(data.type);
    return true;
  }

  /**
   * Clears all registered registered potion effects from this manager.
   *
   * @see #clearAll(UUID)
   */
  public void clearAll() {
    for (UUID uuid : this.entityEffects.rowKeySet()) {
      clearAll(uuid);
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
  public boolean clearAll(@Nonnull UUID uuid) {
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
    if (this.state == State.PAUSED) {
      data.pause();
    }
    data.apply(entity, true); // always force the first application
    this.entityEffects.put(entity.getUniqueId(), potion.getPotionId(), data);
  }

  /**
   * Handles a {@link Player} leaving the game. This clears any registered effects to the manager
   * as well as the effects applied to the player by this manager.
   *
   * @param player player to handle
   */
  public void handleSessionQuit(Player player) {
    Iterator<PotionData> it = this.entityEffects.row(player.getUniqueId()).values().iterator();
    while (it.hasNext()) {
      // Remove potion effects that are not death persistent.
      PotionData data = it.next();
      player.removePotionEffect(data.type); // remove all effects
      if (!data.potion.isSessionPersistent()) {
        it.remove();
      }
    }
  }

  /**
   * Handles the death of an entity by {@link UUID}. This merely clears any registered effects as
   * the potions themselves are already removed on death.
   *
   * @param uuid uuid of the entity to handle
   */
  public void handleDeath(UUID uuid) {
    Iterator<PotionData> it = this.entityEffects.row(uuid).values()
        .iterator();
    while (it.hasNext()) {
      // Remove potion effects that are not death persistent.
      PotionData data = it.next();
      if (!data.potion.isDeathPersistent()) {
        it.remove();
      }
    }
  }

  /**
   * Handles the effect expiration of an entity by {@link UUID}. If an effect has expired, this
   * will clear the effect registration from the manager as well as the effects applied to the
   * player by this manager. Otherwise, it will attempt to apply the effect, assuming the entity is
   * not null, using {@link PotionData#apply(LivingEntity)}.
   *
   * @param uuid uuid of the entity to handle
   */
  public void handleExpire(UUID uuid) {
    LivingEntity entity = getEntityByUUID(uuid);
    Iterator<PotionData> it = this.entityEffects.row(uuid).values().iterator();
    while (it.hasNext()) {
      PotionData data = it.next();
      if (data.isDone()) {
        it.remove();
        if (entity != null) {
          entity.removePotionEffect(data.type);
        }
      } else {
        data.apply(entity);
      }
    }
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
    return this.plugin.getServer().getPlayer(uuid);
  }

  public static final class PotionData implements Pausable {

    private final Potion potion;
    private final PotionEffectType type;
    private long expires = -1; // infinite by default
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
      if (potion.getDuration() != Integer.MAX_VALUE) {
        this.expires = System.currentTimeMillis() + ((long) potion.getDuration() * 50);
      }
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

    public boolean isDone() {
      return this.expires > -1 && System.currentTimeMillis() - this.expires >= 0;
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
      } catch (NoSuchMethodError e) { // 1.7
        return new PotionEffect(type, duration, this.potion.getAmplifier(),
                                this.potion.isAmbient());
      }
    }
  }

  private final class ExpiryTask extends TickerTask {

    public ExpiryTask(long delay, long interval) {
      super(PotionEffectManager.this.plugin, delay, interval);
    }

    @Override
    public void run() {
      Iterator<UUID> it = PotionEffectManager.this.entityEffects.rowKeySet().iterator();
      while (it.hasNext()) { // iterator in case removals during the handle process
        handleExpire(it.next());
      }
    }
  }

  private final class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
      handleSessionQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
      handleDeath(event.getEntity().getUniqueId());
    }
  }
}
