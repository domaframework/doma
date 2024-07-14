package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import example.entity.AutoIncrement;
import example.entity.Emp;
import example.entity._AutoIncrement;
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
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.message.Message;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AutoMultiInsertQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp.setName("bbb");

    AutoMultiInsertQuery<Emp> query = new AutoMultiInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertNotNull(query.getSql());
  }

  @Test
  public void testPrepare_MySql_autoIncrement() {
    runtimeConfig.setDialect(new MysqlDialect());

    AutoIncrement autoIncrement = new AutoIncrement();

    AutoMultiInsertQuery<AutoIncrement> query =
        new AutoMultiInsertQuery<>(_AutoIncrement.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Collections.singletonList(autoIncrement));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertEquals("insert into AutoIncrement () values ()", query.getSql().getRawSql());
  }

  @Test
  public void testPrepare_Mssql_autoIncrement() {
    runtimeConfig.setDialect(new MssqlDialect());

    AutoIncrement autoIncrement = new AutoIncrement();

    AutoMultiInsertQuery<AutoIncrement> query =
        new AutoMultiInsertQuery<>(_AutoIncrement.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Collections.singletonList(autoIncrement));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    JdbcException ex = assertThrows(JdbcException.class, query::prepare);

    assertEquals(Message.DOMA2235, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  public void testPrepare_Oracle_autoIncrement() {
    runtimeConfig.setDialect(new OracleDialect());

    AutoIncrement autoIncrement = new AutoIncrement();

    AutoMultiInsertQuery<AutoIncrement> query =
        new AutoMultiInsertQuery<>(_AutoIncrement.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Collections.singletonList(autoIncrement));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    JdbcException ex = assertThrows(JdbcException.class, query::prepare);

    assertEquals(Message.DOMA2236, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  public void testOption_default() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");

    AutoMultiInsertQuery<Emp> query = new AutoMultiInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?), (?, ?, ?, ?)",
        sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(8, parameters.size());

    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertNull(parameters.get(2).getWrapper().get());
    assertEquals(1, parameters.get(3).getWrapper().get());

    assertEquals(20, parameters.get(4).getWrapper().get());
    assertEquals("bbb", parameters.get(5).getWrapper().get());
    assertNull(parameters.get(6).getWrapper().get());
    assertEquals(1, parameters.get(7).getWrapper().get());
  }

  @Test
  public void testOption_include() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));

    AutoMultiInsertQuery<Emp> query = new AutoMultiInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Collections.singletonList(emp));
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
  public void testOption_exclude() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal(200));

    AutoMultiInsertQuery<Emp> query = new AutoMultiInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
    query.setConfig(runtimeConfig);
    query.setEntities(Collections.singletonList(emp));
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
