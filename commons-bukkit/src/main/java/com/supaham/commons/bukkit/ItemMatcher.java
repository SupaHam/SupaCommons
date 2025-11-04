package com.supaham.commons.bukkit;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an {@link ItemStack} matcher. This class operates around {@link ItemMatcherPredicate}
 * to produce the boolean value that states whether two item stacks match. This class already comes
 * with many predicates that is basically a broken down version of each {@link ItemMeta} class.
 * All of the provided predicates in this class are static fields and are suffixed with
 * {@code _PREDICATE}
 *
 * @see #ItemMatcher(boolean)
 * @since 0.1
 */
public class ItemMatcher {

  /**
   * This predicate checks whether two items are similar.
   *
   * @see ItemStack#isSimilar(ItemStack)
   */
  public static final ItemMatcherPredicate IS_SIMILAR_PREDICATE = new IsSimilarPredicate();
  /**
   * This predicate checks whether two item types are equal.
   *
   * @see ItemStack#getType()
   */
  public static final ItemMatcherPredicate TYPE_PREDICATE = new TypePredicate();
  /**
   * This predicate checks whether two items are of the same amount.
   *
   * @see ItemStack#getAmount()
   */
  public static final ItemMatcherPredicate AMOUNT_PREDICATE = new AmountPredicate();
  /**
   * This predicate checks whether two items are of the same durability.
   *
   * @see ItemStack#getDurability()
   */
  public static final ItemMatcherPredicate DURABILITY_PREDICATE = new DurabilityPredicate();
  /**
   * This predicate checks whether two items are of the same display name. This returns true
   * if both items don't have the display name set.
   *
   * @see ItemMeta#getDisplayName()
   */
  public static final ItemMatcherPredicate NAME_PREDICATE = new NamePredicate();
  /**
   * This predicate checks whether two items are of the same lore. This returns true if both
   * items don't have the lore set.
   *
   * @see ItemMeta#getLore()
   */
  public static final ItemMatcherPredicate LORE_PREDICATE = new LorePredicate();
  /**
   * This predicate checks whether two items have the same enchantments. This returns true
   * if both items don't have any enchantments.
   *
   * @see ItemMeta#getEnchants()
   */
  public static final ItemMatcherPredicate ENCHANTMENTS_PREDICATE = new EnchantmentsPredicate();
  /**
   * This predicate checks whether two items have the same repair cost.
   *
   * @see Repairable#getRepairCost()
   */
  public static final ItemMatcherPredicate REPAIR_COST_PREDICATE = new RepairCostPredicate();
  /**
   * This predicate checks whether two books have the same title.
   *
   * @see BookMeta#getTitle()
   */
  public static final ItemMatcherPredicate BOOK_TITLE_PREDICATE = new BookTitlePredicate();
  /**
   * This predicate checks whether two books have the same author.
   *
   * @see BookMeta#getAuthor()
   */
  public static final ItemMatcherPredicate BOOK_AUTHOR_PREDICATE = new BookAuthorPredicate();
  /**
   * This predicate checks whether two books have the same pages.
   *
   * @see BookMeta#getPages()
   */
  public static final ItemMatcherPredicate BOOK_PAGES_PREDICATE = new BookPagesPredicate();
  /**
   * This predicate checks whether two items have the same firework effects. Supports both
   * {@link FireworkMeta} and {@link FireworkEffectMeta}.
   *
   * @see FireworkMeta#getEffects()
   * @see FireworkEffectMeta#getEffect()
   */
  public static final ItemMatcherPredicate FIREWORK_EFFECTS_PREDICATE =
      new FireworkEffectsPredicate();
  /**
   * This predicate checks whether two fireworks have the same power.
   *
   * @see FireworkMeta#getPower()
   */
  public static final ItemMatcherPredicate FIREWORK_POWER_PREDICATE =
      new FireworkPowerPredicate();
  /**
   * This predicate checks whether two leather armor have the same color.
   *
   * @see LeatherArmorMeta#getColor()
   */
  public static final ItemMatcherPredicate ARMOR_COLOR_PREDICATE = new ArmorColorPredicate();
  /**
   * This predicate checks whether two maps scale.
   *
   * @see MapMeta#isScaling()
   */
  public static final ItemMatcherPredicate MAP_SCALE_PREDICATE = new MapScalePredicate();
  /**
   * This predicate checks whether two potions have the same custom effects.
   *
   * @see PotionMeta#getCustomEffects()
   */
  public static final ItemMatcherPredicate POTION_EFFECTS_PREDICATE = new PotionEffectsPredicate();
  /**
   * This predicate checks whether two skulls have the same owner.
   *
   * @see SkullMeta#getOwner()
   */
  public static final ItemMatcherPredicate SKULL_OWNER_PREDICATE = new SkullOwnerPredicate();
  /**
   * This predicate checks whether two banners have the same base color.
   *
   * @see BannerMeta#getPattern
   * @see Pattern#getColor()
   */
  public static final ItemMatcherPredicate BANNER_COLOR_PREDICATE = new BannerColorPredicate();
  /**
   * This predicate checks whether two banners have the same patterns.
   *
   * @see BannerMeta#getPatterns()
   */
  public static final ItemMatcherPredicate BANNER_PATTERNS_PREDICATE =
      new BannerPatternsPredicate();

