package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for IntegerUtil. This class validates the functionality of the nextPowerOfTwo method,
 * which calculates the next power of two for a given positive integer.
 */
class IntegerUtilTest {

  @Test
  void testNextPowerOfTwo_whenNIsPowerOfTwo() {
    assertEquals(8, IntegerUtil.nextPowerOfTwo(8));
    assertEquals(16, IntegerUtil.nextPowerOfTwo(16));
    assertEquals(1, IntegerUtil.nextPowerOfTwo(1));
  }

  @Test
  void testNextPowerOfTwo_whenNIsNotPowerOfTwo() {
    assertEquals(8, IntegerUtil.nextPowerOfTwo(5));
    assertEquals(16, IntegerUtil.nextPowerOfTwo(9));
    assertEquals(32, IntegerUtil.nextPowerOfTwo(17));
  }

  @Test
  void testNextPowerOfTwo_whenNIsZero() {
    assertEquals(1, IntegerUtil.nextPowerOfTwo(0));
  }

  @Test
  void testNextPowerOfTwo_whenNIsLarge() {
    int n = (Integer.MAX_VALUE >> 1) + 1;
    assertEquals(n, IntegerUtil.nextPowerOfTwo(n));
  }

  @Test
  void testNextPowerOfTwo_whenNIsNegative() {
    assertThrows(IllegalArgumentException.class, () -> IntegerUtil.nextPowerOfTwo(-1));
  }
}
