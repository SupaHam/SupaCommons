package commons.minecraft.world.space;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;

import pluginbase.config.serializers.Serializer;

public class VectorTests {

  private static final Vector V_ZERO = new Vector(0.0000001, 0.0000001, 0.0000001);
  private static final Vector V_ONE = new Vector(1.0000001, 1.0000001, 1.0000001);
  private static final Position P_ZERO = new Position(0.0000001, 0.0000001, 0.0000001, 0, 0);
  private static final Position P_ONE = new Position(1.0000001, 1.0000001, 1.0000001, 1, 1);

  @Test
  public void testFuzzy() throws Exception {
    Assert.assertEquals(Vector.ZERO, V_ZERO);
    Assert.assertEquals(Vector.ONE, V_ONE);
    Assert.assertEquals(MutableVector.ZERO, V_ZERO);
    Assert.assertEquals(MutableVector.ONE, V_ONE);

    Assert.assertEquals(Position.ZERO, P_ZERO);
    Assert.assertEquals(Position.ONE, P_ONE);
    Assert.assertEquals(MutablePosition.ZERO, P_ZERO);
    Assert.assertEquals(MutablePosition.ONE, P_ONE);
  }

  @Test
  public void testAdd() throws Exception {
    testImmutability(Vector::add);
    testMutability(Vector::add);
  }

  @Test
  public void testSubtract() throws Exception {
    testImmutability(Vector::subtract);
    testMutability(Vector::subtract);
  }

  @Test
  public void testDivide() throws Exception {
    BiFunction<Vector, Vector, Vector> func = Vector::divide;

    // No changes, return same instance
    Assert.assertTrue(V_ONE == func.apply(V_ONE, new Vector(1, 1, 1)));
    Assert.assertTrue(P_ONE == func.apply(P_ONE, new Position(1, 1, 1)));

    Assert.assertEquals(new Vector(0.5, 0.5, 0.5), func.apply(V_ONE, new Vector(2, 2, 2)));
    Assert.assertEquals(new Position(0.5, 0.5, 0.5), func.apply(P_ONE, new Position(2, 2, 2)));
  }

  @Test
  public void testMultiplication() throws Exception {
    BiFunction<Vector, Vector, Vector> func = Vector::multiply;

    // No changes, return same instance
    Assert.assertTrue(V_ONE == func.apply(V_ONE, new Vector(1, 1, 1)));
    Assert.assertTrue(P_ONE == func.apply(P_ONE, new Position(1, 1, 1)));

    Assert.assertEquals(new Vector(2, 2, 2), func.apply(V_ONE, new Vector(2, 2, 2)));
    Assert.assertEquals(new Position(2, 2, 2), func.apply(P_ONE, new Position(2, 2, 2)));
  }

  @Test
  public void testMidpoint() throws Exception {
    Assert.assertEquals(new Vector(1.5, 1.5, 1.5), V_ONE.midpoint(new Vector(2, 2, 2)));
  }

  @Test
  public void testCrossProduct() throws Exception {
    Assert.assertEquals(new Vector(0, -1, 0), new Vector(1, 0, 0).crossProduct(new Vector(0, 0, 1)));
  }

  @Test
  public void testLength() throws Exception {
    Assert.assertTrue(1 == new Vector(1, 0, 0).lengthSquared());
    Assert.assertTrue(2 == new Vector(1, 1, 0).lengthSquared());
    Assert.assertTrue(3 == new Vector(1, 1, 1).lengthSquared());
  }

  @Test
  public void testDistance() throws Exception {
    Assert.assertTrue(0 == new Vector(1, 0, 0).distanceSquared(new Vector(1, 0, 0)));
    Assert.assertTrue(1 == new Vector(1, 1, 0).distanceSquared(new Vector(1, 0, 0)));
  }

  @Test
  public void testDotProduct() throws Exception {
    Assert.assertTrue(0 == new Vector(1, 0, 0).dot(new Vector(0, 0, 1)));
    Assert.assertTrue(Math.pow(2, -0.5) == new Vector(Math.pow(2, -0.5), 0, Math.pow(2, -0.5))
        .dot(new Vector(0, 0, 1)));
  }