  private final Set<ItemMatcherPredicate> predicates = new HashSet<>();

  // Used by our predicates.
  private static Boolean precondition(@Nullable ItemStack item1, @Nullable ItemStack item2,
                                      boolean meta) {
    if (item1 == null && item2 == null) {
      return true;
    } else if ((item1 == null || item2 == null)) {
      return false;
    } else if (meta) {
      if (!item1.hasItemMeta() && !item2.hasItemMeta()) {
        return true;
      } else if (!item1.hasItemMeta() || !item2.hasItemMeta()) {
        return false;
      }
    }
    return null;
  }

  /**
   * Constructs a new item matcher.
   *
   * @param addDefaultPredicates whether to add the default predicates see {@link
   * #addDefaultPredicates()}
   */
  public ItemMatcher(boolean addDefaultPredicates) {
    if (addDefaultPredicates) {
      addDefaultPredicates();
    }
  }

  /**
   * Adds the following default predicates:
   * <ul>
   * <li>{@link #IS_SIMILAR_PREDICATE}</li>
   * </ul>
   *
   * @return this item match instance for chaining
   */
  public ItemMatcher addDefaultPredicates() {
    addPredicates(IS_SIMILAR_PREDICATE);
    return this;
  }

  /**
   * Adds an array of {@link ItemMatcherPredicate}s to this item matcher.
   *
   * @param predicates predicates to add
   *
   * @return this item match instance for chaining
   */
  public ItemMatcher addPredicates(@Nonnull ItemMatcherPredicate... predicates) {
    addPredicates(Arrays.asList(predicates));
    return this;
  }

  /**
   * Adds multiple {@link ItemMatcherPredicate}s to this item matcher.
   *
   * @param predicates predicates to add
   *
   * @return this item match instance for chaining
   */
  public ItemMatcher addPredicates(Iterable<ItemMatcherPredicate> predicates) {
    for (ItemMatcherPredicate predicate : predicates) {
      checkNotNull(predicate, "predicate cannot be null.");
      this.predicates.add(predicate);
    }
    return this;
  }

  /**
   * Checks whether two given {@link ItemStack}s match each other. This returns false the
   * moment any of the added predicates return false when
   * {@link ItemMatcherPredicate#apply(ItemStack, ItemStack)} is called.
   * <p />
   * If this item matcher doesn't have any {@link ItemMatcherPredicate}s false is always returned.
   *
   * @param i1 first item to match with {@code i2}
   * @param i2 second item to match with {@code i1}
   *
   * @return whether both item stacks match each other
   */
  public boolean match(@Nullable ItemStack i1, @Nullable ItemStack i2) {
    if (this.predicates.isEmpty()) {
      return false;
    }
    for (ItemMatcherPredicate predicate : this.predicates) {
      if (!predicate.apply(i1, i2)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Represents a predicate interface that passes two {@link ItemStack}s.
   */
  public static interface ItemMatcherPredicate {

    /**
     * Returns the result of applying this predicate to {@code item1} and {@code item2}. This
     * method is <i>generally expected</i>, but not absolutely required, to have the following
     * properties:
     *
     * <ul>
     * <li>Its execution does not cause any observable side effects.
     * <li>The computation is <i>consistent with equals</i>; that is, {@link Objects#equal
     * Objects.equal}{@code (a, b)} implies that {@code predicate.apply(a) ==
     * predicate.apply(b))}.
     * </ul>
     *
     * @throws NullPointerException if {@code input} is null and this predicate does not accept
     * null
     * arguments
     */
    boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2);
  }

  private static final class IsSimilarPredicate implements ItemMatcherPredicate {

    private IsSimilarPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, false);
      return b != null ? b : item1.isSimilar(item2);
    }
  }

  private static final class TypePredicate implements ItemMatcherPredicate {

