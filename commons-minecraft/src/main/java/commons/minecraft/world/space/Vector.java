package commons.minecraft.world.space;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializeWith;

/**
 * Represents an immutable mathematical vector; providing stability in long-term use.
 * <p/>
 * <b>Note: This class supports SerializableConfig serialization by using {@link VectorSerializer}</b>
 *
 * @see #Vector(double, double, double)
 * @since 0.5.1
 */
@SerializeWith(VectorSerializer.class)
public class Vector {

  public static final Vector ZERO = new Vector(0, 0, 0);
  public static final Vector ONE = new Vector(1, 1, 1);
  private static final double EPSILON = 0.000001;

  protected double x;
  protected double y;
  protected double z;

  public Vector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override public boolean equals(Object obj) {
    if (!(obj instanceof Vector)) {
      return false;
    }
    Vector o = (Vector) obj;
    return Math.abs(x - o.x) < EPSILON && Math.abs(y - o.y) < EPSILON && Math.abs(z - o.z) < EPSILON;
  }

  private volatile int hashCode;

  @Override public int hashCode() {
    return hashCode != 0 ? hashCode : (hashCode = Objects.hashCode(x, y, z));
  }

  @Override public String toString() {
    return "Vector{"
           + "x=" + this.x
           + ", y=" + this.y
           + ", z=" + this.z
           + '}';
  }

