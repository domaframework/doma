package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import example.entity.Emp;
import example.entity._Emp;
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

public class AutoInsertQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    InsertQuery insertQuery = query;
    assertNotNull(insertQuery.getSql());
  }

  @Test
  public void testOption_default() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertNull(parameters.get(2).getWrapper().get());
    assertEquals(1, parameters.get(3).getWrapper().get());
  }

  @Test
  public void testOption_excludeNull() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setNullExcluded(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("insert into EMP (ID, NAME, VERSION) values (?, ?, ?)", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertEquals(1, parameters.get(2).getWrapper().get());
  }

  @Test
  public void testOption_include() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setIncludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("insert into EMP (ID, NAME, VERSION) values (?, ?, ?)", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertEquals(1, parameters.get(2).getWrapper().get());
  }

  @Test
  public void testOption_exclude() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));

    AutoInsertQuery<Emp> query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setExcludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("insert into EMP (ID, SALARY, VERSION) values (?, ?, ?)", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(200), parameters.get(1).getWrapper().get());
    assertEquals(1, parameters.get(2).getWrapper().get());
  }
}
