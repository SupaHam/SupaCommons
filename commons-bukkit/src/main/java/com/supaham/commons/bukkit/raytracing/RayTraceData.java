package com.supaham.commons.bukkit.raytracing;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.ImmutableVector;

import org.bukkit.World;

/**
 * Represents data belonging to a {@link RayTracing} task that specifies how it should perform the
 * task.
 */
public class RayTraceData {

  private final World world;
  private final ImmutableVector start;
  private final ImmutableVector end;
  private final boolean stopOnLiquid;
  private final boolean ignoreBoundingBox;
  private final boolean returnLastCollidableBlock;

  public static Builder builder() {
    return new Builder();
  }

  public RayTraceData(World world, ImmutableVector start, ImmutableVector end, boolean stopOnLiquid,
                      boolean ignoreBoundingBox, boolean returnLastCollidableBlock) {
    this.world = world;
    this.start = start;
    this.end = end;
    this.stopOnLiquid = stopOnLiquid;
    this.ignoreBoundingBox = ignoreBoundingBox;
    this.returnLastCollidableBlock = returnLastCollidableBlock;
  }

  public MovingObjectPosition run() {
    return RayTracing.rayTraceBlocks(this);
  }

  public World getWorld() {
    return world;
  }

  public ImmutableVector getStart() {
    return start;
  }

  public ImmutableVector getEnd() {
    return end;
  }

  public boolean isStopOnLiquid() {
    return stopOnLiquid;
  }

  public boolean isIgnoreBoundingBox() {
    return ignoreBoundingBox;
  }

  public boolean isReturnLastCollidableBlock() {
    return returnLastCollidableBlock;
  }

  public static final class Builder {

    private World world;
    private ImmutableVector start;
    private ImmutableVector end;
    private boolean stopOnLiquid = false;
    private boolean ignoreBoundingBox = false;
    private boolean returnLastCollidableBlock = false;

    public RayTraceData build() {
      Preconditions.checkNotNull(this.world, "world cannot be null.");
      Preconditions.checkNotNull(this.start, "start vector cannot be null.");
      Preconditions.checkNotNull(this.end, "end vector cannot be null.");
      return new RayTraceData(this.world, this.start, this.end, this.stopOnLiquid,
                              this.ignoreBoundingBox, this.returnLastCollidableBlock);
    }

    public Builder world(World world) {
      this.world = world;
      return this;
    }

    public Builder start(ImmutableVector start) {
      this.start = start;
      return this;
    }

    public Builder end(ImmutableVector end) {
      this.end = end;
      return this;
    }

    public Builder stopOnLiquid() {
      this.stopOnLiquid = true;
      return this;
    }

    public Builder stopOnLiquid(boolean stopOnLiquid) {
      this.stopOnLiquid = stopOnLiquid;
      return this;
    }

    public Builder ignoreBoundingBox() {
      this.ignoreBoundingBox = true;
      return this;
    }

    public Builder ignoreBoundingBox(boolean ignoreBoundingBox) {
      this.ignoreBoundingBox = ignoreBoundingBox;
      return this;
    }

    public Builder returnLastCollidableBlock() {
      this.returnLastCollidableBlock = true;
      return this;
    }

    public Builder returnLastCollidableBlock(boolean returnLastCollidableBlock) {
      this.returnLastCollidableBlock = returnLastCollidableBlock;
      return this;
    }
  }
}
