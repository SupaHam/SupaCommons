/*
 * Copyright 2016 Ali Moghnieh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.supaham.commons.bukkit.utils;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.serializers.RelativeVectorSerializer;

import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

import pluginbase.config.annotation.SerializeWith;

/**
 * Represents an {@link ImmutableVector} with the given components being optionally relative. This class plays nice
 * with Both {@link ImmutableVector} and {@link Vector}, both through {@link #equals(Object)} & {@link #equals(Vector)}
 * and {@link #with(ImmutableVector)} & {@link #with(Vector)}
 */
@SerializeWith(RelativeVectorSerializer.class)
public class RelativeVector extends ImmutableVector {

  private final boolean xRelative;
  private final boolean yRelative;
  private final boolean zRelative;

  public RelativeVector(double x, double y, double z, boolean xRelative, boolean yRelative, boolean zRelative) {
    super(x, y, z);
    this.xRelative = xRelative;
    this.yRelative = yRelative;
    this.zRelative = zRelative;
  }

  /**
   * Returns whether this {@link RelativeVector} is equal to an {@link ImmutableVector}. If this vector has relativity
   * (as in {@link #isRelative()} returns true), and {@code obj} isn't of type {@link RelativeVector} then {@code
   * false} is immediately returned. Otherwise, component relativity and the components themselves are compared against
   * each other and the result of equivalency is returned. Alternatively, if this vector has no relativity,
   * {@link ImmutableVector#equals(Object)} is called and this RelativeVector is treated exactly like a
   * {@link ImmutableVector}.
   *
   * @param obj object to compare
   *
   * @return whether this RelativeVector is equal to {@code obj}
   */
  @Override public boolean equals(Object obj) {
    // The following if-else makes us equal with ImmutableVector instances if we don't have relativity.
    if (!(obj instanceof ImmutableVector)) {
      return false;
    } else if (!isRelative()) {
      return super.equals(obj); // Treat this RelativeVector as if it were a normal ImmutableVector
    }

    // We've got relativity, the other object must be of type RelativeVector to even have relativity like us.
    if (!(obj instanceof RelativeVector)) {
      return false;
    }
    RelativeVector o2 = (RelativeVector) obj;
    return super.equals(obj)
           && ((this.xRelative == o2.xRelative)
               && (this.yRelative == o2.yRelative)
               && (this.zRelative == o2.zRelative));
  }

  /**
   * Returns whether this {@link RelativeVector} is equal to a {@link Vector}. If this vector has relativity (as in
   * {@link #isRelative()} returns true), then {@code false} is immediately returned as {@code o} is a vector of exact
   * coordinates. Otherwise, the normal {@link ImmutableVector} check of xyz components is done and that result is
   * returned.
   *
   * @param o vector to check
   *
   * @return whether {@code o} is equal to this RelativeVector
   */
  @Override public boolean equals(Vector o) {
    // If we're relative then no way could we be equal to a vector of exact coordinates.
    if (isRelative()) {
      return false;
    }
    // We're not relative, check if our coordinates are equal.
    return super.equals(o);
  }

  /**
   * Returns a new ImmutableVector combined of this relative vector with a {@link Vector}. If this vector has no
   * relativity (as in {@link #isRelative()} returns false), this same RelativeVector is returned. Otherwise, each
   * component keeps it initial value and if the component is relative the vector's same component is added to provide
   * relativity.
   *
   * @param vector vector to combine with this
   *
   * @return either this or a new ImmutableVector of the combination
   */
  @SuppressWarnings("Duplicates")
  public ImmutableVector with(@Nonnull Vector vector) {
    Preconditions.checkNotNull(vector, "vector cannot be null.");
    if (!isRelative()) {
      // Nothing in this vector is relative, return this vector of exact coordinates.
      return this;
    }

    double x = isXRelative() ? vector.getX() + getX() : getX();
    double y = isYRelative() ? vector.getY() + getY() : getY();
    double z = isZRelative() ? vector.getZ() + getZ() : getZ();
    return new ImmutableVector(x, y, z);
  }

  /**
   * Returns a new ImmutableVector combined of this relative vector with a {@link ImmutableVector}. If this vector has
   * no relativity (as in {@link #isRelative()} returns false), this same RelativeVector is returned. Otherwise, each
   * component keeps it initial value and if the component is relative the vector's same component is added to provide
   * relativity.
   *
   * @param vector vector to combine with this
   *
   * @return either this or a new ImmutableVector of the combination
   */
  @SuppressWarnings("Duplicates")
  public ImmutableVector with(@Nonnull ImmutableVector vector) {
    Preconditions.checkNotNull(vector, "vector cannot be null.");
    if (!isRelative()) {
      // Nothing in this vector is relative, return this vector of exact coordinates.
      return this;
    }

    double x = isXRelative() ? vector.getX() + getX() : getX();
    double y = isYRelative() ? vector.getY() + getY() : getY();
    double z = isZRelative() ? vector.getZ() + getZ() : getZ();
    return new ImmutableVector(x, y, z);
  }

  public boolean isRelative() {
    return isXRelative() || isYRelative() || isZRelative();
  }

  public boolean isXRelative() {
    return xRelative;
  }

  public boolean isYRelative() {
    return yRelative;
  }

  public boolean isZRelative() {
    return zRelative;
  }

}
