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

import org.junit.jupiter.api.Test;

public class LongWrapperTest {

  @Test
  public void testSetNull() {
    LongWrapper wrapper = new LongWrapper();
    wrapper.set(null);
    assertNull(wrapper.get());
  }

  @Test
  public void testSetNullNumber() {
    LongWrapper wrapper = new LongWrapper();
    wrapper.set((Number) null);
    assertNull(wrapper.get());
  }

  /** */
  @Test
  public void testIncrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.increment();
    assertEquals(11L, wrapper.get());
  }

  /** */
  @Test
  public void testDecrement() {
    LongWrapper wrapper = new LongWrapper(10L);
    wrapper.decrement();
    assertEquals(9L, wrapper.get());
  }
}
