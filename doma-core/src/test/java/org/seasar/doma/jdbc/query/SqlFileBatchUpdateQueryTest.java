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
import static org.junit.jupiter.api.Assertions.assertNull;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class SqlFileBatchUpdateQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertEquals(2, query.getSqls().size());
  }

  @Test
  public void testOption_default() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(200);

    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals("update emp set name = ?, salary = ? where id = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("update emp set name = ?, salary = ? where id = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(1).getWrapper().get());
    assertEquals(20, parameters.get(2).getWrapper().get());
  }

  @Test
  public void testIsExecutable() {
    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setParameterName("e");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setElements(Collections.emptyList());
    query.prepare();
    assertFalse(query.isExecutable());
  }

  @Test
  public void testPopulate() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), "testPopulate"));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityType(_Emp.getSingletonInternal());
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals(
        "update aaa set NAME = ?, SALARY = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
  }

  @Test
  public void testPopulate_include() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), "testPopulate"));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityType(_Emp.getSingletonInternal());
    query.setIncludedPropertyNames("name");
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals("update aaa set NAME = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
  }

  @Test
  public void testPopulate_exclude() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), "testPopulate"));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityType(_Emp.getSingletonInternal());
    query.setExcludedPropertyNames("name");
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals("update aaa set SALARY = ?, VERSION = ? + 1 where id = ?", sql.getRawSql());
  }

  @Test
  public void testPopulate_ignoreVersion() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<>(Emp.class);
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), "testPopulate"));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setEntityType(_Emp.getSingletonInternal());
    query.setVersionIgnored(true);
    query.prepare();

    assertEquals(2, query.getSqls().size());
    Sql<?> sql = query.getSql();
    assertEquals("update aaa set NAME = ?, SALARY = ?, VERSION = ? where id = ?", sql.getRawSql());
  }
}
