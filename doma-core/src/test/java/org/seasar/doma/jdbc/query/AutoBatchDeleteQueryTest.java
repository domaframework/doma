package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import example.entity.Emp;
import example.entity.Salesman;
import example.entity._Emp;
import example.entity._Salesman;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AutoBatchDeleteQueryTest {

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

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");

    AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
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

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(10);

    AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(20, parameters.get(0).getWrapper().get());
    assertEquals(10, parameters.get(1).getWrapper().get());
  }

  @Test
  public void testOption_ignoreVersion() {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(10);

    AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setVersionIgnored(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals("delete from EMP where ID = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("delete from EMP where ID = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals(20, parameters.get(0).getWrapper().get());
  }

  @Test
  public void testTenantId() {
    Salesman emp1 = new Salesman();
    emp1.setId(10);
    emp1.setTenantId("bbb");
    emp1.setVersion(1);

    Salesman emp2 = new Salesman();
    emp2.setId(20);
    emp2.setTenantId("bbb");
    emp2.setVersion(2);

    AutoBatchDeleteQuery<Salesman> query =
        new AutoBatchDeleteQuery<>(_Salesman.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals(
        "delete from SALESMAN where ID = ? and VERSION = ? and TENANT_ID = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals(1, parameters.get(1).getWrapper().get());
    assertEquals("bbb", parameters.get(2).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals(
        "delete from SALESMAN where ID = ? and VERSION = ? and TENANT_ID = ?", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(20, parameters.get(0).getWrapper().get());
    assertEquals(2, parameters.get(1).getWrapper().get());
    assertEquals("bbb", parameters.get(2).getWrapper().get());
  }
}