  @Test
  public void testSameBlock() throws Exception {
    Assert.assertFalse(V_ONE.isSameBlock(V_ZERO));
    Assert.assertTrue(V_ONE.isSameBlock(new Vector(1.5, 1.5, 1.5)));
  }

  @Test
  public void testInAABB() throws Exception {
    Vector min = new Vector(0, 0, 0);
    Vector max = new Vector(1, 1, 1);
    Assert.assertFalse(V_ONE.isInAABB(min, max));
    Assert.assertTrue(V_ZERO.isInAABB(min, max));
    Assert.assertTrue(new Vector(0.5, 0.5, 0.5).isInAABB(min, max));

    Vector sphereCenter = V_ONE;
    Assert.assertTrue(new Vector(0.5, 0.5, 0.5).isInSphere(sphereCenter, 1));
  }

  @Test
  public void testNormalization() throws Exception {
    Assert.assertEquals(new Vector(1, 0, 0), new Vector(1, 0, 0).normalize());
    Assert.assertEquals(new Vector(0.816496, 0.408248, 0.408248), new Vector(2, 1, 1).normalize());
  }

  @Test
  public void testSerialization() throws Exception {
    Serializer ser = new VectorSerializer();
    Assert.assertEquals(V_ZERO, ser.deserialize("0, 0, 0", Vector.class));
    Assert.assertEquals(V_ONE, ser.deserialize("1, 1, 1", Vector.class));
    Assert.assertEquals(V_ONE, ser.deserialize("1, 1.0000001, 1", Vector.class));
    Assert.assertNotEquals(V_ZERO, ser.deserialize("1, 1, 1", Vector.class));

    Assert.assertEquals(ser.serialize(V_ZERO), "0,0,0");
    Assert.assertEquals(ser.serialize(V_ONE), "1,1,1");

    ser = new PositionSerializer();
    Assert.assertEquals(P_ZERO, ser.deserialize("0.0, 0.0, 0.0", Position.class));
    Assert.assertEquals(P_ZERO, ser.deserialize("0, 0, 0, 0", Position.class));
    Assert.assertEquals(P_ZERO, ser.deserialize("0, 0, 0, 0, 0", Position.class));

    Assert.assertEquals(P_ZERO.setYaw(1), ser.deserialize("0, 0, 0, 1", Position.class));
    Assert.assertEquals(P_ZERO.setYaw(1), ser.deserialize("0, 0, 0, 1, 0", Position.class));
    Assert.assertEquals(P_ZERO.setYaw(1).setPitch(1), ser.deserialize("0, 0, 0, 1, 1", Position.class));
    Assert.assertEquals(P_ZERO.setPitch(1), ser.deserialize("0, 0, 0, 0, 1", Position.class));

    Assert.assertEquals("0,0,0", ser.serialize(P_ZERO));
    Assert.assertEquals("1,1,1,1", ser.serialize(P_ONE.setYaw(1).setPitch(0)));
    Assert.assertEquals("1,1,1,1,1", ser.serialize(P_ONE.setYaw(1)));
    Assert.assertEquals("1,1,1,0,1", ser.serialize(P_ONE.setYaw(0).setPitch(1)));

  }

  public void testImmutability(BiFunction<Vector, Vector, Vector> func) {
    // No changes, return same instance
    Assert.assertTrue(V_ONE == func.apply(V_ONE, new Vector(0, 0, 0)));
    Assert.assertTrue(P_ONE == func.apply(P_ONE, new Vector(0, 0, 0)));
    Assert.assertTrue(V_ONE == func.apply(V_ONE, V_ZERO));
    Assert.assertTrue(P_ONE == func.apply(P_ONE, V_ZERO));
  }

  public void testMutability(BiFunction<Vector, Vector, Vector> func) {
    // Changes, returns new instance
    Assert.assertTrue(V_ONE != func.apply(V_ONE, V_ONE));
    Assert.assertTrue(P_ONE != func.apply(P_ONE, P_ONE));

    MutableVector mv = V_ONE.toMutableVector();
    Assert.assertTrue(V_ONE != mv);
    Assert.assertTrue(mv == mv.add(1));

    MutablePosition mp = V_ONE.toMutablePosition();
    Assert.assertTrue(P_ONE != mp);
    Assert.assertTrue(mp == mp.add(1));
  }

}
