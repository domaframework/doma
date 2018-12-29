package org.seasar.doma.internal.jdbc.command;

import example.entity.Emp;
import example.entity._Emp;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;

public class EntitySingleResultHandlerTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testHandle() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa"));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(getName()));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    EntitySingleResultHandler<Emp> handler =
        new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal());
    Emp emp = handler.handle(resultSet, query, (i, next) -> {}).get();
    assertEquals(new Integer(1), emp.getId());
    assertEquals("aaa", emp.getName());
  }

  public void testHandle_NonUniqueResultException() throws Exception {
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
    query.setMethod(getClass().getMethod(getName()));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    EntitySingleResultHandler<Emp> handler =
        new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal());
    try {
      handler.handle(resultSet, query, (i, next) -> {});
      fail();
    } catch (NonUniqueResultException expected) {
    }
  }
}