    private TypePredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, false);
      return b != null ? b : item1.getType() == item2.getType();
    }
  }

  private static final class AmountPredicate implements ItemMatcherPredicate {

    private AmountPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, false);
      return b != null ? b : item1.getAmount() == item2.getAmount();
    }
  }

  private static final class DurabilityPredicate implements ItemMatcherPredicate {

    private DurabilityPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, false);
      return b != null ? b : item1.getDurability() == item2.getDurability();
    }
  }

  private static final class NamePredicate implements ItemMatcherPredicate {

    private NamePredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      return b != null ? b : item1.getItemMeta().getDisplayName()
          .equals(item2.getItemMeta().getDisplayName());
    }
  }

  private static final class LorePredicate implements ItemMatcherPredicate {

    private LorePredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      return b != null ? b : item1.getItemMeta().getLore().equals(item2.getItemMeta().getLore());
    }
  }

  private static final class EnchantmentsPredicate implements ItemMatcherPredicate {

    private EnchantmentsPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      return b != null ? b : item1.getItemMeta().getEnchants()
          .equals(item2.getItemMeta().getEnchants());
    }
  }

  private static final class RepairCostPredicate implements ItemMatcherPredicate {

    private RepairCostPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      return b != null ? b : ((Repairable) item1.getItemMeta()).getRepairCost()
                             == ((Repairable) item2.getItemMeta()).getRepairCost();
    }
  }

  private static final class BookTitlePredicate implements ItemMatcherPredicate {

    private BookTitlePredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      } else if ((!(item1.getItemMeta() instanceof BookMeta)
                  || !(item2.getItemMeta() instanceof BookMeta))) {
        return false;
      }
      return ((BookMeta) item1.getItemMeta()).getTitle()
          .equals(((BookMeta) item2.getItemMeta()).getTitle());
    }
  }

  private static final class BookAuthorPredicate implements ItemMatcherPredicate {

    private BookAuthorPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      } else if ((!(item1.getItemMeta() instanceof BookMeta)
                  || !(item2.getItemMeta() instanceof BookMeta))) {
        return false;
      }
      return ((BookMeta) item1.getItemMeta()).getAuthor()
          .equals(((BookMeta) item2.getItemMeta()).getAuthor());
    }
  }

  private static final class BookPagesPredicate implements ItemMatcherPredicate {

    private BookPagesPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      } else if ((!(item1.getItemMeta() instanceof BookMeta)
                  || !(item2.getItemMeta() instanceof BookMeta))) {
        return false;
      }
      return ((BookMeta) item1.getItemMeta()).getPages()
          .equals(((BookMeta) item2.getItemMeta()).getPages());
    }
  }

  private static final class FireworkEffectsPredicate implements ItemMatcherPredicate {

    private FireworkEffectsPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof FireworkMeta
          && item2.getItemMeta() instanceof FireworkMeta) {
        return ((FireworkMeta) item1.getItemMeta()).getEffects()
            .equals(((FireworkMeta) item2.getItemMeta()).getEffects());
      } else if (item1.getItemMeta() instanceof FireworkEffectMeta
                 && item2.getItemMeta() instanceof FireworkEffectMeta) {
        return ((FireworkEffectMeta) item1.getItemMeta()).getEffect()
            .equals(((FireworkEffectMeta) item2.getItemMeta()).getEffect());
      }
      return false;
    }
  }

  private static final class FireworkPowerPredicate implements ItemMatcherPredicate {

    private FireworkPowerPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof FireworkMeta
          && item2.getItemMeta() instanceof FireworkMeta) {
        return ((FireworkMeta) item1.getItemMeta()).getPower()
               == ((FireworkMeta) item2.getItemMeta()).getPower();
      }
      return false;
    }
  }

  private static final class ArmorColorPredicate implements ItemMatcherPredicate {

    private ArmorColorPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof LeatherArmorMeta
          && item2.getItemMeta() instanceof LeatherArmorMeta) {
        return ((LeatherArmorMeta) item1.getItemMeta()).getColor()
            .equals(((LeatherArmorMeta) item2.getItemMeta()).getColor());
      }
      return false;
    }
  }

  private static final class MapScalePredicate implements ItemMatcherPredicate {

    private MapScalePredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof MapMeta
          && item2.getItemMeta() instanceof MapMeta) {
        return ((MapMeta) item1.getItemMeta()).isScaling()
               == ((MapMeta) item2.getItemMeta()).isScaling();
      }
      return false;
    }
  }

  private static final class PotionEffectsPredicate implements ItemMatcherPredicate {

    private PotionEffectsPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof PotionMeta
          && item2.getItemMeta() instanceof PotionMeta) {
        return ((PotionMeta) item1.getItemMeta()).getCustomEffects()
            .equals(((PotionMeta) item2.getItemMeta()).getCustomEffects());
      }
      return false;
    }
  }

  private static final class SkullOwnerPredicate implements ItemMatcherPredicate {

    private SkullOwnerPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof SkullMeta
          && item2.getItemMeta() instanceof SkullMeta) {
        return ((SkullMeta) item1.getItemMeta()).getOwner()
            .equals(((SkullMeta) item2.getItemMeta()).getOwner());
      }
      return false;
    }
  }

  private static final class BannerColorPredicate implements ItemMatcherPredicate {

    private BannerColorPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof BannerMeta
          && item2.getItemMeta() instanceof BannerMeta) {
        return ((BannerMeta) item1.getItemMeta()).getPattern(0).getColor()
            .equals(((BannerMeta) item2.getItemMeta()).getPattern(0).getColor());
      }
      return false;
    }
  }

  private static final class BannerPatternsPredicate implements ItemMatcherPredicate {

    private BannerPatternsPredicate() {
    }

    @Override
    public boolean apply(@Nullable ItemStack item1, @Nullable ItemStack item2) {
      Boolean b = precondition(item1, item2, true);
      if (b != null) {
        return b;
      }
      if (item1.getItemMeta() instanceof BannerMeta
          && item2.getItemMeta() instanceof BannerMeta) {
        return ((BannerMeta) item1.getItemMeta()).getPatterns()
            .equals(((BannerMeta) item2.getItemMeta()).getPatterns());
      }
      return false;
    }
  }
}
