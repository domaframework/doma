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
package org.seasar.doma.jdbc.criteria.tuple;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Tuple3Test {

  @Test
  void test() {
    Tuple3<Integer, String, Boolean> tuple = new Tuple3<>(1, "a", true);
    assertEquals(new Tuple3<>(1, "a", true), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(true, tuple.getItem3());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
    assertEquals(tuple.getItem3(), tuple.component3());
  }
}
