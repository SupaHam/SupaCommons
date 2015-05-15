package com.supaham.commons.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for working with {@code java.reflect}. This class contains methods such as
 * {@link #getServerVersion()}, and more, as well as the {@link PackageType} class for version
 * packages such {@code nms} and {@code obc}.
 *
 * @since 0.1
 */
public class ReflectionUtils {

  private static final Map<String, Class> classes = new HashMap<>();
  private static final Map<String, Method> methods = new HashMap<>();
  private static final Map<String, Field> fields = new HashMap<>();

  static {
    PackageType nms = PackageType.MINECRAFT_SERVER;
    PackageType obc = PackageType.CRAFTBUKKIT;

    classes.put("entityPlayer", nms.getClassSafe("EntityPlayer"));
    fields.put("playerConnection", getField(classes.get("entityPlayer"), "playerConnection"));
    methods.put("sendPacket", getMethod(fields.get("playerConnection").getType(), "sendPacket"));
  }

  public static void sendPacket(Player player, Object object) {
    try {
      Object connection = fields.get("playerConnection").get(getHandle(player));
      methods.get("sendPacket").invoke(connection, object);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static String getServerVersion() {
    return Bukkit.getServer().getClass().getPackage().getName().substring(23);
  }

  public static Class<?> getNMSClass(String className) {
    return PackageType.MINECRAFT_SERVER.getClassSafe(className);
  }

  public static Class<?> getOBCClass(String className) {
    return PackageType.CRAFTBUKKIT.getClassSafe(className);
  }

  public static Object getHandle(Object obj) {
    try {
      return getMethod(obj.getClass(), "getHandle").invoke(obj);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Field getField(Class<?> clazz, String name) {
    try {
      Field field = clazz.getDeclaredField(name);
      field.setAccessible(true);
      return field;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Method getMethod(Class<?> clazz, String name,
                                 Class<?>... args) {
    for (Method m : clazz.getMethods()) {
      if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args,
                                                                          m.getParameterTypes()))) {
        m.setAccessible(true);
        return m;
      }
    }
    return null;
  }

  public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
    boolean equal = true;
    if (l1.length != l2.length) {
      return false;
    }
    for (int i = 0; i < l1.length; i++) {
      if (l1[i] != l2[i]) {
        equal = false;
        break;
      }
    }
    return equal;
  }

  /**
   * An enumeration with versioned packages such as {@code nms} and {@code obc}.
   */
  public enum PackageType {
    /**
     * {@code net.minecraft.server.server_version} package.
     */
    MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
    /**
     * {@code org.bukkit.craftbukkit.server_version} package.
     */
    CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),;

    private final String path;

    PackageType(String path) {
      this.path = path;
    }

    @Override
    public String toString() {
      return this.path;
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
      return Class.forName(this + "." + className);
    }

    public Class<?> getClassSafe(String className) {
      try {
        return Class.forName(this + "." + className);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        return null;
      }
    }

    public String getPath() {
      return path;
    }
  }
}
