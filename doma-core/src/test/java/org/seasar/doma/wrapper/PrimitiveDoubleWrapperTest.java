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

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrimitiveDoubleWrapperTest {

  @Test
  void set() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper();
    wrapper.set(10);
    assertEquals(10d, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals(20d, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper();
    wrapper.set(null);
    assertEquals(0d, wrapper.get());
    wrapper.set((Number) null);
    assertEquals(0d, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper();
    assertEquals(0d, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper(1);
    wrapper.increment();
    assertEquals(2d, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper();
    wrapper.increment();
    assertEquals(1d, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper(1);
    wrapper.decrement();
    assertEquals(0d, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper();
    wrapper.decrement();
    assertEquals(-1d, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveDoubleWrapper wrapper = new PrimitiveDoubleWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
