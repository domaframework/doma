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
package org.seasar.doma.it.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.builder.SelectBuilder;

@ExtendWith(IntegrationTestEnvironment.class)
public class SelectBuilderTest {

  @Test
  public void testGetScalarSingleResult(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    String name = builder.getScalarSingleResult(String.class);
    assertEquals("SMITH", name);
  }

  @Test
  public void testGetScalarSingleResult_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    String name = builder.getScalarSingleResult(String.class);
    assertNull(name);
  }

  @Test
  public void testGetOptionalScalarSingleResult(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Optional<String> name = builder.getOptionalScalarSingleResult(String.class);
    assertEquals("SMITH", name.get());
  }

  @Test
  public void testGetOptionalScalarSingleResult_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    Optional<String> name = builder.getOptionalScalarSingleResult(String.class);
    assertFalse(name.isPresent());
  }

  @Test
  public void testGetScalarResultList(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    List<String> list = builder.getScalarResultList(String.class);
    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0));
  }

  @Test
  public void testGetScalarResultList_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select null from EMPLOYEE");
    List<String> list = builder.getScalarResultList(String.class);
    assertEquals(14, list.size());
    assertNull(list.get(0));
  }

  @Test
  public void testGetOptionalScalarResultList(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    List<Optional<String>> list = builder.getOptionalScalarResultList(String.class);
    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0).get());
  }

  @Test
  public void testGetOptionalScalarResultList_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select null from EMPLOYEE");
    List<Optional<String>> list = builder.getOptionalScalarResultList(String.class);
    assertEquals(14, list.size());
    assertFalse(list.get(0).isPresent());
  }

  @Test
  public void testStreamScalar(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Optional<String> name = builder.streamScalar(String.class, Stream::findFirst);
    assertEquals("SMITH", name.get());
  }

  @Test
  public void testStreamScalar_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select null from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    Optional<String> name = builder.streamScalar(String.class, Stream::findFirst);
    assertFalse(name.isPresent());
  }

  @Test
  public void testStreamOptionalScalar(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_NAME from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Optional<Optional<String>> name = builder.streamOptionalScalar(String.class, Stream::findFirst);
    assertEquals("SMITH", name.get().get());
  }

  @Test
  public void testStreamOptionalScalar_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select null from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Optional<Optional<String>> name = builder.streamOptionalScalar(String.class, Stream::findFirst);
    assertFalse(name.get().isPresent());
  }

  @Test
  public void testGetMapSingleResult(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Map<String, Object> employee = builder.getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(employee);
    assertNotNull(employee.get("employeeId"));
    assertNotNull(employee.get("employeeName"));
    assertNotNull(employee.get("hiredate"));
  }

  @Test
  public void testGetMapSingleResult_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    Map<String, Object> employee = builder.getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
    assertNull(employee);
  }

  @Test
  public void testGetOptionalMapSingleResult(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Optional<Map<String, Object>> employee =
        builder.getOptionalMapSingleResult(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(employee);
    assertEquals("SMITH", employee.get().get("employeeName"));
  }

  @Test
  public void testGetOptionalMapSingleResult_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    Optional<Map<String, Object>> employee =
        builder.getOptionalMapSingleResult(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(employee);
    assertFalse(employee.isPresent());
  }

  @Test
  public void testGetMapResultList(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    List<Map<String, Object>> employees = builder.getMapResultList(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(employees);
    assertEquals(14, employees.size());
  }

  @Test
  public void testStreamMap(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    Optional<Map<String, Object>> result =
        builder.streamMap(MapKeyNamingType.CAMEL_CASE, Stream::findFirst);
    assertEquals("SMITH", result.get().get("employeeName"));
  }

  @Test
  public void testGetEntitySingleResult(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Employee employee = builder.getEntitySingleResult(Employee.class);
    assertNotNull(employee);
    assertEquals("SMITH", employee.getEmployeeName());
  }

  @Test
  public void testGetEntitySingleResult_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    Employee employee = builder.getEntitySingleResult(Employee.class);
    assertNull(employee);
  }

  @Test
  public void testGetOptionalEntitySingleResult(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 1);
    Optional<Employee> employee = builder.getOptionalEntitySingleResult(Employee.class);
    assertNotNull(employee);
    assertEquals("SMITH", employee.get().getEmployeeName());
  }

  @Test
  public void testGetOptionalEntitySingleResult_null(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    builder.sql("where");
    builder.sql("EMPLOYEE_ID = ").param(int.class, 99);
    Optional<Employee> employee = builder.getOptionalEntitySingleResult(Employee.class);
    assertNotNull(employee);
    assertFalse(employee.isPresent());
  }

  @Test
  public void testGetEntityResultList(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    List<Employee> employees = builder.getEntityResultList(Employee.class);
    assertEquals(14, employees.size());
    assertEquals("SMITH", employees.get(0).getEmployeeName());
  }

  @Test
  public void testStreamEntity(Config config) {
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    Optional<Employee> employee = builder.streamEntity(Employee.class, Stream::findFirst);
    assertEquals("SMITH", employee.get().getEmployeeName());
  }

  @Test
  public void testStreamEntity_resultStream(Config config) {
    Optional<Employee> employee;
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select EMPLOYEE_ID, EMPLOYEE_NAME, HIREDATE from EMPLOYEE");
    try (Stream<Employee> stream = builder.streamEntity(Employee.class)) {
      employee = stream.findFirst();
    }
    assertTrue(employee.isPresent());
    assertEquals("SMITH", employee.get().getEmployeeName());
  }

  @Test
  public void testSelectBuilderInDao(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectWithBuilder();
    assertEquals(14, employees.size());
  }

  @Test
  public void testStreamBuilderInDao(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    try (Stream<Employee> employees = dao.streamWithBuilder()) {
      assertEquals(14, employees.count());
    }
  }
}
