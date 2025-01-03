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
package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.entity.EntityType;

public class MetamodelTest {

  @Test
  void tableName() {
    Employee_ e = new Employee_("MY_EMP");
    EntityType<?> entityType = e.asType();
    String tableName = entityType.getQualifiedTableName(null, null);
    assertEquals("MY_EMP", tableName);
  }

  @Test
  void tableName_single_quotation() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab'c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void tableName_semicolon() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab;c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void tableName_two_hyphens() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab--c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }

  @Test
  void tableName_slash() {
    DomaIllegalArgumentException ex =
        assertThrows(
            DomaIllegalArgumentException.class,
            () -> {
              Employee_ e = new Employee_("ab/*c");
              e.asType();
            });
    System.out.println(ex.getMessage());
  }
}
