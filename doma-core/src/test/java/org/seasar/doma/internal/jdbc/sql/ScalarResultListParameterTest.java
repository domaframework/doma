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
package org.seasar.doma.internal.jdbc.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.IntegerWrapper;

class ScalarResultListParameterTest {

  @Test
  void getResult() {
    ScalarResultListParameter<Integer, Integer> parameter =
        new ScalarResultListParameter<>(() -> new BasicScalar<>(IntegerWrapper::new));
    parameter.add(1);
    parameter.add(2);
    List<Integer> result = parameter.getResult();
    assertEquals(2, result.size());
    assertEquals(1, result.get(0));
    assertEquals(2, result.get(1));
  }
}
