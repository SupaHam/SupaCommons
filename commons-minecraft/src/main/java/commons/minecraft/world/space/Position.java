package commons.minecraft.world.space;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializeWith;

@SerializeWith(PositionSerializer.class)
public class Position extends Vector {

  public static final Position ZERO = new Position(0, 0, 0, 0, 0);
  public static final Position ONE = new Position(1, 1, 1, 0, 0);

  protected float yaw;
  protected float pitch;

  public Position(double x, double y, double z) {
    super(x, y, z);
  }

  public Position(double x, double y, double z, float yaw, float pitch) {
    super(x, y, z);
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public float getYaw() {
    return yaw;
  }

  public Position setYaw(float yaw) {
    return yaw == this.yaw ? this : new Position(x, y, z, yaw, pitch);
  }

  public float getPitch() {
    return pitch;
  }

  public Position setPitch(float pitch) {
    return pitch == this.pitch ? this : new Position(x, y, z, yaw, pitch);
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
  public Position add(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ZERO.equals(o) ? this : new Position(this.x + o.x, this.y + o.y, this.z + o.z, this.yaw, this.pitch);
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
  public Position add(double x, double y, double z) {
    return x == 0 && y == 0 && z == 0 ? this
                                      : new Position(this.x + x, this.y + y, this.z + z, this.yaw, this.pitch);
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
  public Position add(int a) {
    return a == 0 ? this : new Position(this.x + a, this.y + a, this.z + a, this.yaw, this.pitch);
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
  public Position add(double a) {
    return a == 0 ? this : new Position(this.x + a, this.y + a, this.z + a, this.yaw, this.pitch);
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
  public Position add(float a) {
    return a == 0 ? this : new Position(this.x + a, this.y + a, this.z + a, this.yaw, this.pitch);
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
  public Position subtract(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ZERO.equals(o) ? this : new Position(this.x - o.x, this.y - o.y, this.z - o.z, this.yaw, this.pitch);
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
  public Position subtract(double x, double y, double z) {
    return x == 0 && y == 0 && z == 0 ? this
                                      : new Position(this.x - x, this.y - y, this.z - z, this.yaw, this.pitch);
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
  public Position subtract(int a) {
    return a == 0 ? this : new Position(this.x - a, this.y - a, this.z - a, this.yaw, this.pitch);
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
  public Position subtract(double a) {
    return a == 0 ? this : new Position(this.x - a, this.y - a, this.z - a, this.yaw, this.pitch);
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
  public Position subtract(float a) {
    return a == 0 ? this : new Position(this.x - a, this.y - a, this.z - a,
                                      this.yaw, this.pitch);
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
  public Position divide(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ONE.equals(o) ? this : new Position(this.x / o.x, this.y / o.y, this.z / o.z, this.yaw, this.pitch);
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
  public Position divide(double x, double y, double z) {
    return x == 1 && y == 1 && z == 1 ? this : new Position(this.x / x, this.y / y, this.z / z, this.yaw, this.pitch);
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
  public Position divide(int d) {
    return d == 1 ? this : new Position(this.x / d, this.y / d, this.z / d, this.yaw, this.pitch);
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
  public Position divide(double d) {
    return d == 1 ? this : new Position(this.x / d, this.y / d, this.z / d, this.yaw, this.pitch);
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
  public Position divide(float d) {
    return d == 1 ? this : new Position(this.x / d, this.y / d, this.z / d, this.yaw, this.pitch);
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
  public Position multiply(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return ONE.equals(o) ? this : new Position(this.x * o.x, this.y * o.y, this.z * o.z, this.yaw, this.pitch);
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
  public Position multiply(double x, double y, double z) {
    return x == 1 && y == 1 && z == 1 ? this
                                      : new Position(this.x * x, this.y * y, this.z * z, this.yaw, this.pitch);
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
  public Position multiply(int m) {
    return m == 1 ? this : new Position(this.x * m, this.y * m, this.z * m, this.yaw, this.pitch);
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
  public Position multiply(double m) {
    return m == 1 ? this : new Position(this.x * m, this.y * m, this.z * m, this.yaw, this.pitch);
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
  public Position multiply(float m) {
    return m == 1 ? this : new Position(this.x * m, this.y * m, this.z * m, this.yaw, this.pitch);
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
  public Position midpoint(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return equals(o) ? this
                     : new Position((this.x + o.x) / 2.0D,
                                    (this.y + o.y) / 2.0D,
                                    (this.z + o.z) / 2.0D,
                                    this.yaw, this.pitch);
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
  public Position crossProduct(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return equals(o) ? this
                     : new Position(this.y * o.z - o.y * this.z,
                                    this.z * o.x - o.z * this.x,
                                    this.x * o.y - o.x * this.y,
                                    this.yaw, this.pitch);
  }

  /**
   * Returns a new {@link Vector} of this vector to a unit vector (a vector with length
   * of 1).
   *
   * @return a new {@link Vector} of the unit vector
   */
  @Nonnull
  public Position normalize() {
    return divide(length());
  }

  @Nonnull
  public Position copy() {
    return new Position(x, y, z, yaw, pitch);
  }

  @Nonnull @Override public Position toPosition() {
    return super.toPosition(this.yaw, this.pitch);
  }

  @Nonnull @Override public MutablePosition toMutablePosition() {
    return super.toMutablePosition(this.yaw, this.pitch);
  }
}
