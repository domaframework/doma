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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class Tuple8Test {

  @Test
  void test() {
    Tuple8<Integer, String, Boolean, Long, BigDecimal, Integer, String, Boolean> tuple =
        new Tuple8<>(1, "a", true, 1L, BigDecimal.ONE, 2, "b", false);
    assertEquals(new Tuple8<>(1, "a", true, 1L, BigDecimal.ONE, 2, "b", false), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(true, tuple.getItem3());
    assertEquals(1L, tuple.getItem4());
    assertEquals(BigDecimal.ONE, tuple.getItem5());
    assertEquals(2, tuple.getItem6());
    assertEquals("b", tuple.getItem7());
    assertEquals(false, tuple.getItem8());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
    assertEquals(tuple.getItem3(), tuple.component3());
    assertEquals(tuple.getItem4(), tuple.component4());
    assertEquals(tuple.getItem5(), tuple.component5());
    assertEquals(tuple.getItem6(), tuple.component6());
    assertEquals(tuple.getItem7(), tuple.component7());
    assertEquals(tuple.getItem8(), tuple.component8());
  }
}
