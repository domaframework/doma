package org.seasar.doma.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;

public class DeleteCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(10);

    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new DeleteCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(2, bindValues.size());
    assertEquals(Integer.valueOf(1), bindValues.get(0).getValue());
    assertEquals(Integer.valueOf(10), bindValues.get(1).getValue());
  }

  public void testExecute_throwsOptimisticLockException() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    MockPreparedStatement ps = new MockPreparedStatement();
    ps.updatedRows = 0;
    runtimeConfig.dataSource.connection = new MockConnection(ps);
    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    DeleteCommand command = new DeleteCommand(query);
    try {
      command.execute();
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  public void testExecute_suppressesOptimisticLockException() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setOptimisticLockExceptionSuppressed(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    new DeleteCommand(query).execute();
    query.complete();
  }
}
