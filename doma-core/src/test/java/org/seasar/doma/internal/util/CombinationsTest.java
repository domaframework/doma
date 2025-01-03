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
package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CombinationsTest {

  @Test
  void test() {
    Combinations<String> combinations = new Combinations<>();
    combinations.add(new Pair<>("A", "B"));
    combinations.add(new Pair<>("A", "C"));

    assertTrue(combinations.contains(new Pair<>("A", "B")));
    assertTrue(combinations.contains(new Pair<>("B", "A")));
    assertTrue(combinations.contains(new Pair<>("A", "C")));
    assertTrue(combinations.contains(new Pair<>("C", "A")));
    assertFalse(combinations.contains(new Pair<>("B", "C")));
    assertFalse(combinations.contains(new Pair<>("C", "B")));
  }
}
