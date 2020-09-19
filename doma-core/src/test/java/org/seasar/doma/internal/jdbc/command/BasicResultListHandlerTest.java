package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;

public class BasicResultListHandlerTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testHandle() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("aaa"));
    resultSet.rows.add(new RowData("bbb"));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BasicResultListHandler<String> handler =
        new BasicResultListHandler<>(() -> new org.seasar.doma.wrapper.StringWrapper());
    List<String> results = handler.handle(resultSet, query, (i, next) -> {}).get();
    assertEquals(2, results.size());
    assertEquals("aaa", results.get(0));
    assertEquals("bbb", results.get(1));
  }

  @Test
  public void testHandle_NonSingleColumnException() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("x"));
    metaData.columns.add(new ColumnMetaData("y"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData("aaa", "bbb"));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(method);
    query.setResultEnsured(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    BasicResultListHandler<String> handler =
        new BasicResultListHandler<>(() -> new org.seasar.doma.wrapper.StringWrapper());
    try {
      handler.handle(resultSet, query, (i, next) -> {});
      fail();
    } catch (NonSingleColumnException expected) {
    }
  }
}
