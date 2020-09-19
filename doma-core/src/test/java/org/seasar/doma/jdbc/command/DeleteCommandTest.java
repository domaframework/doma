package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class DeleteCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testExecute() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setSalary(new BigDecimal(1000));
    emp.setVersion(10);

    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
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
    assertEquals(1, bindValues.get(0).getValue());
    assertEquals(10, bindValues.get(1).getValue());
  }

  @Test
  public void testExecute_throwsOptimisticLockException() {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    MockPreparedStatement ps = new MockPreparedStatement();
    ps.updatedRows = 0;
    runtimeConfig.dataSource.connection = new MockConnection(ps);
    AutoDeleteQuery<Emp> query = new AutoDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(method);
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

  @Test
  public void testExecute_suppressesOptimisticLockException() {
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
    query.setOptimisticLockExceptionSuppressed(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    new DeleteCommand(query).execute();
    query.complete();
  }
}
