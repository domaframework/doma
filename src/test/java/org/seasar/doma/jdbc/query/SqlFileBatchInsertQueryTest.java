package org.seasar.doma.jdbc.query;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

/** @author taedium */
public class SqlFileBatchInsertQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setName("bbb");
    emp2.setVersion(200);

    SqlFileBatchInsertQuery<Emp> query = new SqlFileBatchInsertQuery<Emp>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BatchInsertQuery batchInsertQuery = query;
    assertEquals(2, batchInsertQuery.getSqls().size());
  }

  public void testOption_default() throws Exception {
    Emp emp1 = new Emp();
    emp1.setId(10);
    emp1.setName("aaa");
    emp1.setVersion(100);

    Emp emp2 = new Emp();
    emp2.setId(20);
    emp2.setSalary(new BigDecimal(2000));
    emp2.setVersion(200);

    SqlFileBatchInsertQuery<Emp> query = new SqlFileBatchInsertQuery<Emp>(Emp.class);
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.setParameterName("e");
    query.setElements(Arrays.asList(emp1, emp2));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    PreparedSql sql = query.getSqls().get(0);
    assertEquals("insert into emp (id, name, salary) values (?, ?, ?)", sql.getRawSql());
    List<InParameter<?>> parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(new Integer(10), parameters.get(0).getWrapper().get());
    assertEquals("aaa", parameters.get(1).getWrapper().get());
    assertNull(parameters.get(2).getWrapper().get());

    sql = query.getSqls().get(1);
    assertEquals("insert into emp (id, name, salary) values (?, ?, ?)", sql.getRawSql());
    parameters = sql.getParameters();
    assertEquals(3, parameters.size());
    assertEquals(new Integer(20), parameters.get(0).getWrapper().get());
    assertNull(parameters.get(1).getWrapper().get());
    assertEquals(new BigDecimal(2000), parameters.get(2).getWrapper().get());
  }
}
