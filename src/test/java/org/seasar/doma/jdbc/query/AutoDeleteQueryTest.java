package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity._Emp;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoDeleteQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    var emp = new Emp();

    var query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    DeleteQuery deleteQuery = query;
    assertNotNull(deleteQuery.getSql());
  }

  public void testOption_default() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    var query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSql();
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(2, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals(100, parameters.get(1).getWrapper().get());
  }

  public void testOption_ignoreVersion() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    var query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setVersionIgnored(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSql();
    assertEquals("delete from EMP where ID = ?", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(1, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
  }
}
