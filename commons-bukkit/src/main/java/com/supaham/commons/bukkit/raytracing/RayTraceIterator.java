package com.supaham.commons.bukkit.raytracing;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.ImmutableVector;

import org.bukkit.util.Vector;

/**
 * A class for iterating over a Ray-Trace. The necessary data is provided through {@link
 * #next(RayTraceIteratorEntry)} and the last entry can be retrieved through {@link #run()}.
 * <p />
 * After instantiating this class, you will need to call {@link #run()} for it to actually
 * function.
 * <p />
 */
public abstract class RayTraceIterator {

  protected final RayTraceData data;
  protected final double incrementBy;

  private double rayLength;
  private Vector lastVector;
  private Vector incrVector;
  private Vector direction;

  /**
   * Constructs a new {@link RayTraceIterator} with {@link RayTraceData} to control the ray-tracing
   * task. The {@code double} parameter, incrementBy, is how much to progress in the direction
   * every iteration. The progress starts at the {@link RayTraceData#getStart()} and ends at {@link
   * RayTraceData#getEnd()}.
   * <p />
   * {@code incrementBy} as 1 is typically not a bad choice considering a block exists every one
   * meter (or block, basically).
   *
   * @param data data to ray-trace with
   * @param incrementBy increment constant for each iteration
   */
  protected RayTraceIterator(RayTraceData data, double incrementBy) {
    Preconditions.checkNotNull(data, "data cannot be null.");
    Preconditions.checkArgument(incrementBy > 0, "increment value must be larger than 0.");
    this.data = data;
    this.incrementBy = incrementBy;
    defaults();
  }

  protected abstract boolean next(RayTraceIteratorEntry next);

  private void defaults() {
    Vector distance = data.getEnd().subtract(data.getStart()).toVector();
    this.rayLength = distance.length();
    this.direction = distance.normalize().clone();
    this.incrVector = this.direction.clone().multiply(incrementBy);
    this.lastVector = data.getStart().toVector();
  }

  public RayTraceIteratorEntry run() {
    int step = 0;
    while (true) {
      Vector _lastVector = this.lastVector.clone();
      this.lastVector.add(this.incrVector);

      // We're done
      if (data.getStart().distance(lastVector) >= rayLength) {
        break;
      }
      MovingObjectPosition mop = rayTrace(_lastVector, this.lastVector);
      Vector newVector = this.lastVector.clone();
      if (mop != null) {
        newVector = mop.getPosition().toVector();
      }
      RayTraceIteratorEntry next = new RayTraceIteratorEntry(new ImmutableVector(_lastVector),
                                                             new ImmutableVector(newVector),
                                                             mop,
                                                             step++);
      if (next(next)) {
        defaults();
        return next;
      }
    }
    defaults();
    return null;
  }

  private MovingObjectPosition rayTrace(Vector start, Vector end) {
    return RayTracing.rayTraceBlocks(data.getWorld(), start, end,
                                     data.getFluidCollision(),
                                     data.isIgnoreBoundingBox(),
                                     data.isReturnLastCollidableBlock());
  }

  public RayTraceData getData() {
    return data;
  }

  public double getIncrementBy() {
    return incrementBy;
  }

  public static final class RayTraceIteratorEntry {

    private final ImmutableVector lastVector;
    private final ImmutableVector newVector;
    private final MovingObjectPosition movingObjectPosition;
    private final int step;

    public RayTraceIteratorEntry(ImmutableVector lastVector,
                                 ImmutableVector newVector,
                                 MovingObjectPosition movingObjectPosition,
                                 int step) {
      this.lastVector = lastVector;
      this.newVector = newVector;
      this.movingObjectPosition = movingObjectPosition;
      this.step = step;
    }

    public ImmutableVector getLastVector() {
      return lastVector;
    }

    public ImmutableVector getNewVector() {
      return newVector;
    }

    public MovingObjectPosition getMovingObjectPosition() {
      return movingObjectPosition;
    }

    public int getStep() {
      return step;
    }
  }
}
