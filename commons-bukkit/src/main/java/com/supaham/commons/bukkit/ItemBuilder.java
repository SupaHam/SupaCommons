package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an {@link ItemStack} builder. This class has unsafe methods which can throw
 * exceptions, though such exceptions can be suppressed using the silent fail option that is
 * provided in the builder methods.
 * <p />
 * Methods that are <b>UNSAFE</b> are mentioned as a suffix to the
 * method's documentation. An example of an unsafe method is {@link #bookTitle(String)}.
 *
 * @since 0.1
 */
public class ItemBuilder {

  private final ItemStack itemStack;
  private final boolean failSilently;

  private ItemMeta itemMeta;

  /**
   * Creates a new {@link ItemBuilder} to start building an item. This is equivalent to calling
   * {@link #builder(Material, boolean)} with the boolean as false.
   *
   * @param type type of item to build, can be changed at any point during the building process
   * through {@link #type(Material)}
   *
   * @return item builder instance
   */
  public static ItemBuilder builder(Material type) {
    return builder(type, false);
  }

  /**
   * Creates a new {@link ItemBuilder} to start building an item.
   *
   * @param type type of item to build, can be changed at any point during the building process
   * through {@link #type(Material)}
   * @param failSilently whether to fail silently (suppress exceptions) when an <b>UNSAFE</b>
   * method
   * throws an exception
   *
   * @return item builder instance
   */
  public static ItemBuilder builder(Material type, boolean failSilently) {
    return builder(new ItemStack(type), failSilently);
  }

  /**
   * Creates a new {@link ItemBuilder} to start building an item. This is equivalent to calling
   * {@link #builder(ItemStack, boolean)} with the boolean as false.
   *
   * @param item base item to start building
   *
   * @return item builder instance
   */
  public static ItemBuilder builder(ItemStack item) {
    return builder(item, false);
  }

  /**
   * Creates a new {@link ItemBuilder} to start building an item.
   *
   * @param item base item to start building
   * @param failSilently whether to fail silently (suppress exceptions) when an <b>UNSAFE</b>
   * method
   * throws an exception
   *
   * @return item builder instance
   */
  public static ItemBuilder builder(ItemStack item, boolean failSilently) {
    return new ItemBuilder(item, failSilently);
  }

  private ItemBuilder(ItemStack itemStack, boolean failSilently) {
    this.itemStack = itemStack.clone();
    this.itemMeta = itemStack.getItemMeta();
    this.failSilently = failSilently;
  }

  /**
   * Builds this item builder, producing an {@link ItemStack}.
   *
   * @return the built itemstack
   */
  public ItemStack build() {
    this.itemStack.setItemMeta(this.itemMeta);
    return this.itemStack.clone(); // Hang on to the ite
  }

