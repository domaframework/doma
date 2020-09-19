package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import example.entity.Emp;
import example.entity.Salesman;
import example.entity._Emp;
import example.entity._Salesman;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoBatchUpdateQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BatchUpdateQuery batchUpdateQuery = query;
    assertEquals(2, batchUpdateQuery.getSqls().size());
  }

  @Test
  public void testOption_default() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(200);

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals(
        "update EMP set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?",
        sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(5, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(100, parameters.get(2).getWrapper().get());
    assertEquals(10, parameters.get(3).getWrapper().get());
    assertEquals(100, parameters.get(4).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals(
        "update EMP set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?",
        sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(5, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(1).getWrapper().get());
    assertEquals(200, parameters.get(2).getWrapper().get());
    assertEquals(20, parameters.get(3).getWrapper().get());
    assertEquals(200, parameters.get(4).getWrapper().get());
  }

  @Test
  public void testOption_ignoreVersion() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(200);

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setVersionIgnored(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals("update EMP set NAME = ?, SALARY = ?, VERSION = ? where ID = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(100, parameters.get(2).getWrapper().get());
    assertEquals(10, parameters.get(3).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("update EMP set NAME = ?, SALARY = ?, VERSION = ? where ID = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(1).getWrapper().get());
    assertEquals(200, parameters.get(2).getWrapper().get());
    assertEquals(20, parameters.get(3).getWrapper().get());
  }

  @Test
  public void testOption_include() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setSalary(new BigDecimal(200));
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setVersion(200);

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setIncludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals(
        "update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals("aaa", parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertEquals(100, parameters.get(3).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals(
        "update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(200, parameters.get(1).getWrapper().get());
    assertEquals(20, parameters.get(2).getWrapper().get());
    assertEquals(200, parameters.get(3).getWrapper().get());
  }

  @Test
  public void testOption_exclude() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setSalary(new BigDecimal(200));
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setVersion(200);

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setExcludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals(
        "update EMP set SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals(new BigDecimal(200), parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals(10, parameters.get(2).getWrapper().get());
    assertEquals(100, parameters.get(3).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals(
        "update EMP set SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(200, parameters.get(1).getWrapper().get());
    assertEquals(20, parameters.get(2).getWrapper().get());
    assertEquals(200, parameters.get(3).getWrapper().get());
  }

  @Test
  public void testIsExecutable() throws Exception {
    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setEntities(Collections.<Emp>emptyList());
    query.prepare();
    assertFalse(query.isExecutable());
  }

  @Test
  public void testTenantId() throws Exception {
    Salesman s1 = new Salesman();
    s1.setId(10);
    s1.setName("aaa");
    s1.setTenantId("bbb");
    s1.setVersion(100);

    Salesman s2 = new Salesman();
    s2.setId(20);
    s2.setSalary(new BigDecimal(2000));
    s2.setTenantId("bbb");
    s2.setVersion(200);

    AutoBatchUpdateQuery<Salesman> query =
        new AutoBatchUpdateQuery<>(_Salesman.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(s1, s2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
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

    sql = query.getSqls().get(1);
    assertEquals(
        "update SALESMAN set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ? and TENANT_ID = ?",
        sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(6, parameters.size());
    assertNull(parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(1).getWrapper().get());
    assertEquals(200, parameters.get(2).getWrapper().get());
    assertEquals(20, parameters.get(3).getWrapper().get());
    assertEquals(200, parameters.get(4).getWrapper().get());
    assertEquals("bbb", parameters.get(5).getWrapper().get());
  }
}
