package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import example.entity._Emp;
import java.math.BigDecimal;
import java.util.Arrays;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoBatchInsertQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");

    var query = new AutoBatchInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BatchInsertQuery batchInsertQuery = query;
    assertTrue(batchInsertQuery.isBatchSupported());
    assertEquals(2, batchInsertQuery.getSqls().size());
  }

  public void testOption_default() throws Exception {
    var emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");

    var emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(10);

    var query = new AutoBatchInsertQuery<>(_Emp.getSingletonInternal());
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setEntities(Arrays.asList(emp1, emp2));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var sql = query.getSqls().get(0);
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql.getRawSql());
    var parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals(10, parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertNull(parameters.get(2).getWrapper().get());
    assertEquals(1, parameters.get(3).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (?, ?, ?, ?)", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(4, parameters.size());
    assertEquals(20, parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(2).getWrapper().get());
    assertEquals(10, parameters.get(3).getWrapper().get());
  }
}
