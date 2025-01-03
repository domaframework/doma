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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class BigIntegerWrapperTest {

  /** */
  @Test
  public void testSetBigIntegerAsNumber() {
    Number greaterThanLongMaxValue =
        new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE).toBigInteger();
    BigIntegerWrapper wrapper = new BigIntegerWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue, wrapper.get());
  }

  /** */
  @Test
  public void testSetBigDecimal() {
    BigDecimal greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(BigDecimal.ONE);
    BigIntegerWrapper wrapper = new BigIntegerWrapper();
    wrapper.set(greaterThanLongMaxValue);
    assertEquals(greaterThanLongMaxValue.toBigInteger(), wrapper.get());
  }

  @Test
  public void testSetNull() {
    BigIntegerWrapper wrapper = new BigIntegerWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    BigIntegerWrapper wrapper = new BigIntegerWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    BigIntegerWrapper wrapper = new BigIntegerWrapper(new BigInteger("10"));
    wrapper.increment();
    assertEquals(new BigInteger("11"), wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    BigIntegerWrapper wrapper = new BigIntegerWrapper(new BigInteger("10"));
    wrapper.decrement();
    assertEquals(new BigInteger("9"), wrapper.get());
  }
}