  /**
   * Creates a new {@link Vector} of this vector plus another {@link Vector}. If
   * the given vector is equals to {@link #ZERO} this same Point instance is returned.
   *
   * @param o the other vector to add
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector add(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ZERO.equals(o) ? this : new Vector(this.x + o.x, this.y + o.y, this.z + o.z);
  }

  /**
   * Creates a new {@link Vector} of this vector plus the three given components. If
   * the given components are equal to 0 this same Point instance is returned.
   *
   * @param x how much to add on the x axis
   * @param y how much to add on the y axis
   * @param z how much to add on the z axis
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector add(double x, double y, double z) {
    return x == 0 && y == 0 && z == 0 ? this
                                      : new Vector(this.x + x, this.y + y, this.z + z);
  }

  /**
   * Creates a new {@link Vector} of this vector plus the given int constant on all axes.
   * If the given constant is equal to 0, this same Point instance is returned.
   *
   * @param a constant to add to all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector add(int a) {
    return a == 0 ? this : new Vector(this.x + a, this.y + a, this.z + a);
  }

  /**
   * Creates a new {@link Vector} of this vector plus the given double constant on all
   * axes. If the given constant is equal to 0, this same Point instance is returned.
   *
   * @param a constant to add to all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector add(double a) {
    return a == 0 ? this : new Vector(this.x + a, this.y + a, this.z + a);
  }

  /**
   * Creates a new {@link Vector} of this vector plus the given float constant on all
   * axes. If the given constant is equal to 0, this same Point instance is returned.
   *
   * @param a constant to add to all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector add(float a) {
    return a == 0 ? this : new Vector(this.x + a, this.y + a, this.z + a);
  }

  /**
   * Creates a new {@link Vector} of this vector minus another {@link Vector}. If
   * the given vector is equals to {@link #ZERO} this same Point instance is returned.
   *
   * @param o the other vector to subtract
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector subtract(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ZERO.equals(o) ? this : new Vector(this.x - o.x, this.y - o.y, this.z - o.z);
  }

  /**
   * Creates a new {@link Vector} of this vector minus the three given components. If
   * the given components are equal to 0 this same Point instance is returned.
   *
   * @param x how much to subtract on the x axis
   * @param y how much to subtract on the y axis
   * @param z how much to subtract on the z axis
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector subtract(double x, double y, double z) {
    return x == 0 && y == 0 && z == 0 ? this
                                      : new Vector(this.x - x, this.y - y, this.z - z);
  }

  /**
   * Creates a new {@link Vector} of this vector minus the given int constant on all axes.
   * If the given constant is equal to 0, this same Point instance is returned.
   *
   * @param a constant to subtract from all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector subtract(int a) {
    return a == 0 ? this : new Vector(this.x - a, this.y - a, this.z - a);
  }

  /**
   * Creates a new {@link Vector} of this vector minus the given double constant on all
   * axes. If the given constant is equal to 0, this same Point instance is returned.
   *
   * @param a constant to subtract from all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector subtract(double a) {
    return a == 0 ? this : new Vector(this.x - a, this.y - a, this.z - a);
  }

  /**
   * Creates a new {@link Vector} of this vector minus the given float constant on all
   * axes. If the given constant is equal to 0, this same Point instance is returned.
   *
   * @param a constant to subtract from all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector subtract(float a) {
    return a == 0 ? this : new Vector(this.x - a, this.y - a, this.z - a);
  }

  /**
   * Creates a new {@link Vector} of this vector divided by another {@link
   * Vector}. If the given vector is equals to {@link #ONE} this same Point
   * instance is returned.
   *
   * @param o the other vector to divide by
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector divide(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ONE.equals(o) ? this : new Vector(this.x / o.x, this.y / o.y, this.z / o.z);
  }

  /**
   * Creates a new {@link Vector} of this vector divided by the three given components. If
   * the given components are equal to 1 this same Point instance is returned.
   *
   * @param x how much to divide by on the x axis
   * @param y how much to divide by on the y axis
   * @param z how much to divide by on the z axis
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector divide(double x, double y, double z) {
    return x == 1 && y == 1 && z == 1 ? this
                                      : new Vector(this.x / x, this.y / y, this.z / z);
  }

  /**
   * Creates a new {@link Vector} of this vector divided by the given int constant on all
   * axes. If the given constant is equal to 1, this same Point instance is returned.
   *
   * @param d constant to divide by for all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector divide(int d) {
    return d == 1 ? this : new Vector(this.x / d, this.y / d, this.z / d);
  }

  /**
   * Creates a new {@link Vector} of this vector divided by the given double constant on
   * all axes. If the given constant is equal to 1, this same Point instance is returned.
   *
   * @param d constant to divide by for all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector divide(double d) {
    return d == 1 ? this : new Vector(this.x / d, this.y / d, this.z / d);
  }

  /**
   * Creates a new {@link Vector} of this vector divided by the given float constant on
   * all axes. If the given constant is equal to 1, this same Point instance is returned.
   *
   * @param d constant to divide by for all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector divide(float d) {
    return d == 1 ? this : new Vector(this.x / d, this.y / d, this.z / d);
  }

  /**
   * Creates a new {@link Vector} of this vector multiplied by another {@link
   * Vector}. If the given vector is equals to {@link #ONE} this same Point
   * instance is returned.
   *
   * @param o the other vector to multiply by
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector multiply(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ONE.equals(o) ? this : new Vector(this.x * o.x, this.y * o.y, this.z * o.z);
  }

  /**
   * Creates a new {@link Vector} of this vector multiplied by the three given components.
   * If the given components are equal to 1 this same Point instance is returned.
   *
   * @param x how much to multiply by on the x axis
   * @param y how much to multiply by on the y axis
   * @param z how much to multiply by on the z axis
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector multiply(double x, double y, double z) {
    return x == 1 && y == 1 && z == 1 ? this
                                      : new Vector(this.x * x, this.y * y, this.z * z);
  }

  /**
   * Creates a new {@link Vector} of this vector multiplied by the given int constant on
   * all axes. If the given constant is equal to 1, this same Point instance is returned.
   *
   * @param m constant to multiply by for all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector multiply(int m) {
    return m == 1 ? this : new Vector(this.x * m, this.y * m, this.z * m);
  }

  /**
   * Creates a new {@link Vector} of this vector multiplied by the given double constant
   * on all axes. If the given constant is equal to 1, this same Point instance is
   * returned.
   *
   * @param m constant to multiply by for all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector multiply(double m) {
    return m == 1 ? this : new Vector(this.x * m, this.y * m, this.z * m);
  }

  /**
   * Returns a new {@link Vector} of this vector multiplied by the given float constant
   * on all axes. If the given constant is equal to 1, this same Point instance is
   * returned.
   *
   * @param m constant to multiply by for all three components
   *
   * @return a new {@link Vector} or the same instance if no modification was made
   */
  @Nonnull
  public Vector multiply(float m) {
    return m == 1 ? this : new Vector(this.x * m, this.y * m, this.z * m);
  }

  /**
   * Returns a new {@link Vector} of the midpoint vector between this vector and another
   * {@link Vector}. If the given constant is {@link #equals(Object)} to the given vector
   * {@code o}, this same Point instance is returned.
   *
   * @param o the other vector
   *
   * @return a new {@link Vector} of the midpoint
   */
  @Nonnull
  public Vector midpoint(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return equals(o) ? this
                     : new Vector((this.x + o.x) / 2.0D,
                                  (this.y + o.y) / 2.0D,
                                  (this.z + o.z) / 2.0D);
  }