  /**
   * Sets the type of item to build.
   * <p />
   * <b>WARNING: This method WILL call {@link ItemMetaCopyCat#copy(Material, ItemMeta)} to copy the
   * previous item meta.</b>
   *
   * @param type type to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder type(@Nonnull Material type) {
    if (type == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("type cannot be null.");
      }
      return this;
    }
    try {
      if (this.itemStack.getType() != type) {
        this.itemStack.setItemMeta(this.itemMeta);
        this.itemStack.setType(type);
        this.itemMeta = ItemMetaCopyCat.copy(type, this.itemMeta);
        if (this.itemMeta == null) {
          this.itemMeta = this.itemStack.getItemMeta();
        }
      }
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Sets the item's amount.
   *
   * @param amount amount to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder amount(int amount) {
    try {
      this.itemStack.setAmount(amount);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Sets the item's durability.
   *
   * @param durability durability to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder durability(int durability) {
    if (!this.failSilently) {
      Preconditions.checkArgument(durability <= Short.MAX_VALUE,
                                  "durability cannot exceed " + Short.MAX_VALUE);
    }
    try {
      this.itemStack.setDurability((short) durability);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Sets the item's display name.
   *
   * @param name display name to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder name(@Nullable String name) {
    try {
      this.itemMeta.setDisplayName(name);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Adds lore to this item.
   *
   * @param lore lore to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder lore(@Nonnull String... lore) {
    lore(Arrays.asList(lore));
    return this;
  }

  /**
   * Adds lore to this item.
   *
   * @param lore lore to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder lore(@Nonnull Collection<String> lore) {
    if (lore == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("lore cannot be null.");
      }
      return this;
    }

    try {
      if (lore.size() != 0) {
        List<String> loreList = this.itemMeta.getLore();
        if (loreList == null) {
          loreList = new ArrayList<>(lore.size());
        }
        loreList.addAll(lore);
        this.itemMeta.setLore(loreList);
      }
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Sets this item's lore. This method effectively clears the already existing lore and adds the 
   * given lore.
   *
   * @param lore lore to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder setLore(@Nonnull Collection<String> lore) {
    if (lore == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("lore cannot be null.");
      }
      return this;
    }

    try {
      this.itemMeta.setLore(!(lore instanceof List) ? new ArrayList<>(lore)
                                                    : ((List<String>) lore));
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Removes a lore, by index, from this item.
   *
   * @param index lore index to remove
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder removeLore(int index) {
    try {
      if (this.itemMeta.hasLore()) {
        this.itemMeta.getLore().remove(index);
      }
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Clears all lore from this item.
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder clearLore() {
    try {
      this.itemMeta.setLore(null);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Adds an {@link Enchantment} to this item with level set to 1. This won't override the existing
   * enchantment, if it does exist, if it's got a higher level. This is equivalent to calling
   * {@link #enchant(Enchantment, int, boolean)} with the int as 1 and the boolean as false.
   *
   * @param enchantment enchantment to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder enchant(@Nonnull Enchantment enchantment) {
    return enchant(enchantment, 1, false);
  }

  /**
   * Adds an {@link Enchantment} to this item. This WILL overwrite the existing enchantment, if it
   * does exist.
   *
   * @param enchantment enchantment to add
   * @param level level of the {@code enchantment}
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder enchant(@Nonnull Enchantment enchantment, int level) {
    return enchant(enchantment, level, true);
  }

  /**
   * Adds an {@link Enchantment} to this item.
   *
   * @param enchantment enchantment to add
   * @param level level of the {@code enchantment}
   * @param overwrite whether to overwrite the existing enchantment, if it does exist
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder enchant(@Nonnull Enchantment enchantment, int level, boolean overwrite) {
    try {
      this.itemMeta.addEnchant(enchantment, level, overwrite);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Removes an {@link Enchantment} from this item.
   *
   * @param enchantment enchantment to remove
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder removeEnchant(@Nonnull Enchantment enchantment) {
    try {
      this.itemMeta.removeEnchant(enchantment);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Sets this item's repair cost.
   *
   * @param cost repair cost
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder repairCost(int cost) {
    try {
      ((Repairable) this.itemMeta).setRepairCost(cost);
    } catch (Exception e) {
      if (!this.failSilently) {
        e.printStackTrace();
      }
    }
    return this;
  }

  /**
   * Sets this book's title, assuming it is a book.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param title title to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bookTitle(String title) {
    if (isBookMeta()) {
      try {
        ((BookMeta) this.itemMeta).setTitle(title);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this book's author, assuming it is a book.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param author author to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bookAuthor(@Nullable String author) {
    if (isBookMeta()) {
      try {
        ((BookMeta) this.itemMeta).setAuthor(author);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this a page index in this book, assuming it is a book.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param index index of the page to set
   * @param data page data to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bookSetPage(int index, @Nullable String data) {
    if (isBookMeta()) {
      try {
        ((BookMeta) this.itemMeta).setPage(index, data);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this book's pages, assuming it is a book.
   * <p />
   * <b>Note: This is a clear and write operation, for appending, see {@link
   * #bookAdd(String...)}</b>
   * <p />
   * <b>UNSAFE</b>
   *
   * @param pages pages to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bookSet(@Nonnull String... pages) {
    if (pages == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("pages cannot be null.");
      }
      return this;
    }
    if (isBookMeta()) {
      try {
        ((BookMeta) this.itemMeta).setPages(pages);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Adds pages to this book, assuming it is a book.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param pages pages to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bookAdd(@Nonnull String... pages) {
    if (pages == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("pages cannot be null.");
      }
      return this;
    }
    if (isBookMeta()) {
      try {
        ((BookMeta) this.itemMeta).addPage(pages);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Adds {@link FireworkEffect}s to this item, assuming it is a firework or firework charge.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param effects effects to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder fireworkAdd(@Nonnull FireworkEffect... effects) {
    if (effects == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("effects cannot be null.");
      }
      return this;
    }
    boolean b = isFireworkEffectMeta();
    if (b || isFireworkMeta()) {
      try {
        if (b) {
          ((FireworkEffectMeta) this.itemMeta).setEffect(effects[0]);
        } else {
          ((FireworkMeta) this.itemMeta).addEffects(effects);
        }
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Removes the first {@link FireworkEffect} on this item, assuming it is a firework or firework
   * charge. This is equivalent to calling {@link #fireworkRemove(int)} with int as 0.
   * <p />
   * <b>UNSAFE</b>
   *
   * @return this item builder instance, for chaining
   *
   * @see #fireworkRemove(int)
   */
  public ItemBuilder fireworkRemove() {
    return fireworkRemove(0);
  }

