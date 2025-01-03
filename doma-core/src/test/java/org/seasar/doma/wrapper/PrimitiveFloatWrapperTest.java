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

class PrimitiveFloatWrapperTest {

  @Test
  void set() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.set(10);
    assertEquals(10f, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals(20f, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.set(null);
    assertEquals(0f, wrapper.get());
    wrapper.set((Number) null);
    assertEquals(0f, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    assertEquals(0f, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper(1);
    wrapper.increment();
    assertEquals(2f, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.increment();
    assertEquals(1f, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper(1);
    wrapper.decrement();
    assertEquals(0f, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    wrapper.decrement();
    assertEquals(-1f, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveFloatWrapper wrapper = new PrimitiveFloatWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
