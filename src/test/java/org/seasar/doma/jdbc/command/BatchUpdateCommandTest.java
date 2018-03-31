package org.seasar.doma.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import java.util.Arrays;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;

/** @author taedium */
public class BatchUpdateCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(1);
    emp1.setName("hoge");
    emp1.setVersion(10);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("foo");
    emp2.setVersion(20);

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int[] rows = new BatchUpdateCommand(query).execute();
    query.complete();

    assertEquals(2, rows.length);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals(
        "update EMP set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql);
    assertEquals(Integer.valueOf(11), emp1.getVersion());
    assertEquals(Integer.valueOf(21), emp2.getVersion());
  }

  public void testExecute_throwsOptimisticLockException() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setVersion(10);

    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    BatchUpdateCommand command = new BatchUpdateCommand(query);
    try {
      command.execute();
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  public void testExecute_suppressesOptimisticLockException() throws Exception {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("hoge");
    emp.setVersion(10);

    runtimeConfig.dataSource.connection.preparedStatement.updatedRows = 0;

    AutoBatchUpdateQuery<Emp> query = new AutoBatchUpdateQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp));
    query.setOptimisticLockExceptionSuppressed(true);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    new BatchUpdateCommand(query).execute();
    query.complete();
  }
}
