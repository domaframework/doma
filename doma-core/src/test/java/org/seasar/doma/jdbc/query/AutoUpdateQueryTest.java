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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import example.entity.Emp;
import example.entity.Salesman;
import example.entity._Emp;
import example.entity._Salesman;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AutoUpdateQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertNotNull(query.getSql());
  }

  @Test
  public void testOption_default() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());

    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertEquals(100, parameters.get(3).getWrapper().get());
  }

  @Test
  public void testOption_excludeNull() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setNullExcluded(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("update EMP set VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(100, parameters.get(0).getWrapper().get());
    assertEquals(10, parameters.get(1).getWrapper().get());
    assertEquals(100, parameters.get(2).getWrapper().get());
  }

  @Test
  public void testOption_ignoreVersion() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setVersionIgnored(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("update EMP set NAME = ?, VERSION = ? where ID = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
  }

  @Test
  public void testOption_include() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setIncludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertEquals(100, parameters.get(3).getWrapper().get());
  }

  @Test
  public void testOption_exclude() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setExcludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "update EMP set SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals(new BigDecimal(200), parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
  }

  @Test
  public void testIsExecutable() {
    Emp emp = new Emp();
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertFalse(query.isExecutable());
  }

  @Test
  public void testTenantId() {
    Salesman salesman = new Salesman();
    salesman.setId(10);
    salesman.setName("aaa");
    salesman.setTenantId("bbb");
    salesman.setVersion(100);

    AutoUpdateQuery<Salesman> query = new AutoUpdateQuery<>(_Salesman.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(salesman);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "update SALESMAN set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ? and TENANT_ID = ?",
        sql.getRawSql());

    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(6, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(100, parameters.get(2).getWrapper().get());
    assertEquals(10, parameters.get(3).getWrapper().get());
    assertEquals(100, parameters.get(4).getWrapper().get());
    assertEquals("bbb", parameters.get(5).getWrapper().get());
  }
}
