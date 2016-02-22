package com.supaham.commons.relatives;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

public class RelativeDurationTest {

  public static final Duration ONE = Duration.ofSeconds(1);
  public static final Duration TWO = Duration.ofSeconds(2);
  @Test
  public void testDouble() throws Exception {
    long millis = 20_000L;
    Duration duration = Duration.ofMillis(millis);

    RelativeDuration relative = RelativeDuration.from(ArithmeticOperator.ADDITION, duration);
    Assert.assertTrue(duration.equals(relative.apply(null)));
    Assert.assertTrue(duration.plusSeconds(1).equals(relative.apply(ONE)));

    relative = RelativeDuration.from(ArithmeticOperator.SUBTRACTION, duration);
    Assert.assertTrue(duration.minusSeconds(1).equals(relative.apply(ONE)));

    relative = RelativeDuration.from(ArithmeticOperator.MULTIPLICATION, duration);
    Assert.assertTrue(duration.multipliedBy(2).equals(relative.apply(TWO)));

    relative = RelativeDuration.from(ArithmeticOperator.DIVISION, duration);
    Assert.assertTrue(duration.dividedBy(2).equals(relative.apply(TWO)));

    relative = RelativeDuration.from(ArithmeticOperator.MODULUS, duration);
    Assert.assertTrue(Duration.ofMillis(millis % 2).equals(relative.apply(TWO)));

    relative = RelativeDuration.from(ArithmeticOperator.POWER, duration);
    Assert.assertTrue(Duration.ofSeconds(400).equals(relative.apply(TWO)));
  }

  @Test
  public void testEqualsAndHashCode() throws Exception {
    RelativeDuration first = RelativeDuration.from(ArithmeticOperator.ADDITION, ONE);
    RelativeDuration second = RelativeDuration.from(ArithmeticOperator.ADDITION, ONE);
    RelativeDuration third = RelativeDuration.from(ArithmeticOperator.SUBTRACTION, ONE);

    Assert.assertEquals(first, second);
    Assert.assertNotEquals(first, third);
    Assert.assertNotEquals(second, third);

    Assert.assertEquals(first.hashCode(), second.hashCode());
    Assert.assertNotEquals(first.hashCode(), third.hashCode());
    Assert.assertNotEquals(second.hashCode(), third.hashCode());
  }

  @Test
  public void testSerialization() throws Exception {
    RelativeDuration add = RelativeDuration.from(ArithmeticOperator.ADDITION, ONE);
    RelativeDuration subtract = RelativeDuration.from(ArithmeticOperator.SUBTRACTION, ONE);
    RelativeDuration multiplication = RelativeDuration.from(ArithmeticOperator.MULTIPLICATION, ONE);
    RelativeDuration division = RelativeDuration.from(ArithmeticOperator.DIVISION, ONE);
    RelativeDuration modulus = RelativeDuration.from(ArithmeticOperator.MODULUS, ONE);
    RelativeDuration power = RelativeDuration.from(ArithmeticOperator.POWER, ONE);

    // RelativeDuration serializes ADDITION with nothing but the number
    Assert.assertEquals("~1s", add.toString());
    Assert.assertEquals("~-1s", subtract.toString());
    Assert.assertEquals("~*1s", multiplication.toString());
    Assert.assertEquals("~/1s", division.toString());
    Assert.assertEquals("~%1s", modulus.toString());
    Assert.assertEquals("~^1s", power.toString());
  }

  @Test
  public void testDeserialization() throws Exception {
    RelativeDuration add = RelativeDuration.from(ArithmeticOperator.ADDITION, ONE);
    RelativeDuration subtract = RelativeDuration.from(ArithmeticOperator.SUBTRACTION, ONE);
    RelativeDuration multiplication = RelativeDuration.from(ArithmeticOperator.MULTIPLICATION, ONE);
    RelativeDuration division = RelativeDuration.from(ArithmeticOperator.DIVISION, ONE);
    RelativeDuration modulus = RelativeDuration.from(ArithmeticOperator.MODULUS, ONE);
    RelativeDuration power = RelativeDuration.from(ArithmeticOperator.POWER, ONE);

    Assert.assertEquals(RelativeDuration.ZERO, RelativeDuration.fromString(""));
    Assert.assertEquals(RelativeDuration.ZERO, RelativeDuration.fromString("~"));
    // RelativeDuration assumes ADDITION when no operator is present
    Assert.assertEquals(add, RelativeDuration.fromString("~1s"));
    Assert.assertEquals(add, RelativeDuration.fromString("~+1s"));
    Assert.assertEquals(add, RelativeDuration.fromString("~1")); // no unit defaults to seconds

    Assert.assertEquals(subtract, RelativeDuration.fromString("~-1s"));
    Assert.assertEquals(subtract, RelativeDuration.fromString("~-1"));

    Assert.assertEquals(multiplication, RelativeDuration.fromString("~*1s"));
    Assert.assertEquals(multiplication, RelativeDuration.fromString("~*1"));

    Assert.assertEquals(division, RelativeDuration.fromString("~/1s"));
    Assert.assertEquals(division, RelativeDuration.fromString("~/1"));

    Assert.assertEquals(modulus, RelativeDuration.fromString("~%1s"));
    Assert.assertEquals(modulus, RelativeDuration.fromString("~%1"));

    Assert.assertEquals(power, RelativeDuration.fromString("~^1s"));
    Assert.assertEquals(power, RelativeDuration.fromString("~^1"));
  }
}
