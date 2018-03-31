package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoInsertQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    var query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    InsertQuery insertQuery = query;
    assertNotNull(insertQuery.getSql());
  }

  public void testOption_default() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    var query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals(Integer.valueOf(10), parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertNull(parameters.get(2).getWrapper().get());
    assertEquals(Integer.valueOf(1), parameters.get(3).getWrapper().get());
  }

  public void testOption_excludeNull() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    var query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setNullExcluded(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSql();
    assertEquals("insert into EMP (ID, NAME, VERSION) values (?, ?, ?)", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(Integer.valueOf(10), parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertEquals(Integer.valueOf(1), parameters.get(2).getWrapper().get());
  }

  public void testOption_include() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));

    var query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setIncludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSql();
    assertEquals("insert into EMP (ID, NAME, VERSION) values (?, ?, ?)", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(Integer.valueOf(10), parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertEquals(Integer.valueOf(1), parameters.get(2).getWrapper().get());
  }

  public void testOption_exclude() throws Exception {
    var emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));

    var query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setExcludedPropertyNames("name");
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSql();
    assertEquals("insert into EMP (ID, SALARY, VERSION) values (?, ?, ?)", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(Integer.valueOf(10), parameters.get(0).getWrapper().get());
    assertEquals(new BigDecimal(200), parameters.get(1).getWrapper().get());
    assertEquals(Integer.valueOf(1), parameters.get(2).getWrapper().get());
  }
}
