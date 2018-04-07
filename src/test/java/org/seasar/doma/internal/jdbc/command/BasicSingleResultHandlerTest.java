package org.seasar.doma.internal.jdbc.command;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlTemplateSelectQuery;

public class BasicSingleResultHandlerTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @Override
  protected void setUp() throws Exception {
    method = getClass().getMethod(getName());
  }

  public void testHandle() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("aaa"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarSingleResultHandler<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.StringWrapper(), false));
    var result = handler.handle(resultSet, query, (__) -> {}).get();
    assertEquals("aaa", result);
  }

  public void testHandle_NonUniqueResultException() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("aaa"));
    resultSet.rows.add(new RowData("bbb"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarSingleResultHandler<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.StringWrapper(), false));
    try {
      handler.handle(resultSet, query, (__) -> {});
      fail();
    } catch (NonUniqueResultException ignore) {
    }
  }

  public void testHandle_NonSingleColumnException() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    metaData.columns.add(new ColumnMetaData("y"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("aaa", "bbb"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarSingleResultHandler<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.StringWrapper(), false));
    try {
      handler.handle(resultSet, query, (__) -> {});
      fail();
    } catch (NonSingleColumnException ignore) {
    }
  }
}
