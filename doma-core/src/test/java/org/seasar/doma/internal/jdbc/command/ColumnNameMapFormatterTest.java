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
package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity._Dept;
import example.entity._Emp;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ColumnNameMapFormatterTest {

  @Test
  void test_format_withPopulatedMap() {
    _Emp emp = _Emp.getSingletonInternal();
    _Dept dept = _Dept.getSingletonInternal();

    Map<String, MappingSupport.PropType> map = new HashMap<>();
    map.put(
        emp.version.getColumnName().toLowerCase(),
        new MappingSupport.PropType(emp, emp.version, ""));
    map.put(
        dept.name.getColumnName().toLowerCase(), new MappingSupport.PropType(dept, dept.name, ""));
    map.put(
        emp.salary.getColumnName().toLowerCase(), new MappingSupport.PropType(emp, emp.salary, ""));

    String expected =
        """
        ------------------------------------------------------
        Lowercase Column Name -> Property Name (Entity Name)
        ------------------------------------------------------
        name -> name (Dept)
        salary -> salary (Emp)
        version -> version (Emp)
        ------------------------------------------------------""";

    String actual = ColumnNameMapFormatter.format(map);

    assertEquals(expected, actual);
  }
}
