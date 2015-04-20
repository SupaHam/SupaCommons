package com.supaham.commons.bukkit.potion;

import org.bukkit.potion.PotionEffectType;

/**
 * A class of goodies made of potion effects such as {@link #noJump()} and {@link #noWalk()}. All
 * methods in this class returns new instances of said potion effect with the duration as 600.
 */
public class Potions {

  private static final Potion
      NO_JUMP = new Potion(PotionEffectType.JUMP.getId(), 600, 127),
      NO_WALK = new Potion(PotionEffectType.SLOW.getId(), 600, 6);

  /**
   * Returns potion with the attributes to stop the client from jumping.
   *
   * @return no jump potion effect
   */
  public static Potion noJump() {
    return new Potion(NO_JUMP);
  }

  /**
   * Returns potion with the attributes to stop the client from walking.
   *
   * @return no walk potion effect
   */
  public static Potion noWalk() {
    return new Potion(NO_WALK);
  }
}