  /**
   * Removes a {@link FireworkEffect} by index from this item, assuming it is a firework. If it is
   * a
   * firework charge, its one and only effect is removed, disregarding the index given.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param index index of the firework to remove
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder fireworkRemove(int index) {
    boolean b = isFireworkEffectMeta();
    if (b || isFireworkMeta()) {
      try {
        if (b) {
          ((FireworkEffectMeta) this.itemMeta).setEffect(null);
        } else {
          ((FireworkMeta) this.itemMeta).removeEffect(index);
        }
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Clears all {@link FireworkEffect}s on this item, assuming it is a firework or firework charge.
   * <p />
   * <b>UNSAFE</b>
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder fireworkClear() {
    boolean b = isFireworkEffectMeta();
    if (b || isFireworkMeta()) {
      try {
        if (b) {
          ((FireworkEffectMeta) this.itemMeta).setEffect(null);
        } else {
          ((FireworkMeta) this.itemMeta).clearEffects();
        }
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this firework's power, assuming it is a firework.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param power power to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder fireworkPower(int power) {
    if (isFireworkMeta()) {
      try {
        ((FireworkMeta) this.itemMeta).setPower(power);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this leather armor's color, assuming the item is leather armor.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param color color to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder armorColor(@Nonnull Color color) {
    if (isLeatherArmorMeta()) {
      try {
        ((LeatherArmorMeta) this.itemMeta).setColor(color);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets whether this map scales, assuming the item is a map.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param scale whether to scale
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder mapScale(boolean scale) {
    if (isMapMeta()) {
      try {
        ((MapMeta) this.itemMeta).setScaling(scale);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this item's main effect, assuming it is a potion.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param effectType effect type to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder potionMain(@Nonnull PotionEffectType effectType) {
    if (isPotionMeta()) {
      try {
        ((PotionMeta) this.itemMeta).setMainEffect(effectType);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Adds a {@link PotionEffect} to this item, assuming it is a potion. This is equivalent to
   * calling {@link #potionAdd(PotionEffect, boolean)} with boolean as true.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param effect effect to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder potionAdd(@Nonnull PotionEffect effect) {
    return potionAdd(effect, true);
  }

  /**
   * Adds a {@link PotionEffect} to this item, assuming it is a potion.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param effect effect to add
   * @param overwrite whether to overwrite any previous potions of the same effect type
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder potionAdd(@Nonnull PotionEffect effect, boolean overwrite) {
    if (isPotionMeta()) {
      try {
        ((PotionMeta) this.itemMeta).addCustomEffect(effect, overwrite);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Removes a {@link PotionEffect} from this item, assuming it is a potion.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param effectType effect type to remove
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder potionRemove(@Nonnull PotionEffectType effectType) {
    if (isPotionMeta()) {
      try {
        ((PotionMeta) this.itemMeta).removeCustomEffect(effectType);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Clears all custom (added) {@link PotionEffect}s from this item, assuming it is a potion.
   * <p />
   * <b>UNSAFE</b>
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder potionClear() {
    if (isPotionMeta()) {
      try {
        ((PotionMeta) this.itemMeta).clearCustomEffects();
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this item's skull owner (player head), assuming it is a skull.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param owner owner to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder skull(@Nullable String owner) {
    if (isSkullMeta()) {
      try {
        ((SkullMeta) this.itemMeta).setOwner(owner);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this banner's base color, assuming it is a banner.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param color base color to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bannerColor(DyeColor color) {
    if (isBannerMeta()) {
      try {
        ((BannerMeta) this.itemMeta).setBaseColor(color);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Adds a {@link Pattern} to a specific index to this banner, assuming it is a banner.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param index index to set
   * @param pattern pattern to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bannerSet(int index, Pattern pattern) {
    if (isBannerMeta()) {
      try {
        ((BannerMeta) this.itemMeta).setPattern(index, pattern);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Sets this banner's patterns, assuming it is a banner.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param patterns patterns to set
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bannerSet(List<Pattern> patterns) {
    if (isBannerMeta()) {
      try {
        ((BannerMeta) this.itemMeta).setPatterns(patterns);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Adds {@link Pattern}s to this banner, assuming it is a banner.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param patterns patterns to add
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bannerAdd(Pattern... patterns) {
    if (patterns == null) {
      if (!this.failSilently) {
        throw new IllegalArgumentException("patterns cannot be null.");
      }
      return this;
    }
    if (isBannerMeta()) {
      try {
        for (Pattern pattern : patterns) {
          if (pattern != null) {
            ((BannerMeta) this.itemMeta).addPattern(pattern);
          } else {
            throw new NullPointerException("pattern element is null.");
          }
        }
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * Removes a pattern by index from this banner, assuming it is a banner.
   * <p />
   * <b>UNSAFE</b>
   *
   * @param index index of pattern to remove
   *
   * @return this item builder instance, for chaining
   */
  public ItemBuilder bannerRemove(int index) {
    if (isBannerMeta()) {
      try {
        ((BannerMeta) this.itemMeta).removePattern(index);
      } catch (Exception e) {
        if (!this.failSilently) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  private boolean isBookMeta() {
    if (!(this.itemMeta instanceof BookMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of BookMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isFireworkMeta() {
    if (!(this.itemMeta instanceof FireworkMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of FireworkMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isFireworkEffectMeta() {
    if (!(this.itemMeta instanceof FireworkEffectMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of FireworkEffectMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isLeatherArmorMeta() {
    if (!(this.itemMeta instanceof LeatherArmorMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of LeatherArmorMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isMapMeta() {
    if (!(this.itemMeta instanceof MapMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of MapMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isPotionMeta() {
    if (!(this.itemMeta instanceof PotionMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of PotionMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isSkullMeta() {
    if (!(this.itemMeta instanceof SkullMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of SkullMeta.");
      }
      return false;
    }
    return true;
  }

  private boolean isBannerMeta() {
    if (!(this.itemMeta instanceof BannerMeta)) {
      if (!this.failSilently) {
        throw new IllegalStateException("ItemMeta is not of BannerMeta.");
      }
      return false;
    }
    return true;
  }
}
