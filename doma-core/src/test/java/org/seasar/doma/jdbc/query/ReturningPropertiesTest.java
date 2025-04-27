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
package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.*;

import example.entity._Emp;
import org.junit.jupiter.api.Test;

class ReturningPropertiesTest {

  @Test
  void isNone() {
    assertTrue(ReturningProperties.NONE.isNone());
    assertFalse(ReturningProperties.ALL.isNone());
  }

  @Test
  void isAll() {
    assertFalse(ReturningProperties.NONE.isAll());
    assertTrue(ReturningProperties.ALL.isAll());
  }

  @Test
  void resolve_none() {
    var e = _Emp.getSingletonInternal();
    var propertyTypes = ReturningProperties.NONE.resolve(e);
    assertEquals(0, propertyTypes.size());
  }

  @Test
  void resolve_all() {
    var e = _Emp.getSingletonInternal();
    var propertyTypes = ReturningProperties.ALL.resolve(e);
    assertEquals(4, propertyTypes.size());
    assertEquals(e.id, propertyTypes.get(0));
    assertEquals(e.name, propertyTypes.get(1));
    assertEquals(e.salary, propertyTypes.get(2));
    assertEquals(e.version, propertyTypes.get(3));
  }
}
