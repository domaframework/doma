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
import java.util.List;
import org.junit.jupiter.api.Test;

class ReturningPropertyNamesTest {

  @Test
  public void of_empty() {
    var properties = ReturningPropertyNames.of(List.of(), List.of());
    assertEquals(ReturningProperties.ALL, properties);
  }

  @Test
  public void resolve_inclusion() {
    var e = _Emp.getSingletonInternal();
    var returning = ReturningPropertyNames.of(List.of("id", "name"), List.of());
    var propertyTypes = returning.resolve(e);
    assertEquals(2, propertyTypes.size());
    assertEquals(e.id, propertyTypes.get(0));
    assertEquals(e.name, propertyTypes.get(1));
  }

  @Test
  public void resolve_exclusion() {
    var e = _Emp.getSingletonInternal();
    var returning = ReturningPropertyNames.of(List.of(), List.of("id", "name"));
    var propertyTypes = returning.resolve(e);
    assertEquals(2, propertyTypes.size());
    assertEquals(e.salary, propertyTypes.get(0));
    assertEquals(e.version, propertyTypes.get(1));
  }

  @Test
  public void resolve_inclusion_exclusion() {
    var e = _Emp.getSingletonInternal();
    var returning = ReturningPropertyNames.of(List.of("id", "name"), List.of("name"));
    var propertyTypes = returning.resolve(e);
    assertEquals(1, propertyTypes.size());
    assertEquals(e.id, propertyTypes.get(0));
  }
}
