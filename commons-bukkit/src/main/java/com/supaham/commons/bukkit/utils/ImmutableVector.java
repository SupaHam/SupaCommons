package com.supaham.commons.bukkit.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.supaham.commons.utils.NumberUtils.roundExact;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.ImmutableVector.ImmutableVectorSerializer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.Serializer;

/**
 * Represents an immutable version of Bukkit's {@link Vector} class; providing stability in
 * long-term use. This class does not actually extend {@link Vector}, to get an instance of
 * {@link Vector}, call {@link #toVector()}. The same applies with {@link Location},
 * {@link #toLocation(World)}.
 * <br/>
 * {@link ImmutableVector}s may be cloned using {@link #ImmutableVector(ImmutableVector)}
 * <br/>
 * This class functions exactly like Bukkit's {@link Vector}, so things like {@link
 * #equals(Object)} utilizes an {@link #EPSILON} value of 0.000001 to ensure stability.
 * <p/>
 * <b>Note: This class supports PluginBase serialization, using {@link
 * ImmutableVectorSerializer}</b>
 *
 * @see #ImmutableVector(double, double, double)
 * @see #ImmutableVector(float, float, float)
 * @see #ImmutableVector(ImmutableVector)
 * @see #ImmutableVector(Vector)
 * @since 0.3.5
 */
@SerializeWith(ImmutableVectorSerializer.class)
public class ImmutableVector {

  public static final ImmutableVector ZERO = new ImmutableVector(0, 0, 0);
  public static final ImmutableVector ONE = new ImmutableVector(1, 1, 1);
  /**
   * Threshold for fuzzy equals().
   */
  private static final double EPSILON = 0.000001;

  protected final double x;
  protected final double y;
  protected final double z;

  public ImmutableVector(@Nonnull ImmutableVector vector) {
    Preconditions.checkNotNull(vector, "vector cannot be null.");
    this.x = vector.x;
    this.y = vector.y;
    this.z = vector.z;
  }

  public ImmutableVector(@Nonnull Vector vector) {
    Preconditions.checkNotNull(vector, "vector cannot be null.");
    this.x = vector.getX();
    this.y = vector.getY();
    this.z = vector.getZ();
  }

