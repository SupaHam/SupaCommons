package com.supaham.commons.bukkit.utils;

import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import net.kyori.text.Component;
import net.kyori.text.serializer.ComponentSerializer;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

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

  public static void sendComponent(Player player, Component component) {
    sendComponent(Collections.singleton(player), component);
  }

  public static void sendComponent(Iterable<Player> players, Component component) {
    String json = ComponentSerializer.serialize(component);
    sendJson(players, json);
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
