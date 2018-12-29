package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity.Salesman;
import example.entity._Emp;
import example.entity._Salesman;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoUpdateQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    UpdateQuery updateQuery = query;
    assertNotNull(updateQuery.getSql());
  }

  public void testOption_default() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
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
    assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
    assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
    assertEquals(new Integer(100), parameters.get(3).getWrapper().get());
  }

  public void testOption_excludeNull() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
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
    assertEquals(new Integer(100), parameters.get(0).getWrapper().get());
    assertEquals(new Integer(10), parameters.get(1).getWrapper().get());
    assertEquals(new Integer(100), parameters.get(2).getWrapper().get());
  }

  public void testOption_ignoreVersion() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
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
    assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
    assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
  }

  public void testOption_include() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
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
    assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
    assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
    assertEquals(new Integer(100), parameters.get(3).getWrapper().get());
  }

  public void testOption_exclude() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));
    emp.setVersion(100);

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
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
    assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
    assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
    assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
  }

  public void testIsExecutable() throws Exception {
    Emp emp = new Emp();
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertFalse(query.isExecutable());
  }

  public void testTenantId() throws Exception {
    Salesman salesman = new Salesman();
    salesman.setId(10);
    salesman.setName("aaa");
    salesman.setTenantId("bbb");
    salesman.setVersion(100);

    AutoUpdateQuery<Salesman> query =
        new AutoUpdateQuery<Salesman>(_Salesman.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
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
    assertEquals(new Integer(100), parameters.get(2).getWrapper().get());
    assertEquals(new Integer(10), parameters.get(3).getWrapper().get());
    assertEquals(new Integer(100), parameters.get(4).getWrapper().get());
    assertEquals("bbb", parameters.get(5).getWrapper().get());
  }
}
