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
package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.AutoMultiInsertQuery;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class InsertCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testExecute() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(10);

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new InsertCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals("hoge", bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());
  }

  @Test
  public void testExecute_defaultVersion() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(null);

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new InsertCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals("hoge", bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(1, bindValues.get(3).getValue());
  }

  @Test
  public void testExecute_multiInsert() throws Exception {
    runtimeConfig.dialect = new H2Dialect();
    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 2;

    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(10);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("bar");
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(20);

    AutoMultiInsertQuery<Emp> query = new AutoMultiInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new InsertCommand(query).execute();
    query.complete();

    assertEquals(2, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?), (?, ?, ?, ?)", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals("hoge", bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());

    assertEquals(2, bindValues.get(4).getValue());
    assertEquals("bar", bindValues.get(5).getValue());
    assertEquals(new BigDecimal(2000), bindValues.get(6).getValue());
    assertEquals(20, bindValues.get(7).getValue());
  }

  @Test
  public void testExecute_defaultVersion_multiInsert() throws Exception {
    runtimeConfig.dialect = new H2Dialect();
    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 2;

    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(null);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("bar");
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(null);

    AutoMultiInsertQuery<Emp> query = new AutoMultiInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new InsertCommand(query).execute();
    query.complete();

    assertEquals(2, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?), (?, ?, ?, ?)", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(8, bindValues.size());
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals("hoge", bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(1, bindValues.get(3).getValue());
    assertEquals(2, bindValues.get(4).getValue());
    assertEquals("bar", bindValues.get(5).getValue());
    assertEquals(new BigDecimal(2000), bindValues.get(6).getValue());
    assertEquals(1, bindValues.get(7).getValue());
  }
}
