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

class PrimitiveShortWrapperTest {

  @Test
  void set() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.set((short) 10);
    assertEquals((short) 10, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals((short) 20, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.set(null);
    assertEquals((short) 0, wrapper.get());
    wrapper.set((Number) null);
    assertEquals((short) 0, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    assertEquals((short) 0, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper((short) 1);
    wrapper.increment();
    assertEquals((short) 2, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.increment();
    assertEquals((short) 1, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper((short) 1);
    wrapper.decrement();
    assertEquals((short) 0, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    wrapper.decrement();
    assertEquals((short) -1, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveShortWrapper wrapper = new PrimitiveShortWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
