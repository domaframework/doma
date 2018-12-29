package org.seasar.doma.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import java.util.Arrays;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;

public class BatchInsertCommandTest extends TestCase {

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

    AutoBatchInsertQuery<Emp> query = new AutoBatchInsertQuery<Emp>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int[] rows = new BatchInsertCommand(query).execute();
    query.complete();

    assertEquals(2, rows.length);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql);
  }
}
