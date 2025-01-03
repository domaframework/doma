/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
