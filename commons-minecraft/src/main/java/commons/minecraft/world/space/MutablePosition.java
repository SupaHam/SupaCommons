package commons.minecraft.world.space;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class MutablePosition extends Position {

  public MutablePosition(double x, double y, double z) {
    super(x, y, z);
  }

  public MutablePosition(double x, double y, double z, float yaw, float pitch) {
    super(x, y, z, yaw, pitch);
  }

  @Override public String toString() {
    return "MutablePosition{"
           + "x=" + this.x
           + ", y=" + this.y
           + ", z=" + this.z
           + '}';
  }

  @Override public Position setYaw(float yaw) {
    this.yaw = yaw;
    return this;
  }

  @Override public Position setPitch(float pitch) {
    this.pitch = pitch;
    return this;
  }

  @Nonnull @Override public MutablePosition add(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return add(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutablePosition add(double x, double y, double z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  @Nonnull @Override public MutablePosition add(int a) {
    this.x += a;
    this.y += a;
    this.z += a;
    return this;
  }

  @Nonnull @Override public MutablePosition add(double a) {
    this.x += a;
    this.y += a;
    this.z += a;
    return this;
  }

  @Nonnull @Override public MutablePosition add(float a) {
    this.x += a;
    this.y += a;
    this.z += a;
    return this;
  }

  @Nonnull @Override public MutablePosition subtract(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return subtract(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutablePosition subtract(double x, double y, double z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    return this;
  }

  @Nonnull @Override public MutablePosition subtract(int a) {
    this.x -= a;
    this.y -= a;
    this.z -= a;
    return this;
  }

  @Nonnull @Override public MutablePosition subtract(double a) {
    this.x -= a;
    this.y -= a;
    this.z -= a;
    return this;
  }

  @Nonnull @Override public MutablePosition subtract(float a) {
    this.x -= a;
    this.y -= a;
    this.z -= a;
    return this;
  }

  @Nonnull @Override public MutablePosition divide(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return divide(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutablePosition divide(double x, double y, double z) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    return this;
  }

  @Nonnull @Override public MutablePosition divide(int d) {
    this.x /= d;
    this.y /= d;
    this.z /= d;
    return this;
  }

  @Nonnull @Override public MutablePosition divide(double d) {
    this.x /= d;
    this.y /= d;
    this.z /= d;
    return this;
  }

  @Nonnull @Override public MutablePosition divide(float d) {
    this.x /= d;
    this.y /= d;
    this.z /= d;
    return this;
  }

  @Nonnull @Override public MutablePosition multiply(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return multiply(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutablePosition multiply(double x, double y, double z) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    return this;
  }

  @Nonnull @Override public MutablePosition multiply(int m) {
    this.x *= m;
    this.y *= m;
    this.z *= m;
    return this;
  }

  @Nonnull @Override public MutablePosition multiply(double m) {
    this.x *= m;
    this.y *= m;
    this.z *= m;
    return this;
  }

  @Nonnull @Override public MutablePosition multiply(float m) {
    this.x *= m;
    this.y *= m;
    this.z *= m;
    return this;
  }

  @Nonnull @Override public MutablePosition midpoint(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    this.x = (this.x + o.x) / 2.0D;
    this.y = (this.y + o.y) / 2.0D;
    this.z = (this.z + o.z) / 2.0D;
    return this;
  }

  @Nonnull @Override public MutablePosition crossProduct(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    this.x = this.y * o.z - o.y * this.z;
    this.y = this.z * o.x - o.z * this.x;
    this.z = this.x * o.y - o.x * this.y;
    return this;
  }

  @Nonnull @Override public MutablePosition normalize() {
    divide(length());
    return this;
  }

  @Nonnull @Override
  public MutablePosition copy() {
    return new MutablePosition(x, y, z, yaw, pitch);
  }
}