  /**
   * Returns a new {@link Vector} of the cross product of this vector with another {@link
   * Vector}. The cross product is defined as:
   * <ul>
   * <li>x = y1 * z2 - y2 * z1
   * <li>y = z1 * x2 - z2 * x1
   * <li>z = x1 * y2 - x2 * y1
   * </ul>
   *
   * @param o the other vector
   *
   * @return a new {@link Vector} of the cross product
   */
  @Nonnull
  public Vector crossProduct(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return equals(o) ? this
                     : new Vector(this.y * o.z - o.y * this.z,
                                  this.z * o.x - o.z * this.x,
                                  this.x * o.y - o.x * this.y);
  }

  /**
   * Returns a new {@link Vector} of this vector to a unit vector (a vector with length
   * of 1).
   *
   * @return a new {@link Vector} of the unit vector
   */
  @Nonnull
  public Vector normalize() {
    return divide(length());
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
    return Math.sqrt(x * x + y * y + z * z);
  }

  /**
   * Returns the magnitude of this vector squared.
   *
   * @return the squared magnitude
   */
  public double lengthSquared() {
    return x * x + y * y + z * z;
  }

  /**
   * Returns the distance between this vector and another {@link Vector}. The value of
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
  public double distance(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return Math.sqrt(Math.pow(this.x - o.x, 2)
                     + Math.pow(this.y - o.y, 2)
                     + Math.pow(this.z - o.z, 2));
  }

  /**
   * Returns the squared distance between this vector and another {@link Vector}.
   *
   * @param o the other vector
   *
   * @return the distance
   */
  public double distanceSquared(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return Math.pow(this.x - o.x, 2)
           + Math.pow(this.y - o.y, 2)
           + Math.pow(this.z - o.z, 2);
  }

  /**
   * Calculates the dot product of this vector with another {@link Vector}. The dot
   * product is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
   *
   * @param o the other vector
   *
   * @return dot product
   */
  public double dot(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return this.x * o.x + this.y * o.y + this.z * o.z;
  }

