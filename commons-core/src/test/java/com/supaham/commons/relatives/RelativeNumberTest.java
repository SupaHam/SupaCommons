package com.supaham.commons.relatives;

import org.junit.Assert;
import org.junit.Test;

public class RelativeNumberTest {

  @Test
  public void testDouble() throws Exception {
    Double original = 2.0D;
    RelativeNumber relative = RelativeNumber.from(ArithmeticOperator.ADDITION, original);
    Assert.assertTrue(original.equals(relative.apply(null)));
    Assert.assertTrue(original + 1 == relative.apply(1));

    relative = RelativeNumber.from(ArithmeticOperator.SUBTRACTION, original);
    Assert.assertTrue(original - 1 == relative.apply(1));

    relative = RelativeNumber.from(ArithmeticOperator.MULTIPLICATION, original);
    Assert.assertTrue(original * 2 == relative.apply(2));

    relative = RelativeNumber.from(ArithmeticOperator.DIVISION, original);
    Assert.assertTrue(original / 2 == relative.apply(2));

    relative = RelativeNumber.from(ArithmeticOperator.MODULUS, original);
    Assert.assertTrue(original % 2 == relative.apply(2));

    relative = RelativeNumber.from(ArithmeticOperator.POWER, original);
    Assert.assertTrue(Math.pow(original, 2) == relative.apply(2));
  }

  @Test
  public void testNumberOverflow() throws Exception {
    RelativeNumber from = RelativeNumber.from(ArithmeticOperator.MULTIPLICATION, Double.MAX_VALUE);
    Assert.assertTrue(Double.POSITIVE_INFINITY == from.apply(2));
    Assert.assertTrue(Double.NEGATIVE_INFINITY == from.apply(-2));
  }

  @Test
  public void testEqualsAndHashCode() throws Exception {
    RelativeNumber first = RelativeNumber.from(ArithmeticOperator.ADDITION, 1);
    RelativeNumber second = RelativeNumber.from(ArithmeticOperator.ADDITION, 1);
    RelativeNumber third = RelativeNumber.from(ArithmeticOperator.SUBTRACTION, 1);

    Assert.assertEquals(first, second);
    Assert.assertNotEquals(first, third);
    Assert.assertNotEquals(second, third);

    Assert.assertEquals(first.hashCode(), second.hashCode());
    Assert.assertNotEquals(first.hashCode(), third.hashCode());
    Assert.assertNotEquals(second.hashCode(), third.hashCode());
  }

  @Test
  public void testSerialization() throws Exception {
    RelativeNumber add = RelativeNumber.from(ArithmeticOperator.ADDITION, 1);
    RelativeNumber subtract = RelativeNumber.from(ArithmeticOperator.SUBTRACTION, 1);
    RelativeNumber multiplication = RelativeNumber.from(ArithmeticOperator.MULTIPLICATION, 1);
    RelativeNumber division = RelativeNumber.from(ArithmeticOperator.DIVISION, 1);
    RelativeNumber modulus = RelativeNumber.from(ArithmeticOperator.MODULUS, 1);
    RelativeNumber power = RelativeNumber.from(ArithmeticOperator.POWER, 1);

    // RelativeNumber serializes ADDITION with nothing but the number
    Assert.assertEquals("~1", add.toString());
    Assert.assertEquals("~-1", subtract.toString());
    Assert.assertEquals("~*1", multiplication.toString());
    Assert.assertEquals("~/1", division.toString());
    Assert.assertEquals("~%1", modulus.toString());
    Assert.assertEquals("~^1", power.toString());
  }

  @Test
  public void testDeserialization() throws Exception {
    RelativeNumber add = RelativeNumber.from(ArithmeticOperator.ADDITION, 1);
    RelativeNumber subtract = RelativeNumber.from(ArithmeticOperator.SUBTRACTION, 1);
    RelativeNumber multiplication = RelativeNumber.from(ArithmeticOperator.MULTIPLICATION, 1);
    RelativeNumber division = RelativeNumber.from(ArithmeticOperator.DIVISION, 1);
    RelativeNumber modulus = RelativeNumber.from(ArithmeticOperator.MODULUS, 1);
    RelativeNumber power = RelativeNumber.from(ArithmeticOperator.POWER, 1);

    Assert.assertEquals(RelativeNumber.ZERO, RelativeNumber.fromString(""));
    Assert.assertEquals(RelativeNumber.ZERO, RelativeNumber.fromString("~"));
    // RelativeNumber assumes ADDITION when no operator is present
    Assert.assertEquals(add, RelativeNumber.fromString("~1"));
    Assert.assertEquals(add, RelativeNumber.fromString("~+1"));

    Assert.assertEquals(subtract, RelativeNumber.fromString("~-1"));
    Assert.assertEquals(multiplication, RelativeNumber.fromString("~*1"));
    Assert.assertEquals(division, RelativeNumber.fromString("~/1"));
    Assert.assertEquals(modulus, RelativeNumber.fromString("~%1"));
    Assert.assertEquals(power, RelativeNumber.fromString("~^1"));
  }
}
