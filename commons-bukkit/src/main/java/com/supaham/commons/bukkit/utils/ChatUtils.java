package com.supaham.commons.bukkit.utils;

import com.supaham.commons.Enums;
import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import net.kyori.text.Component;
import net.kyori.text.KeybindComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatUtils {

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
  
  public static <T extends Component> T forceResetStyles(T component) {
    component = (T) component.color(null);
    for (TextDecoration textDecoration : TextDecoration.values()) {
      component = (T) component.decoration(textDecoration, false);
    }
    return component;
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
      String json = ComponentSerializer.serialize(component);
      sendJson(Collections.singleton(((Player) sender)), json);
    } else {
      sendStringComponent(sender, component);
    }
  }

  public static void sendComponent(Iterable<CommandSender> senders, Component component) {
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
      String json = ComponentSerializer.serialize(component);
      sendJson(players, json);
    }
    if (!others.isEmpty()) {
      sendStringComponent(others, component);
    }
  }

  public static void sendStringComponent(CommandSender sender, Component component) {
    sendStringComponent(Collections.singleton(sender), component);
  }

  public static void sendStringComponent(Iterable<CommandSender> senders, Component component) {
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
    return nmsFromJson(ComponentSerializer.serialize(component));
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
