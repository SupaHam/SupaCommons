package com.supaham.commons.bukkit.raytracing;

import com.supaham.commons.bukkit.utils.ImmutableBlockVector;
import com.supaham.commons.bukkit.utils.ImmutableVector;
import com.supaham.commons.bukkit.utils.ReflectionUtils;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;

/**
 * Represents a possible moving object position, used by {@link RayTracing}.
 *
 * @since 0.3.5
 */
public class MovingObjectPosition {

  private Type type;
  private BlockFace direction;
  private ImmutableBlockVector blockPosition;
  private ImmutableVector position;
  private Entity entity;

  private MovingObjectPosition() {}

  protected static MovingObjectPosition from(Object object) {
    return MakeFrom.from(object);
  }

  public MovingObjectPosition(BlockFace direction, ImmutableVector position) {
    this(Type.BLOCK, direction, ImmutableBlockVector.ZERO, position, null);
  }
  
  public MovingObjectPosition(Type type, BlockFace direction, ImmutableBlockVector blockPosition,
                              ImmutableVector position, Entity entity) {
    this.type = type;
    this.direction = direction;
    this.blockPosition = blockPosition;
    this.position = position;
    this.entity = entity;
  }

  @Override public String toString() {
    return "MovingObjectPosition{"
           + "type=" + this.type
           + ", dir=" + this.direction
           + ", blockPos=" + this.blockPosition
           + ", pos=" + this.position
           + ", entity=" + this.entity
           + '}';
  }

  /**
   * Returns the type of this object ray trace result.
   *
   * @return type
   */
  public Type getType() {
    return type;
  }

  /**
   * Gets the {@link BlockFace} of where the block was hit.
   *
   * @return block face
   */
  public BlockFace getDirection() {
    return direction;
  }

  /**
   * Returns an {@link ImmutableBlockVector} of where the block hit
   *
   * @return block vector
   */
  public ImmutableBlockVector getBlockPosition() {
    return blockPosition;
  }

  /**
   * Returns an {@link ImmutableVector} representing the exact position the ray trace hit.
   *
   * @return vector
   */
  public ImmutableVector getPosition() {
    return position;
  }

  /**
   * Returns the {@link Entity} that was hit as a result of the ray trace.
   *
   * @return entity
   */
  public Entity getEntity() {
    return entity;
  }

  /**
   * Represents a type of event that a {@link MovingObjectPosition} consists of.
   */
  public enum Type {
    /**
     * Represents a miss event, where no block or entity was found. This only happens if the ray
     * trace specifies returning the last collidable block
     */
    MISS,
    /**
     * Represents a block hit event.
     */
    BLOCK,
    /**
     * Represents an entity hit event.
     */
    ENTITY
  }

  private static final class MakeFrom {

    private static Field blockPos;
    private static Field type;
    private static Field direction;
    private static Field pos;
    private static Field entity;

    static {
      Class nmsMOP = ReflectionUtils.getNMSClass("MovingObjectPosition");
      try {
        blockPos = nmsMOP.getDeclaredField("e");
        blockPos.setAccessible(true);
        type = nmsMOP.getDeclaredField("type");
        direction = nmsMOP.getDeclaredField("direction");
        pos = nmsMOP.getDeclaredField("pos");
        entity = nmsMOP.getDeclaredField("entity");
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }

    public static MovingObjectPosition from(Object object) {
      try {
        MovingObjectPosition mop = new MovingObjectPosition();
        Type type = Type.valueOf(((Enum) MakeFrom.type.get(object)).name());
        mop.type = type;
        //noinspection ConstantConditions
        mop.position = new ImmutableVector(ReflectionUtils.fromNMSVec3D(MakeFrom.pos.get(object)));
        if (type == Type.ENTITY) {
          mop.entity = ReflectionUtils.getBukkitEntity(MakeFrom.entity.get(object));
        } else {
          mop.blockPosition = new ImmutableBlockVector(
              ReflectionUtils.fromNMSBlockPosition(MakeFrom.blockPos.get(object)));
          mop.direction = BlockFace.valueOf(((Enum) MakeFrom.direction.get(object)).name());
        }
        return mop;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        return null;
      }
    }
  }
}
