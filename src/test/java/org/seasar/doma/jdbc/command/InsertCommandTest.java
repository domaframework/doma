package org.seasar.doma.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoInsertQuery;

public class InsertCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(10);

    AutoInsertQuery<Emp> query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
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
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(Integer.valueOf(1), bindValues.get(0).getValue());
    assertEquals(new String("hoge"), bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(Integer.valueOf(10), bindValues.get(3).getValue());
  }

  public void testExecute_defaultVersion() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(null);

    AutoInsertQuery<Emp> query = new AutoInsertQuery<Emp>(_Emp.getSingletonInternal());
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
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals(Integer.valueOf(1), bindValues.get(0).getValue());
    assertEquals(new String("hoge"), bindValues.get(1).getValue());
    assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
    assertEquals(Integer.valueOf(1), bindValues.get(3).getValue());
  }
}
