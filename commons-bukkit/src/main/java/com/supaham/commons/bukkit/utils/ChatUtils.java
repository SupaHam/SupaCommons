package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.Enums;
import com.supaham.commons.bukkit.text.TextParsers;
import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import net.kyori.text.BuildableComponent;
import net.kyori.text.Component;
import net.kyori.text.KeybindComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializer;
import net.kyori.text.serializer.GsonComponentSerializer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

public class ChatUtils {

  private static final GsonComponentSerializer gsonComponentSerializer = new GsonComponentSerializer();
  public static final TextComponent NEW_LINE = TextComponent.of("\n");

  protected static Class<?> nmsIChatBaseComponent = PackageType.MINECRAFT_SERVER
      .getClassSafe("IChatBaseComponent");
  protected static Class<?> nmsPacketPlayOutChat = PackageType.MINECRAFT_SERVER
      .getClassSafe("PacketPlayOutChat");
  protected static Class<?> nmsChatSerializer;
  private static Constructor nmsPacketPlayOutChatCtor;
  private static Method nmsChatSerializerSerialize;

  static {
    try {
      nmsPacketPlayOutChatCtor = nmsPacketPlayOutChat.getConstructor(nmsIChatBaseComponent);

      if (ReflectionUtils.isServer18OrHigher()) {
        nmsChatSerializer = PackageType.MINECRAFT_SERVER.getClassSafe("IChatBaseComponent$ChatSerializer");
      } else {
        nmsChatSerializer = PackageType.MINECRAFT_SERVER.getClassSafe("ChatSerializer");
      }
      nmsChatSerializerSerialize = ReflectionUtils.getMethod(nmsChatSerializer, "a", String.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns parsed legacy message using ampersand as color char.
   * @param message message to parse
   * @return parsed component
   */
  public static Component parseLegacy(@Nonnull String message) {
    Preconditions.checkNotNull(message, "message");
    message = ChatColor.translateAlternateColorCodes('&', message);
    return TextParsers.LEGACY.parse(message);
  }

  /**
   * Applies function over a {@link Component} and its children.
   *
   * @param component base component
   * @param function function to apply, returning the component to be appended to new base
   *
   * @return transformed component
   */
  public static Component transform(@Nonnull Component component, @Nonnull Function<Component, Component> function) {
    Preconditions.checkNotNull(component, "component");
    Preconditions.checkNotNull(function, "function");
    Component result = function.apply(component);
    for (Component child : component.children()) {
      result.append(transform(child, function));
    }
    return result;
  }

  public static <T extends Component> T forceResetStyles(T component) {
    component = (T) component.color(null);
    for (TextDecoration textDecoration : TextDecoration.values()) {
      component = (T) component.decoration(textDecoration, false);
    }
    return component;
  }

  public static void forceResetStyles(BuildableComponent.Builder builder) {
    builder.color(null);
    for (TextDecoration textDecoration : TextDecoration.values()) {
      builder.decoration(textDecoration, false);
    }
  }

  public static String getReadableComponent(Component component) {
    StringBuilder sb = new StringBuilder();
    getReadableComponentSingle(sb, component);
    for (Component child : component.children()) {
      getReadableComponentSingle(sb, child);
    }
    return sb.toString();
  }

  public static void getReadableComponentSingle(StringBuilder sb, Component component) {
    sb.append(textColorToBukkit(component.color()));
    if (component instanceof TextComponent) {
      sb.append(((TextComponent) component).content());
    } else if (component instanceof KeybindComponent) {
      sb.append(((KeybindComponent) component).keybind());
    } else if (component instanceof SelectorComponent) {
      sb.append(((SelectorComponent) component).pattern());
    } else {
      throw new UnsupportedOperationException("Cannot parse " + component.getClass());
    }
  }

  public static ChatColor textColorToBukkit(TextColor textColor) {
    return Enums.findByValue(ChatColor.class, textColor.name());
  }

  public static ChatColor textDecorationToBukkit(TextDecoration textDecoration) {
    if (textDecoration == TextDecoration.OBFUSCATED) {
      return ChatColor.MAGIC;
    }
    return Enums.findByValue(ChatColor.class, textDecoration.name());
  }

  public static void sendComponent(CommandSender sender, Component component) {
    if (sender instanceof Player) {
      String json = gsonComponentSerializer.serialize(component);
      sendJson(Collections.singleton(((Player) sender)), json);
    } else {
      sendStringComponent(sender, component);
    }
  }

  public static void sendComponent(Iterable<? extends CommandSender> senders, Component component) {
    List<Player> players = new ArrayList<>();
    List<CommandSender> others = new ArrayList<>();
    for (CommandSender sender : senders) {
      if (sender instanceof Player) {
        players.add(((Player) sender));
      } else {
        others.add(sender);
      }
    }
    if (!players.isEmpty()) {
      String json = gsonComponentSerializer.serialize(component);
      sendJson(players, json);
    }
    if (!others.isEmpty()) {
      sendStringComponent(others, component);
    }
  }

  public static void sendStringComponent(CommandSender sender, Component component) {
    sendStringComponent(Collections.singleton(sender), component);
  }

  public static void sendStringComponent(Iterable<? extends CommandSender> senders, Component component) {
    String str = getReadableComponent(component);
    for (CommandSender sender : senders) {
      sender.sendMessage(str);
    }
  }

  public static void sendJson(Player player, String json) {
    sendJson(Collections.singleton(player), json);
  }

  public static void sendJson(Iterable<Player> players, String json) {
    try {
      Object packet = nmsPacketPlayOutChatCtor.newInstance(nmsFromJson(json));
      for (Player player : players) {
        ReflectionUtils.sendPacket(player, packet);
      }
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      e.printStackTrace();
    }
  }

  public static Object nmsFromComponent(Component component) {
    return nmsFromJson(gsonComponentSerializer.serialize(component));
  }

  public static Object nmsFromJson(String json) {
    try {
      return nmsChatSerializerSerialize.invoke(null, json);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private ChatUtils() {
    throw new AssertionError("I don't want to chat right now.");
  }
}
