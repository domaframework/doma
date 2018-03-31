package org.seasar.doma.internal.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.*;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;

public class EntityIterationHandlerTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testHandle() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa"));
    resultSet.rows.add(new RowData(2, "bbb"));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    EntityIterationHandler<Emp, Integer> handler =
        new EntityIterationHandler<>(
            _Emp.getSingletonInternal(),
            new IterationCallback<Emp, Integer>() {

              private int count;

              @Override
              public Integer iterate(Emp target, IterationContext iterationContext) {
                count++;
                return count;
              }
            });
    Integer result = handler.handle(resultSet, query, (__) -> {}).get();
    assertEquals(Integer.valueOf(2), result);
  }

  public void testHandle_exits() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa"));
    resultSet.rows.add(new RowData(2, "bbb"));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    EntityIterationHandler<Emp, Integer> handler =
        new EntityIterationHandler<>(
            _Emp.getSingletonInternal(),
            new IterationCallback<Emp, Integer>() {

              private int count;

              @Override
              public Integer iterate(Emp target, IterationContext iterationContext) {
                iterationContext.exit();
                count++;
                return count;
              }
            });
    Integer result = handler.handle(resultSet, query, (__) -> {}).get();
    assertEquals(Integer.valueOf(1), result);
  }
}
