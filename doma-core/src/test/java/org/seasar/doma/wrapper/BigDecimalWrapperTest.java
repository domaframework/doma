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
package org.seasar.doma.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class BigDecimalWrapperTest {

  /** */
  @Test
  public void testSetBigDecimalAsNumber() {
    Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
    BigDecimalWrapper wrapper = new BigDecimalWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue, wrapper.get());
  }

  /** */
  @Test
  public void testSetBigInteger() {
    BigInteger greaterThanLongMaxValue =
        new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE).toBigInteger();
    BigDecimalWrapper wrapper = new BigDecimalWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(new BigDecimal(greaterThanLongMaxValue), wrapper.get());
  }

  @Test
  public void testSetNull() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
    wrapper.increment();
    assertEquals(new BigDecimal(11), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
    wrapper.decrement();
    assertEquals(new BigDecimal(9), wrapper.get());
  }

  @Test
  public void testHasEqualValue() {
    BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal("10"));
    assertTrue(wrapper.hasEqualValue(new BigDecimal("10.00")));
    assertNotEquals(new BigDecimal("10.00"), wrapper.get());
  }
}
