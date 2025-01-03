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

import org.junit.jupiter.api.Test;

class PrimitiveBooleanWrapperTest {

  @Test
  void set() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    wrapper.set(true);
    assertEquals(true, wrapper.get());
    wrapper.set(false);
    assertEquals(false, wrapper.get());
  }

  @Test
  void set_null() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    wrapper.set(null);
    assertEquals(false, wrapper.get());
  }

  @Test
  void getDefault() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    assertEquals(false, wrapper.getDefault());
  }

  @Test
  void isPrimitiveWrapper() {
    PrimitiveBooleanWrapper wrapper = new PrimitiveBooleanWrapper();
    assertTrue(wrapper.isPrimitiveWrapper());
  }
}
