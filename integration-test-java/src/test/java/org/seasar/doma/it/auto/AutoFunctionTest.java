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
package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.FunctionDao;
import org.seasar.doma.it.dao.FunctionDaoImpl;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ResultMappingException;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(unless = {Dbms.HSQLDB, Dbms.H2, Dbms.DB2, Dbms.SQLITE})
public class AutoFunctionTest {

  @Test
  public void testNoParam(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    Integer result = dao.func_none_param();
    assertEquals(Integer.valueOf(10), result);
  }

  @Test
  public void testOneParam(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    Integer result = dao.func_simpletype_param(10);
    assertEquals(Integer.valueOf(20), result);
  }

  @Test
  public void testOneParam_time(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    Time result = dao.func_simpletype_time_param(Time.valueOf("12:34:56"));
    assertEquals(Time.valueOf("12:34:56"), result);
  }

  @Test
  public void testTwoParams(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    Integer result = dao.func_dto_param(10, 20);
    assertEquals(Integer.valueOf(30), result);
  }

  @Test
  public void testTwoParams_time(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    Time result = dao.func_dto_time_param(Time.valueOf("12:34:56"), 20);
    assertEquals(Time.valueOf("12:34:56"), result);
  }

  @Test
  @Run(onlyIf = {Dbms.POSTGRESQL})
  public void testScalarResultSet(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    List<String> result = dao.func_simpletype_resultset(1);
    assertEquals(13, result.size());
    assertEquals("ALLEN", result.get(0));
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER})
  public void testResultSet(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    List<Employee> result = dao.func_resultset(1);
    assertEquals(13, result.size());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER})
  public void testResultSet_check(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    try {
      dao.func_resultset_check(1);
      fail();
    } catch (ResultMappingException ignored) {
      System.err.println(ignored);
    }
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER})
  public void testResultSet_noCheck(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    List<Employee> result = dao.func_resultset_nocheck(1);
    assertEquals(13, result.size());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER})
  public void testResultSet_map(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    List<Map<String, Object>> result = dao.func_resultset_map(1);
    assertEquals(13, result.size());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER})
  public void testResultSetAndUpdate(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    List<Employee> result = dao.func_resultset_update(1);
    assertEquals(13, result.size());
    DepartmentDao departmentDao = new DepartmentDaoImpl(config);
    Department department = departmentDao.selectById(1);
    assertEquals("HOGE", department.getDepartmentName());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.SQLSERVER})
  public void testResultSetAndUpdate2(Config config) {
    FunctionDao dao = new FunctionDaoImpl(config);
    List<Employee> result = dao.func_resultset_update2(1);
    assertEquals(13, result.size());
    DepartmentDao departmentDao = new DepartmentDaoImpl(config);
    Department department = departmentDao.selectById(1);
    assertEquals("HOGE", department.getDepartmentName());
  }
}
