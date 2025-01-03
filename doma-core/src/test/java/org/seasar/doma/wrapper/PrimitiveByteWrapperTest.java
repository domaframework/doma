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

class PrimitiveByteWrapperTest {

  @Test
  void set() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.set((byte) 10);
    assertEquals((byte) 10, wrapper.get());
    wrapper.set(new BigDecimal(20));
    assertEquals((byte) 20, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.set(null);
    assertEquals((byte) 0, wrapper.get());
    wrapper.set((Number) null);
    assertEquals((byte) 0, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    assertEquals((byte) 0, wrapper.getDefault());
  }

  @Test
  void increment() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper((byte) 1);
    wrapper.increment();
    assertEquals((byte) 2, wrapper.get());
  }

  @Test
  void increment_fromDefaultValue() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.increment();
    assertEquals((byte) 1, wrapper.get());
  }

  @Test
  void decrement() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper((byte) 1);
    wrapper.decrement();
    assertEquals((byte) 0, wrapper.get());
  }

  @Test
  void decrement_fromDefaultValue() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    wrapper.decrement();
    assertEquals((byte) -1, wrapper.get());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveByteWrapper wrapper = new PrimitiveByteWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
