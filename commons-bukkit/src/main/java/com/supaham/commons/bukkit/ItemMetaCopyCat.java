package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class that adds the possibility to create an {@link ItemMeta} from an old {@link ItemMeta} for
 * a specific {@link Material}. This class reflects the {@code org.bukkit.craftbukkit} {@link
 * ItemFactory} which contains the implementation of said feature.
 */
public class ItemMetaCopyCat {

  private static Method getItemMetaMethod;

  static {
    try {
      ItemFactory itemFactory = Bukkit.getItemFactory();
      Class<?> clazz = PackageType.CRAFTBUKKIT.getClass("inventory.CraftMetaItem");
      getItemMetaMethod = itemFactory.getClass().getDeclaredMethod("getItemMeta",
                                                                   Material.class, clazz);
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates an {@link ItemMeta} from an old {@link ItemMeta} for a specific {@link Material}. If
   * the new type does not support some of the old item meta, it will be ignored. 
   * <p />
   * A case where item meta would not be supported is if the given item meta is of type 
   * {@link BookMeta} but the given {@link Material} is something other than a book, such as a dirt
   * block. Everything but the {@link BookMeta} would be copied.
   *
   * @param material material to create new item meta for
   * @param readFrom old item meta to write to new item meta
   *
   * @return new item meta
   *
   * @throws CommonBukkitException thrown if an error occurred during the copying process.
   */
  @Nullable
  public static ItemMeta copy(@Nonnull Material material, @Nonnull ItemMeta readFrom)
      throws CommonBukkitException {
    Preconditions.checkNotNull(material, "material cannot be null.");
    Preconditions.checkNotNull(readFrom, "item meta cannot be null.");

    if (getItemMetaMethod == null) {
      throw new UnsupportedOperationException("Reflection initialization failed.");
    }
    try {
      return ((ItemMeta) getItemMetaMethod.invoke(Bukkit.getItemFactory(), material, readFrom));
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw ((CommonBukkitException)
                 new CommonBukkitException("Failed to create ItemMeta out of material " + material)
                     .initCause(e));
    }
  }
}
