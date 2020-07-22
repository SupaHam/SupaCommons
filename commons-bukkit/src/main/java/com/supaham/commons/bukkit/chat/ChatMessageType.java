package com.supaham.commons.bukkit.chat;

import com.google.common.base.Preconditions;
import com.supaham.commons.bukkit.NMSVersion;
import com.supaham.commons.bukkit.title.Title;
import com.supaham.commons.bukkit.utils.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public enum ChatMessageType {
  CHAT, SYSTEM, GAME_INFO;

  public static final Class<?> chatComponentClass = ReflectionUtils.getNMSClass("IChatBaseComponent");
  public static final Class<?> chatPacket = ReflectionUtils.getNMSClass("PacketPlayOutChat");
  public static Class<? extends Enum> chatMessageTypeClass;
  private static Constructor ctor1;
  private final Object nmsMessageType;

  static {
    try {
      ctor1 = chatPacket.getDeclaredConstructor(chatComponentClass, chatMessageTypeClass, UUID.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  public static void sendMessage(Player player, ChatMessageType type, Object nmsObject, UUID uuid)
          throws IllegalAccessException, InvocationTargetException, InstantiationException {
    Object packet = ctor1.newInstance(nmsObject, type.getNmsMessageType(), uuid);
    ReflectionUtils.sendPacket(player, packet);
  }

  public static void sendMessage(Iterable<Player> players, ChatMessageType type, Object nmsObject, UUID uuid)
          throws IllegalAccessException, InvocationTargetException, InstantiationException {
    Object packet = ctor1.newInstance(nmsObject, type.getNmsMessageType(), uuid);
    for (Player player : players) {
      ReflectionUtils.sendPacket(player, packet);
    }
  }

  private static Class<? extends Enum> getChatMessageTypeClass() {
    if (chatMessageTypeClass == null) {
      chatMessageTypeClass = (Class<? extends Enum>) ReflectionUtils.getNMSClass("ChatMessageType");
    }
    return chatMessageTypeClass;
  }

  ChatMessageType() {
    nmsMessageType = Preconditions.checkNotNull(Enum.valueOf(getChatMessageTypeClass(), name()));
  }
  
  public byte getId() {
    return (byte) ordinal();
  }

  public Object getNmsMessageType() {
    return nmsMessageType;
  }
}
