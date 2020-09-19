package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;

public class BatchDeleteCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testExecute() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(1);
    emp1.setName("hoge");
    emp1.setVersion(10);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("foo");
    emp2.setVersion(20);

    AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(method.getName()));
    query.setConfig(runtimeConfig);
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();
    int[] rows = new BatchDeleteCommand(query).execute();
    query.complete();

    assertEquals(2, rows.length);
    String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
    assertEquals("delete from EMP where ID = ? and VERSION = ?", sql);
  }
}
