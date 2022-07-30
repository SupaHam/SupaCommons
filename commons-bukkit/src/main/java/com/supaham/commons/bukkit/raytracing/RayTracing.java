package com.supaham.commons.bukkit.raytracing;

import static com.supaham.commons.bukkit.utils.ReflectionUtils.toNMSVec3D;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.ReflectionUtils;
import com.supaham.commons.bukkit.utils.ReflectionUtils.PackageType;

import org.bukkit.World;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * RayTracing utility class providing methods such as {@link #rayTraceBlocks(World, Vector,
 * Vector)}.
 *
 * @see #rayTraceBlocks(World, Vector, Vector)
 * @since 0.3.5
 */
public class RayTracing {

  private static Method rayTraceMethod;

  static {
    PackageType nms = PackageType.MINECRAFT_SERVER;
    Class<?> worldClazz = nms.getClassSafe("world.level", "World");
    rayTraceMethod = ReflectionUtils.getMethod(worldClazz, "rayTrace", nms.getClassSafe("world.phys", "Vec3D"),
                                               nms.getClassSafe("world.phys","Vec3D"),
                                               nms.getClassSafe("world.level","FluidCollisionOption"),
                                               boolean.class, boolean.class);
  }

  /**
   * Returns a {@link MovingObjectPosition} as a result of a ray trace from two {@link Vector}s.
   * This is equivalent to calling {@link #rayTraceBlocks(World, Vector, Vector, boolean, boolean,
   * boolean)} with the respective booleans provided by the {@link RayTraceData}.
   *
   * @param data ray tracing data
   *
   * @return {@link MovingObjectPosition}, nullable
   */
  @Nullable
  public static MovingObjectPosition rayTraceBlocks(@Nonnull RayTraceData data) {
    Preconditions.checkNotNull(data, "data cannot be null.");
    return rayTraceBlocks(data.getWorld(), data.getStart().toVector(), data.getEnd().toVector(),
                          data.getFluidCollision(), data.isIgnoreBoundingBox(),
                          data.isReturnLastCollidableBlock());
  }

  /**
   * Returns a {@link MovingObjectPosition} as a result of a ray trace from two {@link Vector}s.
   * This is equivalent to calling {@link #rayTraceBlocks(World, Vector, Vector, FluidCollisionOption, boolean,
   * boolean)} with the booleans as {@code false, false, false}. Meaning the ray trace won't stop
   * when it hits liquids, it won't ignore non solid blocks' bounding boxes, and it won't return
   * the last collidable block.
   *
   * @param world world to perform ray trace in
   * @param start starting point of ray trace
   * @param end end point of ray trace
   *
   * @return {@link MovingObjectPosition}, nullable
   */
  @Nullable
  public static MovingObjectPosition rayTraceBlocks(World world, Vector start, Vector end) {
    return rayTraceBlocks(world, start, end, FluidCollisionOption.NEVER, false, false);
  }

  /**
   * Returns a {@link MovingObjectPosition} as a result of a ray trace from two {@link Vector}s.
   * This is equivalent to calling {@link #rayTraceBlocks(World, Vector, Vector, FluidCollisionOption, boolean,
   * boolean)} with the last two booleans as {@code false, false}. Meaning the ray trace won't
   * ignore non solid blocks' bounding boxes, and it won't return the last collidable block.
   *
   * @param world world to perform ray trace in
   * @param start starting point of ray trace
   * @param end end point of ray trace
   * @param fluidCollision fluidCollision
   *
   * @return {@link MovingObjectPosition}, nullable
   */
  public static MovingObjectPosition rayTraceBlocks(World world, Vector start, Vector end,
                                                    FluidCollisionOption fluidCollision) {
    return rayTraceBlocks(world, start, end, fluidCollision, false, false);
  }

  /**
   * Returns a {@link MovingObjectPosition} as a result of a ray trace from two {@link Vector}s.
   * <p/>
   * {@code returnLastCollidableBlock} is useful for ensuring the result be non null. At that point
   * , it returns the {@code end} point.
   *
   * @param world world to perform ray trace in
   * @param start starting point of ray trace
   * @param end end point of ray trace
   * @param fluidCollision
   * @param ignoreBoundingBox whether to ignore non solid blocks' bounding box
   * @param returnLastCollidableBlock whether to return the last collidable block, returns non null
   *
   * @return {@link MovingObjectPosition}, nullable
   */
  public static MovingObjectPosition rayTraceBlocks(World world,
                                                    Vector start,
                                                    Vector end,
                                                    FluidCollisionOption fluidCollision,
                                                    boolean ignoreBoundingBox,
                                                    boolean returnLastCollidableBlock) {
    try {
      Object result = rayTraceMethod.invoke(ReflectionUtils.getHandle(world), toNMSVec3D(start),
                                            toNMSVec3D(end), fluidCollision.nmsEnum, ignoreBoundingBox,
                                            returnLastCollidableBlock);
      return result == null ? null : MovingObjectPosition.from(result);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  public enum FluidCollisionOption {
    NEVER, SOURCE_ONLY, ALWAYS;
    private Object nmsEnum;

    FluidCollisionOption() {
      Class<? extends Enum> clazz = (Class<? extends Enum>) PackageType.MINECRAFT_SERVER
          .getClassSafe("world.level","FluidCollisionOption");
      nmsEnum = Enum.valueOf(clazz, name());
    }
  }
}
