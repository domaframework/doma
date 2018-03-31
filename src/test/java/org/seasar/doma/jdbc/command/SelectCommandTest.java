package org.seasar.doma.jdbc.command;

import example.entity._Emp;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Collectors;
import junit.framework.TestCase;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;

public class SelectCommandTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testExecute_singleResult() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    var query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.addParameter("name", String.class, "hoge");
    query.addParameter("salary", BigDecimal.class, new BigDecimal(10000));
    query.setMethod(getClass().getMethod(getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var command =
        new SelectCommand<>(query, new EntitySingleResultHandler<>(_Emp.getSingletonInternal()));
    var entity = command.execute();
    query.complete();

    assertNotNull(entity);
    assertEquals(Integer.valueOf(1), entity.getId());
    assertEquals("hoge", entity.getName());
    assertEquals(new BigDecimal(10000), entity.getSalary());
    assertEquals(Integer.valueOf(100), entity.getVersion());

    var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    var bindValue = bindValues.get(0);
    assertEquals("hoge", bindValue.getValue());
    assertEquals(1, bindValue.getIndex());
    bindValue = bindValues.get(1);
    assertEquals(new BigDecimal(10000), bindValue.getValue());
    assertEquals(2, bindValue.getIndex());
  }

  public void testExecute_resultList() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
    resultSet.rows.add(new RowData(2, "foo", new BigDecimal(20000), 200));
    resultSet.rows.add(new RowData(3, "bar", new BigDecimal(30000), 300));
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    var query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.addParameter("salary", BigDecimal.class, new BigDecimal(5000));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(getName()));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var command =
        new SelectCommand<>(query, new EntityResultListHandler<>(_Emp.getSingletonInternal()));
    var entities = command.execute();
    query.complete();

    assertNotNull(entities);
    assertEquals(3, entities.size());
    var entity = entities.get(0);
    assertEquals(Integer.valueOf(1), entity.getId());
    assertEquals("hoge", entity.getName());
    assertEquals(new BigDecimal(10000), entity.getSalary());
    assertEquals(Integer.valueOf(100), entity.getVersion());
    entity = entities.get(1);
    assertEquals(Integer.valueOf(2), entity.getId());
    assertEquals("foo", entity.getName());
    assertEquals(new BigDecimal(20000), entity.getSalary());
    assertEquals(Integer.valueOf(200), entity.getVersion());
    entity = entities.get(2);
    assertEquals(Integer.valueOf(3), entity.getId());
    assertEquals("bar", entity.getName());
    assertEquals(new BigDecimal(30000), entity.getSalary());
    assertEquals(Integer.valueOf(300), entity.getVersion());

    var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    var bindValue = bindValues.get(0);
    bindValue = bindValues.get(0);
    assertEquals(new BigDecimal(5000), bindValue.getValue());
    assertEquals(1, bindValue.getIndex());
  }

  public void testExecute_NoResultException() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    var resultSet = new MockResultSet(metaData);
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    var query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.addParameter("name", String.class, "hoge");
    query.addParameter("salary", BigDecimal.class, new BigDecimal(10000));
    query.setMethod(getClass().getMethod(getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setResultEnsured(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    var command =
        new SelectCommand<>(query, new EntitySingleResultHandler<>(_Emp.getSingletonInternal()));
    try {
      command.execute();
      fail();
    } catch (Exception expected) {
    }
  }

  public void testExecute_resultStream() throws Exception {
    var metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    var resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
    resultSet.rows.add(new RowData(2, "foo", new BigDecimal(20000), 200));
    resultSet.rows.add(new RowData(3, "bar", new BigDecimal(30000), 300));
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    var query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), getName()));
    query.addParameter("salary", BigDecimal.class, new BigDecimal(5000));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(getName()));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setFetchType(FetchType.LAZY);
    query.setResultStream(true);
    query.prepare();

    var command =
        new SelectCommand<>(
            query, new EntityStreamHandler<>(_Emp.getSingletonInternal(), Function.identity()));
    try (var stream = command.execute()) {
      query.complete();

      var entities = stream.collect(Collectors.toList());
      assertNotNull(entities);
      assertEquals(3, entities.size());
      var entity = entities.get(0);
      assertEquals(Integer.valueOf(1), entity.getId());
      assertEquals("hoge", entity.getName());
      assertEquals(new BigDecimal(10000), entity.getSalary());
      assertEquals(Integer.valueOf(100), entity.getVersion());
      entity = entities.get(1);
      assertEquals(Integer.valueOf(2), entity.getId());
      assertEquals("foo", entity.getName());
      assertEquals(new BigDecimal(20000), entity.getSalary());
      assertEquals(Integer.valueOf(200), entity.getVersion());
      entity = entities.get(2);
      assertEquals(Integer.valueOf(3), entity.getId());
      assertEquals("bar", entity.getName());
      assertEquals(new BigDecimal(30000), entity.getSalary());
      assertEquals(Integer.valueOf(300), entity.getVersion());

      var bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
      var bindValue = bindValues.get(0);
      bindValue = bindValues.get(0);
      assertEquals(new BigDecimal(5000), bindValue.getValue());
      assertEquals(1, bindValue.getIndex());

      assertFalse(runtimeConfig.dataSource.connection.preparedStatement.resultSet.isClosed());
      assertFalse(runtimeConfig.dataSource.connection.preparedStatement.isClosed());
      assertFalse(runtimeConfig.dataSource.connection.isClosed());
    }

    assertTrue(runtimeConfig.dataSource.connection.preparedStatement.resultSet.isClosed());
    assertTrue(runtimeConfig.dataSource.connection.preparedStatement.isClosed());
    assertTrue(runtimeConfig.dataSource.connection.isClosed());
  }
}
