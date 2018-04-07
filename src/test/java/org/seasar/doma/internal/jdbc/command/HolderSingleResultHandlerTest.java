package org.seasar.doma.internal.jdbc.command;

import example.holder._PhoneNumber;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlTemplateSelectQuery;

public class HolderSingleResultHandlerTest extends TestCase {

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
    resultSet.rows.add(new RowData("01-2345-6789"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarSingleResultHandler<>(() -> _PhoneNumber.getSingletonInternal().createScalar());
    var result = handler.handle(resultSet, query, (__) -> {}).get();
    assertEquals("01-2345-6789", result.getValue());
  }

  public void testHandle_NonUniqueResultException() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("phoneNumber"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("01-2345-6789"));
    resultSet.rows.add(new RowData("02-2345-6789"));

    var query = new SqlTemplateSelectQuery();
    query.setConfig(runtimeConfig);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var handler =
        new ScalarSingleResultHandler<>(() -> _PhoneNumber.getSingletonInternal().createScalar());
    try {
      handler.handle(resultSet, query, (__) -> {});
      fail();
    } catch (NonUniqueResultException expected) {
    }
  }
}
