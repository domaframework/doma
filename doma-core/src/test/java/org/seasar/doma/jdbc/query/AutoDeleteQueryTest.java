package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import example.entity.Emp;
import example.entity.Salesman;
import example.entity._Emp;
import example.entity._Salesman;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AutoDeleteQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() {
    Emp emp = new Emp();

    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
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

    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
  }

  @Test
  public void testOption_ignoreVersion() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setVersionIgnored(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals("delete from EMP where ID = ?", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
  }

  @Test
  public void testTenantId() {
    Salesman salesman = new Salesman();
    salesman.setId(10);
    salesman.setName("aaa");
    salesman.setTenantId("bbb");
    salesman.setVersion(100);

    AutoDeleteQuery<Salesman> query = new AutoDeleteQuery<>(_Salesman.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntity(salesman);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "delete from SALESMAN where ID = ? and VERSION = ? and TENANT_ID = ?", sql.getRawSql());

    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
    assertEquals("bbb", parameters.get(2).getWrapper().get());
  }
}
