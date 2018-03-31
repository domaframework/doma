package org.seasar.doma.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoInsertQuery;

public class InsertCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute() throws Exception {
    var emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(10);

    var query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new InsertCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    var sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);

    var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals(new String("hoge"), bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());
  }

  public void testExecute_defaultVersion() throws Exception {
    var emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(null);

    var query = new AutoInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new InsertCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    var sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);

    var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals(new String("hoge"), bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(1, bindValues.get(3).getValue());
  }
}