  public ImmutableVector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public ImmutableVector(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override public boolean equals(Object obj) {
    if (!(obj instanceof ImmutableVector)) {
      return false;
    }
    ImmutableVector o = (ImmutableVector) obj;
    return Math.abs(x - o.x) < EPSILON && Math.abs(y - o.y) < EPSILON && Math.abs(z - o.z) < EPSILON
           && (this.getClass().equals(o.getClass()));
  }

  public boolean equals(Vector o) {
    return Math.abs(x - o.getX()) < EPSILON && Math.abs(y - o.getY()) < EPSILON
           && Math.abs(z - o.getZ()) < EPSILON;
  }

  @Override public int hashCode() {
    byte hash = 7;
    int hash1 = 79 * hash + (int) (Double.doubleToLongBits(this.x)
                                   ^ Double.doubleToLongBits(this.x) >>> 32);
    hash1 = 79 * hash1 + (int) (Double.doubleToLongBits(this.y)
                                ^ Double.doubleToLongBits(this.y) >>> 32);
    hash1 = 79 * hash1 + (int) (Double.doubleToLongBits(this.z)
                                ^ Double.doubleToLongBits(this.z) >>> 32);
    return hash1;
  }

  @Override public String toString() {
    return "ImmutableVector{"
           + "x=" + this.x
           + ", y=" + this.y
           + ", z=" + this.z
           + '}';
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector plus another {@link ImmutableVector}. If
   * the given vector is equals to {@link #ZERO} this same ImmutableVector instance is returned.
   *
   * @param o the other vector to add
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector add(ImmutableVector o) {
    return ZERO.equals(o) ? this : new ImmutableVector(this.x + o.x, this.y + o.y, this.z + o.z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector plus another {@link Vector}. If
   * the given vector is equals to {@link #ZERO} this same ImmutableVector instance is returned.
   *
   * @param o the other vector to add
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector add(Vector o) {
    return ZERO.equals(o) ? this : new ImmutableVector(this.x + o.getX(),
                                                       this.y + o.getY(),
                                                       this.z + o.getZ());
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector plus the three given components. If
   * the given components are equal to 0 this same ImmutableVector instance is returned.
   *
   * @param x how much to add on the x axis
   * @param y how much to add on the y axis
   * @param z how much to add on the z axis
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector add(double x, double y, double z) {
    return x == 0 && y == 0 && z == 0 ? this
                                      : new ImmutableVector(this.x + x, this.y + y, this.z + z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector plus the given int constant on all axes.
   * If the given constant is equal to 0, this same ImmutableVector instance is returned.
   *
   * @param a constant to add to all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector add(int a) {
    return a == 0 ? this : new ImmutableVector(this.x + a, this.y + a, this.z + a);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector plus the given double constant on all
   * axes. If the given constant is equal to 0, this same ImmutableVector instance is returned.
   *
   * @param a constant to add to all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector add(double a) {
    return a == 0 ? this : new ImmutableVector(this.x + a, this.y + a, this.z + a);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector plus the given float constant on all
   * axes. If the given constant is equal to 0, this same ImmutableVector instance is returned.
   *
   * @param a constant to add to all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector add(float a) {
    return a == 0 ? this : new ImmutableVector(this.x + (double) a, this.y + (double) a,
                                               this.z + (double) a);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector minus another {@link ImmutableVector}. If
   * the given vector is equals to {@link #ZERO} this same ImmutableVector instance is returned.
   *
   * @param o the other vector to subtract
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector subtract(ImmutableVector o) {
    return ZERO.equals(o) ? this : new ImmutableVector(this.x - o.x, this.y - o.y, this.z - o.z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector minus another {@link Vector}. If
   * the given vector is equals to {@link #ZERO} this same ImmutableVector instance is returned.
   *
   * @param o the other vector to subtract
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector subtract(Vector o) {
    return ZERO.equals(o) ? this : new ImmutableVector(this.x - o.getX(),
                                                       this.y - o.getY(),
                                                       this.z - o.getZ());
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector minus the three given components. If
   * the given components are equal to 0 this same ImmutableVector instance is returned.
   *
   * @param x how much to subtract on the x axis
   * @param y how much to subtract on the y axis
   * @param z how much to subtract on the z axis
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector subtract(double x, double y, double z) {
    return x == 0 && y == 0 && z == 0 ? this
                                      : new ImmutableVector(this.x - x, this.y - y, this.z - z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector minus the given int constant on all axes.
   * If the given constant is equal to 0, this same ImmutableVector instance is returned.
   *
   * @param a constant to subtract from all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector subtract(int a) {
    return a == 0 ? this : new ImmutableVector(this.x - a, this.y - a, this.z - a);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector minus the given double constant on all
   * axes. If the given constant is equal to 0, this same ImmutableVector instance is returned.
   *
   * @param a constant to subtract from all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector subtract(double a) {
    return a == 0 ? this : new ImmutableVector(this.x - a, this.y - a, this.z - a);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector minus the given float constant on all
   * axes. If the given constant is equal to 0, this same ImmutableVector instance is returned.
   *
   * @param a constant to subtract from all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector subtract(float a) {
    return a == 0 ? this : new ImmutableVector(this.x - (double) a, this.y - (double) a,
                                               this.z - (double) a);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector divided by another {@link
   * ImmutableVector}. If the given vector is equals to {@link #ONE} this same ImmutableVector
   * instance is returned.
   *
   * @param o the other vector to divide by
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector divide(ImmutableVector o) {
    return ONE.equals(o) ? this : new ImmutableVector(this.x / o.x, this.y / o.y, this.z / o.z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector divided by another {@link Vector}. If
   * the given vector is equals to {@link #ONE} this same ImmutableVector instance is returned.
   *
   * @param o the other vector to divide by
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector divide(Vector o) {
    return ONE.equals(o) ? this : new ImmutableVector(this.x / o.getX(),
                                                      this.y / o.getY(),
                                                      this.z / o.getZ());
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector divided by the three given components. If
   * the given components are equal to 1 this same ImmutableVector instance is returned.
   *
   * @param x how much to divide by on the x axis
   * @param y how much to divide by on the y axis
   * @param z how much to divide by on the z axis
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector divide(double x, double y, double z) {
    return x == 1 && y == 1 && z == 1 ? this
                                      : new ImmutableVector(this.x / x, this.y / y, this.z / z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector divided by the given int constant on all
   * axes. If the given constant is equal to 1, this same ImmutableVector instance is returned.
   *
   * @param d constant to divide by for all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector divide(int d) {
    return d == 1 ? this : new ImmutableVector(this.x / d, this.y / d, this.z / d);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector divided by the given double constant on
   * all axes. If the given constant is equal to 1, this same ImmutableVector instance is returned.
   *
   * @param d constant to divide by for all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector divide(double d) {
    return d == 1 ? this : new ImmutableVector(this.x / d, this.y / d, this.z / d);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector divided by the given float constant on
   * all axes. If the given constant is equal to 1, this same ImmutableVector instance is returned.
   *
   * @param d constant to divide by for all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector divide(float d) {
    return d == 1 ? this : new ImmutableVector(this.x / (double) d, this.y / (double) d,
                                               this.z / (double) d);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector multiplied by another {@link
   * ImmutableVector}. If the given vector is equals to {@link #ONE} this same ImmutableVector
   * instance is returned.
   *
   * @param o the other vector to multiply by
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector multiply(ImmutableVector o) {
    return ONE.equals(o) ? this : new ImmutableVector(this.x * o.x, this.y * o.y, this.z * o.z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector multiplied by another {@link Vector}. If
   * the given vector is equals to {@link #ONE} this same ImmutableVector instance is returned.
   *
   * @param o the other vector to multiply by
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector multiply(Vector o) {
    return ONE.equals(o) ? this : new ImmutableVector(this.x * o.getX(),
                                                      this.y * o.getY(),
                                                      this.z * o.getZ());
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector multiplied by the three given components.
   * If the given components are equal to 1 this same ImmutableVector instance is returned.
   *
   * @param x how much to multiply by on the x axis
   * @param y how much to multiply by on the y axis
   * @param z how much to multiply by on the z axis
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector multiply(double x, double y, double z) {
    return x == 1 && y == 1 && z == 1 ? this
                                      : new ImmutableVector(this.x * x, this.y * y, this.z * z);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector multiplied by the given int constant on
   * all axes. If the given constant is equal to 1, this same ImmutableVector instance is returned.
   *
   * @param m constant to multiply by for all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector multiply(int m) {
    return m == 1 ? this : new ImmutableVector(this.x * m, this.y * m, this.z * m);
  }

  /**
   * Creates a new {@link ImmutableVector} of this vector multiplied by the given double constant
   * on all axes. If the given constant is equal to 1, this same ImmutableVector instance is
   * returned.
   *
   * @param m constant to multiply by for all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector multiply(double m) {
    return m == 1 ? this : new ImmutableVector(this.x * m, this.y * m, this.z * m);
  }

  /**
   * Returns a new {@link ImmutableVector} of this vector multiplied by the given float constant
   * on all axes. If the given constant is equal to 1, this same ImmutableVector instance is
   * returned.
   *
   * @param m constant to multiply by for all three components
   *
   * @return a new {@link ImmutableVector} or the same instance if no modification was made
   */
  public ImmutableVector multiply(float m) {
    return m == 1 ? this : new ImmutableVector(this.x * (double) m, this.y * (double) m,
                                               this.z * (double) m);
  }

  /**
   * Returns a new {@link ImmutableVector} of the midpoint vector between this vector and another
   * {@link ImmutableVector}. If the given constant is {@link #equals(Object)} to the given vector
   * {@code o}, this same ImmutableVector instance is returned.
   *
   * @param o the other vector
   *
   * @return a new {@link ImmutableVector} of the midpoint
   */
  public ImmutableVector midpoint(ImmutableVector o) {
    return equals(o) ? this
                     : new ImmutableVector((this.x + o.x) / 2.0D,
                                           (this.y + o.y) / 2.0D,
                                           (this.z + o.z) / 2.0D);
  }

  /**
   * Returns a new {@link ImmutableVector} of the midpoint vector between this vector and another
   * {@link Vector}. If the given constant is {@link #equals(Vector)} to the given vector {@code o}
   * , this same ImmutableVector instance is returned.
   *
   * @param o the other vector
   *
   * @return a new {@link ImmutableVector} of the midpoint
   */
  public ImmutableVector midpoint(Vector o) {
    return equals(o) ? this
                     : new ImmutableVector((this.x + o.getX()) / 2.0D,
                                           (this.y + o.getY()) / 2.0D,
                                           (this.z + o.getZ()) / 2.0D);
  }

  /**
   * Returns a new {@link ImmutableVector} of the cross product of this vector with another {@link
   * ImmutableVector}. The cross product is defined as:
   * <ul>
   * <li>x = y1 * z2 - y2 * z1
   * <li>y = z1 * x2 - z2 * x1
   * <li>z = x1 * y2 - x2 * y1
   * </ul>
   *
   * @param o the other vector
   *
   * @return a new {@link ImmutableVector} of the cross product
   */
  public ImmutableVector crossProduct(ImmutableVector o) {
    return equals(o) ? this
                     : new ImmutableVector(this.y * o.z - o.y * this.z,
                                           this.z * o.x - o.z * this.x,
                                           this.x * o.y - o.x * this.y);
  }

  /**
   * Returns a new {@link ImmutableVector} of the cross product of this vector with another {@link
   * Vector}. The cross product is defined as:
   * <ul>
   * <li>x = y1 * z2 - y2 * z1
   * <li>y = z1 * x2 - z2 * x1
   * <li>z = x1 * y2 - x2 * y1
   * </ul>
   *
   * @param o the other vector
   *
   * @return a new {@link ImmutableVector} of the cross product
   */
  public ImmutableVector crossProduct(Vector o) {
    return equals(o) ? this
                     : new ImmutableVector(this.y * o.getZ() - o.getY() * this.z,
                                           this.z * o.getX() - o.getZ() * this.x,
                                           this.x * o.getY() - o.getX() * this.y);
  }

  /**
   * Returns a new {@link ImmutableVector} of this vector to a unit vector (a vector with length
   * of 1).
   *
   * @return a new {@link ImmutableVector} of the unit vector
   */
  public ImmutableVector normalize() {
    double length = this.length();
    return new ImmutableVector(this.x / length, this.y / length, this.z / length);
  }

  /**
   * Returns the magnitude of this vector, defined as sqrt(x^2+y^2+z^2). The value of this method
   * is not cached and uses a costly square-root function, so do not repeatedly call this method to
   * get the vector's magnitude. NaN will be returned if the inner result of the sqrt() function
   * overflows, which will be caused if the length is too long.
   *
   * @return the magnitude
   */
  public double length() {
    return Math.sqrt(NumberConversions.square(this.x) + NumberConversions.square(this.y)
                     + NumberConversions.square(this.z));
  }

  /**
   * Returns the magnitude of this vector squared.
   *
   * @return the squared magnitude
   */
  public double lengthSquared() {
    return NumberConversions.square(this.x) + NumberConversions.square(this.y)
           + NumberConversions.square(this.z);
  }

  /**
   * Returns the distance between this vector and another {@link ImmutableVector}. The value of
   * this
   * method is not cached and uses a costly square-root function, so do not
   * repeatedly call this method to get the vector's magnitude. NaN will be
   * returned if the inner result of the sqrt() function overflows, which
   * will be caused if the distance is too long.
   *
   * @param o the other vector
   *
   * @return the distance
   */
  public double distance(ImmutableVector o) {
    return Math.sqrt(NumberConversions.square(this.x - o.x)
                     + NumberConversions.square(this.y - o.y)
                     + NumberConversions.square(this.z - o.z));
  }

  /**
   * Returns the distance between this vector and another {@link Vector}. The value of this
   * method is not cached and uses a costly square-root function, so do not
   * repeatedly call this method to get the vector's magnitude. NaN will be
   * returned if the inner result of the sqrt() function overflows, which
   * will be caused if the distance is too long.
   *
   * @param o the other vector
   *
   * @return the distance
   */
  public double distance(Vector o) {
    return Math.sqrt(NumberConversions.square(this.x - o.getX())
                     + NumberConversions.square(this.y - o.getY())
                     + NumberConversions.square(this.z - o.getZ()));
  }

  /**
   * Returns the squared distance between this vector and another {@link ImmutableVector}.
   *
   * @param o the other vector
   *
   * @return the distance
   */
  public double distanceSquared(ImmutableVector o) {
    return NumberConversions.square(this.x - o.x)
           + NumberConversions.square(this.y - o.y)
           + NumberConversions.square(this.z - o.z);
  }

  /**
   * Returns the squared distance between this vector and another {@link Vector}.
   *
   * @param o the other vector
   *
   * @return the distance
   */
  public double distanceSquared(Vector o) {
    return NumberConversions.square(this.x - o.getX())
           + NumberConversions.square(this.y - o.getY())
           + NumberConversions.square(this.z - o.getZ());
  }

  /**
   * Returns the angle between this vector and another in radians.
   *
   * @param o the other vector
   *
   * @return angle in radians
   */
  public float angle(Vector o) {
    double dot = this.dot(o) / (this.length() * o.length());
    return (float) Math.acos(dot);
  }

  /**
   * Calculates the dot product of this vector with another {@link ImmutableVector}. The dot
   * product is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
   *
   * @param o the other vector
   *
   * @return dot product
   */
  public double dot(ImmutableVector o) {
    return this.x * o.x + this.y * o.y + this.z * o.z;
  }

  /**
   * Calculates the dot product of this vector with another {@link Vector}. The dot
   * product is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
   *
   * @param o the other vector
   *
   * @return dot product
   */
  public double dot(Vector o) {
    return this.x * o.getX() + this.y * o.getY() + this.z * o.getZ();
  }

  public boolean isSameBlock(ImmutableVector o) {
    return o != null
           && getBlockX() == o.getBlockX()
           && getBlockY() == o.getBlockY()
           && getBlockZ() == o.getBlockZ();
  }

  /**
   * Returns whether this vector is in an axis-aligned bounding box of {@link ImmutableVector}s.
   * <b>The minimum and maximum vectors given must be truly the minimum and maximum X, Y and Z
   * components.</b>
   *
   * @param min minimum vector
   * @param max maximum vector
   *
   * @return whether this vector is in the AABB
   */
  public boolean isInAABB(ImmutableVector min, ImmutableVector max) {
    return !(min == null || max == null)
           && this.x >= min.x && this.x <= max.x
           && this.y >= min.y && this.y <= max.y
           && this.z >= min.z && this.z <= max.z;
  }

  /**
   * Returns whether this vector is in an axis-aligned bounding box of {@link Vector}s.
   * <b>The minimum and maximum vectors given must be truly the minimum and maximum X, Y and Z
   * components.</b>
   *
   * @param min minimum vector
   * @param max maximum vector
   *
   * @return whether this vector is in the AABB
   */
  public boolean isInAABB(Vector min, Vector max) {
    return !(min == null || max == null)
           && this.x >= min.getX() && this.x <= max.getX()
           && this.y >= min.getY() && this.y <= max.getY()
           && this.z >= min.getZ() && this.z <= max.getZ();
  }

  /**
   * Returns whether this vector is within a sphere.
   *
   * @param origin sphere origin
   * @param radius sphere radius
   *
   * @return whether this vector is in the sphere
   */
  public boolean isInSphere(ImmutableVector origin, double radius) {
    return origin != null
           && NumberConversions.square(origin.x - this.x)
              + NumberConversions.square(origin.y - this.y)
              + NumberConversions.square(origin.z - this.z) <= NumberConversions.square(radius);
  }

  /**
   * Returns whether this vector is within a sphere.
   *
   * @param origin sphere origin
   * @param radius sphere radius
   *
   * @return whether this vector is in the sphere
   */
  public boolean isInSphere(Vector origin, double radius) {
    return origin != null
           && NumberConversions.square(origin.getX() - this.x)
              + NumberConversions.square(origin.getY() - this.y)
              + NumberConversions.square(origin.getZ() - this.z) <= NumberConversions
                  .square(radius);
  }

  /**
   * Returns a mutable {@link Vector} using this vector's components.
   *
   * @return new mutable vector
   */
  public Vector toVector() {
    return new Vector(this.x, this.y, this.z);
  }

  /**
   * Returns a mutable {@link BlockVector} using this vector's components.
   *
   * @return new mutable block vector
   */
  public BlockVector toBlockVector() {
    return new BlockVector(this.x, this.y, this.z);
  }

  /**
   * Returns a mutable {@link Location} using this vector's components, with yaw and pitch as 0.
   *
   * @return new mutable location
   */
  public Location toLocation(@Nullable World world) {
    return new Location(world, this.x, this.y, this.z);
  }

  /**
   * Returns the {@code x} component.
   *
   * @return {@code x} component.
   */
  public double getX() {
    return x;
  }

  /**
   * Returns the {@code x} component floored (rounded down).
   *
   * @return {@code x} component.
   */
  public int getBlockX() {
    return NumberConversions.floor(this.x);
  }

  /**
   * Returns the {@code y} component.
   *
   * @return {@code y} component.
   */
  public double getY() {
    return y;
  }

  /**
   * Returns the {@code y} component floored (rounded down).
   *
   * @return {@code y} component.
   */
  public int getBlockY() {
    return NumberConversions.floor(this.y);
  }

  /**
   * Returns the {@code z} component.
   *
   * @return {@code z} component.
   */
  public double getZ() {
    return z;
  }

  /**
   * Returns the {@code z} component floored (rounded down).
   *
   * @return {@code z} component.
   */
  public int getBlockZ() {
    return NumberConversions.floor(this.z);
  }

  public static final class ImmutableVectorSerializer implements Serializer<ImmutableVector> {

    private static final Pattern PATTERN = Pattern.compile("\\s+,\\s+");

    @Nullable @Override
    public Object serialize(@Nullable ImmutableVector object) throws IllegalArgumentException {
      if (object == null) {
        return null;
      }
      return roundExact(3, object.getX()) + ","
             + roundExact(3, object.getY()) + ","
             + roundExact(3, object.getZ());
    }

    @Nullable @Override
    public ImmutableVector deserialize(@Nullable Object serialized, @Nonnull Class wantedType)
        throws IllegalArgumentException {
      if (serialized == null) {
        return null;
      }
      checkNotNullOrEmpty(serialized.toString(), "serialized string");
      String[] split = PATTERN.split(serialized.toString(), 3);
      checkArgument(split.length == 3, "string is in an invalid format.");
      return new ImmutableVector(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                                 Double.parseDouble(split[2]));
    }
  }
}
