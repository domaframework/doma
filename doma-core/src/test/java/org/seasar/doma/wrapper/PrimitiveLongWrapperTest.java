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

class PrimitiveLongWrapperTest {

  @Test
  void set() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.set(10);
    assertEquals(10L, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals(20L, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.set(null);
    assertEquals(0L, wrapper.get());
    wrapper.set((Number) null);
    assertEquals(0L, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    assertEquals(0L, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper(1);
    wrapper.increment();
    assertEquals(2L, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.increment();
    assertEquals(1L, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper(1);
    wrapper.decrement();
    assertEquals(0L, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    wrapper.decrement();
    assertEquals(-1L, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveLongWrapper wrapper = new PrimitiveLongWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
