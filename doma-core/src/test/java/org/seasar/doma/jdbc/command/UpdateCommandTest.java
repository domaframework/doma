package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class UpdateCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testExecute() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setVersion(10);
    emp.originalStates = new Emp();

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new UpdateCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals("hoge", bindValues.get(0).getValue());
    assertEquals(10, bindValues.get(1).getValue());
    assertEquals(1, bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());
  }

  @Test
  public void testExecute_throwsOptimisticLockException() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    UpdateCommand command = new UpdateCommand(query);
    try {
      command.execute();
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  @Test
  public void testExecute_suppressesOptimisticLockException() throws Exception {
    Emp emp = new Emp();
    emp.setId(10);
    emp.setName("aaa");
    emp.setVersion(100);

    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setOptimisticLockExceptionSuppressed(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    new UpdateCommand(query).execute();
    query.complete();
  }

  @Test
  public void testExecute_OriginalStates() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setVersion(10);

    Emp states = new Emp();
    states.setId(1);
    states.setName("foo");
    states.setVersion(10);

    emp.originalStates = states;

    AutoUpdateQuery<Emp> query = new AutoUpdateQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntity(emp);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int rows = new UpdateCommand(query).execute();
    query.complete();

    assertEquals(1, rows);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql);

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    assertEquals(4, bindValues.size());
    assertEquals("hoge", bindValues.get(0).getValue());
    assertEquals(10, bindValues.get(1).getValue());
    assertEquals(1, bindValues.get(2).getValue());
    assertEquals(10, bindValues.get(3).getValue());

    Emp updatedStates = emp.originalStates;
    assertEquals(1, updatedStates.getId());
    assertEquals("hoge", updatedStates.getName());
    assertNull(updatedStates.getSalary());
    assertEquals(11, updatedStates.getVersion());
  }
}
