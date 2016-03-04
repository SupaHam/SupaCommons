package commons.minecraft.world.space;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class MutableVector extends Vector {

  public MutableVector(double x, double y, double z) {
    super(x, y, z);
  }

  @Override public String toString() {
    return "MutableVector{"
           + "x=" + this.x
           + ", y=" + this.y
           + ", z=" + this.z
           + '}';
  }

  @Nonnull @Override public MutableVector add(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return add(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutableVector add(double x, double y, double z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  @Nonnull @Override public MutableVector add(int a) {
    this.x += a;
    this.y += a;
    this.z += a;
    return this;
  }

  @Nonnull @Override public MutableVector add(double a) {
    this.x += a;
    this.y += a;
    this.z += a;
    return this;
  }

  @Nonnull @Override public MutableVector add(float a) {
    this.x += a;
    this.y += a;
    this.z += a;
    return this;
  }

  @Nonnull @Override public MutableVector subtract(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return subtract(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutableVector subtract(double x, double y, double z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    return this;
  }

  @Nonnull @Override public MutableVector subtract(int a) {
    this.x -= a;
    this.y -= a;
    this.z -= a;
    return this;
  }

  @Nonnull @Override public MutableVector subtract(double a) {
    this.x -= a;
    this.y -= a;
    this.z -= a;
    return this;
  }

  @Nonnull @Override public MutableVector subtract(float a) {
    this.x -= a;
    this.y -= a;
    this.z -= a;
    return this;
  }

  @Nonnull @Override public MutableVector divide(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return divide(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutableVector divide(double x, double y, double z) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    return this;
  }

  @Nonnull @Override public MutableVector divide(int d) {
    this.x /= d;
    this.y /= d;
    this.z /= d;
    return this;
  }

  @Nonnull @Override public MutableVector divide(double d) {
    this.x /= d;
    this.y /= d;
    this.z /= d;
    return this;
  }

  @Nonnull @Override public MutableVector divide(float d) {
    this.x /= d;
    this.y /= d;
    this.z /= d;
    return this;
  }

  @Nonnull @Override public MutableVector multiply(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    return multiply(o.x, o.y, o.z);
  }

  @Nonnull @Override public MutableVector multiply(double x, double y, double z) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    return this;
  }

  @Nonnull @Override public MutableVector multiply(int m) {
    this.x *= m;
    this.y *= m;
    this.z *= m;
    return this;
  }

  @Nonnull @Override public MutableVector multiply(double m) {
    this.x *= m;
    this.y *= m;
    this.z *= m;
    return this;
  }

  @Nonnull @Override public MutableVector multiply(float m) {
    this.x *= m;
    this.y *= m;
    this.z *= m;
    return this;
  }

  @Nonnull @Override public MutableVector midpoint(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    this.x = (this.x + o.x) / 2.0D;
    this.y = (this.y + o.y) / 2.0D;
    this.z = (this.z + o.z) / 2.0D;
    return this;
  }

  @Nonnull @Override public MutableVector crossProduct(@Nonnull Vector o) {
    Preconditions.checkNotNull(o, "o cannot be null.");
    this.x = this.y * o.z - o.y * this.z;
    this.y = this.z * o.x - o.z * this.x;
    this.z = this.x * o.y - o.x * this.y;
    return this;
  }

  @Nonnull @Override public MutableVector normalize() {
    divide(length());
    return this;
  }

  @Nonnull @Override
  public MutableVector copy() {
    return new MutableVector(this.x, this.y, this.z);
  }
}
