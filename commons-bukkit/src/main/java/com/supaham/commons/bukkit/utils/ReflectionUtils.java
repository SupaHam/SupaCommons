package com.supaham.commons.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
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
  private static final Map<String, Constructor> constructors = new HashMap<>();
  private static final Map<String, Method> methods = new HashMap<>();
  private static final Map<String, Field> fields = new HashMap<>();

  // This cannot be done in a static block as PackageType needs to initialize ReflectionUtils first.
  private static void init() throws Exception {
    PackageType nms = PackageType.MINECRAFT_SERVER;
    PackageType obc = PackageType.CRAFTBUKKIT;

    classes.put("entityPlayer", nms.getClassSafe("EntityPlayer"));
    fields.put("playerConnection", getField(classes.get("entityPlayer"), "playerConnection"));
    methods.put("sendPacket", getMethod(fields.get("playerConnection").getType(), "sendPacket"));

    Class<?> clazz = nms.getClassSafe("Vec3D");
    classes.put("Vec3D", clazz);
    constructors.put("Vec3D",
                     clazz.getDeclaredConstructor(double.class, double.class, double.class));
    fields.put("Vec3Dx", getField(clazz, "a"));
    fields.put("Vec3Dy", getField(clazz, "b"));
    fields.put("Vec3Dz", getField(clazz, "c"));

    classes.put("BlockPosition", nms.getClassSafe("BlockPosition"));
    clazz = nms.getClassSafe("BaseBlockPosition");
    fields.put("BaseBlockPositionx", getField(clazz, "a"));
    fields.put("BaseBlockPositiony", getField(clazz, "c"));
    fields.put("BaseBlockPositionz", getField(clazz, "d"));
  }

  public static void sendPacket(Player player, Object object) {
    try {
      Object connection = fields.get("playerConnection").get(getHandle(player));
      methods.get("sendPacket").invoke(connection, object);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static Object toNMSVec3D(Vector vector) {
    Class<?> vec3D = PackageType.MINECRAFT_SERVER.getClassSafe("Vec3D");
    try {
      Constructor<?> ctor = constructors.get("Vec3D");
      return ctor.newInstance(vector.getX(), vector.getY(), vector.getZ());
    } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Vector fromNMSVec3D(Object object) {
    if (!object.getClass().equals(PackageType.MINECRAFT_SERVER.getClassSafe("Vec3D"))) {
      return null;
    }
    try {
      double x = (double) fields.get("Vec3Dx").get(object);
      double y = (double) fields.get("Vec3Dy").get(object);
      double z = (double) fields.get("Vec3Dz").get(object);
      return new Vector(x, y, z);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Vector fromNMSBlockPosition(Object object) {
    if (!object.getClass().equals(PackageType.MINECRAFT_SERVER.getClassSafe("BlockPosition"))) {
      return null;
    }
    try {
      int x = (int) fields.get("BaseBlockPositionx").get(object);
      int y = (int) fields.get("BaseBlockPositiony").get(object);
      int z = (int) fields.get("BaseBlockPositionz").get(object);
      return new Vector(x, y, z);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Entity getBukkitEntity(Object obj) {
    try {
      return (Entity) getMethod(obj.getClass(), "getBukkitEntity").invoke(obj);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String getServerVersion() {
    // org.bukkit.craftbukkit.v1_7_R4.entity
    // org.bukkit.craftbukkit.v1_8_R2.entity;
    return Bukkit.getServer().getClass().getPackage().getName().substring(23);
  }

  public static boolean isServer18() {
    return getServerVersion().startsWith("v1_8");
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

    static {
      try {
        ReflectionUtils.init();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

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
