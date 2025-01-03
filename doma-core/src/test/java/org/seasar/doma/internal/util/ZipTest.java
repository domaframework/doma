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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ZipTest {

  @Test
  void stream() {
    List<String> a = Arrays.asList("a", "b", "c");
    List<Integer> b = Arrays.asList(1, 2, 3, 4, 5);
    List<Pair<String, Integer>> result = Zip.stream(a, b).toList();
    assertEquals(3, result.size());
    assertEquals("a", result.get(0).fst);
    assertEquals(Integer.valueOf(1), result.get(0).snd);
    assertEquals("b", result.get(1).fst);
    assertEquals(Integer.valueOf(2), result.get(1).snd);
    assertEquals("c", result.get(2).fst);
    assertEquals(Integer.valueOf(3), result.get(2).snd);
  }
}