  public boolean isSameBlock(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return getXAsInt() == o.getXAsInt()
           && getYAsInt() == o.getYAsInt()
           && getZAsInt() == o.getZAsInt();
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
  public boolean isInAABB(@Nonnull Vector min, @Nonnull Vector max) {
    Preconditions.checkNotNull(min, "min cannot be null.");
    Preconditions.checkNotNull(max, "max cannot be null.");
    return this.x >= min.x && this.x <= max.x
           && this.y >= min.y && this.y <= max.y
           && this.z >= min.z && this.z <= max.z;
  }

  /**
   * Returns whether this vector is within a sphere.
   *
   * @param origin sphere origin
   * @param radius sphere radius
   *
   * @return whether this vector is in the sphere
   */
  public boolean isInSphere(@Nonnull Vector origin, double radius) {
    Preconditions.checkNotNull(origin, "origin cannot be null.");
    Preconditions.checkArgument(radius > 0, "radius cannot be 0 or less");
    return Math.pow(origin.x - this.x, 2)
           + Math.pow(origin.y - this.y, 2)
           + Math.pow(origin.z - this.z, 2) <= radius * radius;
  }

  /**
   * Returns a copy of this {@link Vector}. Each Vector extension overrides this method to return a copy of its
   * version. E.g. {@link MutableVector} will create a {@link MutableVector} instance. To only ensure the current
   * {@link Vector} is a {@link MutableVector} instead of always copying, see {@link #toMutableVector()}.
   *
   * @return new vector copy
   *
   * @see #toMutableVector()
   * @see #toPosition()
   * @see #toPosition(float, float)
   * @see #toMutablePosition()
   * @see #toMutablePosition(float, float)
   */
  @Nonnull
  public Vector copy() {
    return new Vector(x, y, z);
  }

  /**
   * Returns an immutable {@link Vector} from this current Vector instance's components. If this instance isn't a
   * {@link Vector} instance, a {@link Vector} instance is created, alternatively this same instance is returned. It is
   * important to note this means if {@link MutableVector}, {@link Position}, or {@link MutablePosition} call this
   * method, a new instance of {@link Vector} of the {@code x}, {@code y}, {@code z} components, copied from the
   * caller, is created.
   *
   * @return a {@link Vector} instance
   */
  @Nonnull
  public Vector toVector() {
    return getClass().equals(Vector.class) ? this : new Vector(x, y, z);
  }

  /**
   * Returns a {@link MutableVector} from this current Vector instance's components. If this instance isn't a {@link
   * MutableVector} instance, a {@link MutableVector} instance is created, alternatively this same instance is
   * returned. It is important to note this means if {@link Vector}, {@link Position}, or {@link MutablePosition} call
   * this method, a new instance of {@link MutableVector} of the {@code x}, {@code y}, {@code z} components, copied
   * from the caller, is created.
   *
   * @return a {@link MutableVector} instance
   */
  @Nonnull
  public MutableVector toMutableVector() {
    return this instanceof MutableVector ? (MutableVector) this : new MutableVector(x, y, z);
  }

  /**
   * Returns an immutable {@link Position} using this vector's components, with yaw and pitch as 0. If this instance
   * isn't a {@link Position} instance, a {@link Position} instance is created, alternatively this same instance is
   * returned. It is important to note this means if {@link Vector}, {@link MutableVector}, or {@link MutablePosition}
   * call this method, a new instance of {@link Position} of the {@code x}, {@code y}, {@code z} components, copied
   * from the caller, is created.
   * <p />
   * <b>Note:</b> If this is a {@link MutablePosition}, the yaw and pitch will be provided in the returned {@link
   * Position}.
   *
   * @return new {@link Position}
   *
   * @see #toPosition(float, float)
   * @see #toMutablePosition()
   * @see #toMutablePosition(float, float)
   */
  @Nonnull
  public Position toPosition() {
    return toPosition(0, 0);
  }

  /**
   * Returns an immutable {@link Position} using this vector's components, with the given yaw and pitch. If this
   * instance isn't a {@link Position} instance, a {@link Position} instance is created, alternatively this same
   * instance is returned. It is important to note this means if {@link Vector}, {@link MutableVector}, or {@link
   * MutablePosition} call this method, a new instance of {@link Position} of the {@code x}, {@code y}, {@code z}
   * components, copied from the caller, is created.
   * <p />
   * <b>Note:</b> If this is a {@link MutablePosition}, the yaw and pitch will be provided in the returned {@link
   * Position}.
   *
   * @return new {@link Position}
   *
   * @see #toPosition(float, float)
   * @see #toMutablePosition()
   * @see #toMutablePosition(float, float)
   */
  @Nonnull
  public Position toPosition(float yaw, float pitch) {
    return getClass().equals(Position.class) ? (Position) this : new Position(x, y, z, yaw, pitch);
  }

  /**
   * Returns a {@link MutablePosition} using this vector's components, with yaw and pitch as 0. If this instance
   * isn't a {@link MutablePosition} instance, a {@link MutablePosition} instance is created, alternatively this same
   * instance is returned. It is important to note this means if {@link Vector}, {@link MutableVector}, or {@link
   * Position} call this method, a new instance of {@link MutablePosition} of the {@code x}, {@code y}, {@code z}
   * components, copied from the caller, is created.
   * <p />
   * <b>Note:</b> If this is a {@link Position}, the yaw and pitch will be provided in the returned {@link
   * MutablePosition}.
   *
   * @return new {@link MutablePosition}
   *
   * @see #toMutablePosition(float, float)
   * @see #toPosition()
   * @see #toPosition(float, float)
   */
  @Nonnull
  public MutablePosition toMutablePosition() {
    return toMutablePosition(0, 0);
  }


  /**
   * Returns a {@link MutablePosition} using this vector's components, with the given yaw and pitch. If this instance
   * isn't a {@link MutablePosition} instance, a {@link MutablePosition} instance is created, alternatively this same
   * instance is returned. It is important to note this means if {@link Vector}, {@link MutableVector}, or {@link
   * Position} call this method, a new instance of {@link MutablePosition} of the {@code x}, {@code y}, {@code z}
   * components, copied from the caller, is created.
   * <p />
   * <b>Note:</b> If this is a {@link Position}, the yaw and pitch will be provided in the returned {@link
   * MutablePosition}.
   *
   * @return new {@link MutablePosition}
   *
   * @see #toMutablePosition(float, float)
   * @see #toPosition()
   * @see #toPosition(float, float)
   */
  @Nonnull
  public MutablePosition toMutablePosition(float yaw, float pitch) {
    return this instanceof MutablePosition ? (MutablePosition) this : new MutablePosition(x, y, z, yaw, pitch);
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
  public int getXAsInt() {
    return (int) Math.floor(x);
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
  public int getYAsInt() {
    return (int) Math.floor(y);
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
  public int getZAsInt() {
    return (int) Math.floor(z);
  }
}
