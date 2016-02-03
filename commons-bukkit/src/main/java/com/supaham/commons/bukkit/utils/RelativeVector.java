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

/**
 * Represents an {@link ImmutableVector} with the given components being optionally relative.
 */
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

  @Override public boolean equals(Object obj) {
    if (!(obj instanceof RelativeVector)) {
      return false;
    }
    RelativeVector o2 = (RelativeVector) obj;
    return super.equals(obj)
           && ((this.xRelative == o2.xRelative)
           && (this.yRelative == o2.yRelative)
           && (this.zRelative == o2.zRelative));
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
