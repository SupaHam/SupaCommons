package com.supaham.commons.bukkit.title;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.text.FancyMessage;
import com.supaham.commons.bukkit.utils.ReflectionUtils;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Title messaging system completely based on packets through static methods provided by this
 * class.
 * <p />
 * <b>Note:</b> As of 1.8, subtitles do not appear without the title. This means that in order for
 * a {@link #sendSubtitle(Player, FancyMessage)} to visually display for the player,
 * {@link #sendTitle(Player, FancyMessage)} must be sent immediately before or after the subtitle.
 *
 * @see #sendTimes(Player, int, int, int)
 * @see #sendSubtitle(Player, FancyMessage)
 * @see #sendTitle(Player, FancyMessage)
 */
public class Title {

  private static Class<?> packetClass = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
  private static Class<? extends Enum> packetActionClass = ((Class<? extends Enum>) ReflectionUtils
      .getNMSClass("PacketPlayOutTitle$EnumTitleAction"));
  private static Class<?> chatComponentClass = ReflectionUtils.getNMSClass("IChatBaseComponent");
  private static Constructor ctor1, ctor3;

  static {
    try {
      ctor1 = packetClass.getDeclaredConstructor(packetActionClass, chatComponentClass);
      ctor3 = packetClass.getDeclaredConstructor(packetActionClass, chatComponentClass,
                                                 int.class, int.class, int.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends title time properties to a {@link Player}. This is typically called before sending the
   * title in order for the title message to utilize these properties.
   *
   * @param player player to send the times to
   * @param fadeIn fade in duration (in ticks)
   * @param stay stay duration (in ticks)
   * @param fadeOut fade out duration (in ticks)
   */
  public static void sendTimes(@Nonnull Player player, int fadeIn, int stay, int fadeOut) {
    sendTitle(player, fadeIn, stay, fadeOut, null, null);
  }

  /**
   * Sends a subtitle message to a {@link Player}.
   *
   * @param player player to send the subtitle to
   * @param subtitle subtitle to send
   *
   * @see #sendTimes(Player, int, int, int)
   * @see #sendTitle(Player, FancyMessage)
   */
  public static void sendSubtitle(@Nonnull Player player, @Nullable FancyMessage subtitle) {
    Preconditions.checkNotNull(subtitle, "subtitle cannot be null.");
    sendTitle(player, null, null, null, null, subtitle);
  }

  /**
   * Sends a title message to a {@link Player}.
   *
   * @param player player to send the subtitle to
   * @param title title to send
   *
   * @see #sendTimes(Player, int, int, int)
   * @see #sendSubtitle(Player, FancyMessage)
   */
  public static void sendTitle(@Nonnull Player player, @Nonnull FancyMessage title) {
    Preconditions.checkNotNull(title, "title cannot be null.");
    sendTitle(player, null, null, null, title, null);
  }

  private static void sendTitle(@Nonnull Player player,
                                Integer fadeIn, Integer stay, Integer fadeOut,
                                @Nullable FancyMessage title, @Nullable FancyMessage subtitle) {
    Preconditions.checkNotNull(player, "player cannot be null.");
    try {
      if (fadeIn != null && stay != null && fadeOut != null) {
        Preconditions.checkArgument(fadeIn >= 0, "fadeIn must be 0 or larger.");
        Preconditions.checkArgument(stay >= 0, "stay must be 0 or larger.");
        Preconditions.checkArgument(fadeOut >= 0, "fadeOut must be 0 or larger.");
        Object packet = ctor3.newInstance(Action.TIMES.nmsAction, null, fadeIn, stay, fadeOut);
        ReflectionUtils.sendPacket(player, packet);
      }

      if (title != null) {
        Object titlePacket = ctor1.newInstance(Action.TITLE.nmsAction, title.getNMSChatObject());
        ReflectionUtils.sendPacket(player, titlePacket);
      }

      if (subtitle != null) {
        Object subtitlePacket = ctor1.newInstance(Action.SUBTITLE.nmsAction,
                                                  subtitle.getNMSChatObject());
        ReflectionUtils.sendPacket(player, subtitlePacket);
      }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private enum Action {
    TITLE, SUBTITLE, TIMES, CLEAR, RESET;

    private final Object nmsAction;

    Action() {
      nmsAction = Preconditions.checkNotNull(Enum.valueOf(packetActionClass, name()));
    }
  }

  private Title() {
    throw new AssertionError("Didn't you read the Title? Ha. ha. Read the documentation pls.");
  }
}
