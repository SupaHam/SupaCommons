package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import com.supaham.commons.bukkit.utils.ImmutableBlockVector.ImmutableBlockVectorSerializer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.Serializer;

/**
 * Represents an immutable version of Bukkit's {@link BlockVector} class; providing stability in
 * long-term use. This class does not actually extend {@link BlockVector}, to get an instance of
 * {@link BlockVector}, call {@link #toBlockVector()}. The same applies with {@link Location},
 * {@link #toLocation(World)}.
 * <br/>
 * {@link ImmutableBlockVector}s may be cloned using {@link #ImmutableBlockVector(ImmutableVector)}
 * <br/>
 * This class functions exactly like Bukkit's {@link BlockVector}, so things like {@link
 * #equals(Object)} utilizes an {@link #EPSILON} value of 0.000001 to ensure stability.
 * <p/>
 * <b>Note: This class supports PluginBase serialization, using {@link
 * ImmutableBlockVectorSerializer}</b>
 *
 * @see #ImmutableBlockVector(ImmutableVector)
 * @see #ImmutableBlockVector(Vector)
 * @see #ImmutableBlockVector(int, int, int)
 * @since 0.3.5
 */
@SerializeWith(ImmutableBlockVectorSerializer.class)
public class ImmutableBlockVector extends ImmutableVector {

  public static final ImmutableBlockVector ZERO = new ImmutableBlockVector(0, 0, 0);
  public static final ImmutableBlockVector ONE = new ImmutableBlockVector(1, 1, 1);

  public ImmutableBlockVector(@Nonnull ImmutableVector vector) {
    this(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
  }

  public ImmutableBlockVector(@Nonnull Vector vector) {
    this(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
  }

  public ImmutableBlockVector(int x, int y, int z) {
    super(x, y, z);
  }

  public int hashCode() {
    return (Integer.valueOf((int) x).hashCode() >> 13)
           ^ (Integer.valueOf((int) y).hashCode() >> 7)
           ^ Integer.valueOf((int) z).hashCode();
  }

  @Override
  public String toString() {
    return "ImmutableBlockVector{"
           + "x=" + getBlockX()
           + ", y=" + getBlockY()
           + ", z=" + getBlockZ()
           + '}';
  }

  public static final class ImmutableBlockVectorSerializer
      implements Serializer<ImmutableBlockVector> {

    private static final Pattern PATTERN = Pattern.compile("\\s+,\\s+");

    @Nullable @Override
    public Object serialize(@Nullable ImmutableBlockVector object) throws IllegalArgumentException {
      if (object == null) {
        return null;
      }
      return object.getBlockX() + "," + object.getBlockY() + "," + object.getBlockZ();
    }

    @Nullable @Override
    public ImmutableBlockVector deserialize(@Nullable Object serialized, @Nonnull Class wantedType)
        throws IllegalArgumentException {
      if (serialized == null) {
        return null;
      }
      checkNotNullOrEmpty(serialized.toString(), "serialized string");
      String[] split = PATTERN.split(serialized.toString(), 3);
      checkArgument(split.length == 3, "string is in an invalid format.");
      return new ImmutableBlockVector(Integer.parseInt(split[0]), Integer.parseInt(split[1]),
                                      Integer.parseInt(split[2]));
    }
  }
}
