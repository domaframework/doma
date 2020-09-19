package org.seasar.doma.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.mock.BindValue;
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

public class SelectCommandTest {

  private final MockConfig runtimeConfig = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testExecute_singleResult() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.addParameter("name", String.class, "hoge");
    query.addParameter("salary", BigDecimal.class, new BigDecimal(10000));
    query.setMethod(getClass().getMethod(method.getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    SelectCommand<Emp> command =
        new SelectCommand<Emp>(
            query, new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal()));
    Emp entity = command.execute();
    query.complete();

    assertNotNull(entity);
    assertEquals(new Integer(1), entity.getId());
    assertEquals("hoge", entity.getName());
    assertEquals(new BigDecimal(10000), entity.getSalary());
    assertEquals(new Integer(100), entity.getVersion());

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    BindValue bindValue = bindValues.get(0);
    assertEquals("hoge", bindValue.getValue());
    assertEquals(1, bindValue.getIndex());
    bindValue = bindValues.get(1);
    assertEquals(new BigDecimal(10000), bindValue.getValue());
    assertEquals(2, bindValue.getIndex());
  }

  @Test
  public void testExecute_resultList() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
    resultSet.rows.add(new RowData(2, "foo", new BigDecimal(20000), 200));
    resultSet.rows.add(new RowData(3, "bar", new BigDecimal(30000), 300));
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.addParameter("salary", BigDecimal.class, new BigDecimal(5000));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(method.getName()));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    SelectCommand<List<Emp>> command =
        new SelectCommand<List<Emp>>(
            query, new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
    List<Emp> entities = command.execute();
    query.complete();

    assertNotNull(entities);
    assertEquals(3, entities.size());
    Emp entity = entities.get(0);
    assertEquals(new Integer(1), entity.getId());
    assertEquals("hoge", entity.getName());
    assertEquals(new BigDecimal(10000), entity.getSalary());
    assertEquals(new Integer(100), entity.getVersion());
    entity = entities.get(1);
    assertEquals(new Integer(2), entity.getId());
    assertEquals("foo", entity.getName());
    assertEquals(new BigDecimal(20000), entity.getSalary());
    assertEquals(new Integer(200), entity.getVersion());
    entity = entities.get(2);
    assertEquals(new Integer(3), entity.getId());
    assertEquals("bar", entity.getName());
    assertEquals(new BigDecimal(30000), entity.getSalary());
    assertEquals(new Integer(300), entity.getVersion());

    List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
    BindValue bindValue = bindValues.get(0);
    bindValue = bindValues.get(0);
    assertEquals(new BigDecimal(5000), bindValue.getValue());
    assertEquals(1, bindValue.getIndex());
  }

  @Test
  public void testExecute_NoResultException() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    MockResultSet resultSet = new MockResultSet(metaData);
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.addParameter("name", String.class, "hoge");
    query.addParameter("salary", BigDecimal.class, new BigDecimal(10000));
    query.setMethod(getClass().getMethod(method.getName()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setResultEnsured(true);
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    SelectCommand<Emp> command =
        new SelectCommand<Emp>(
            query, new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal()));
    try {
      command.execute();
      fail();
    } catch (Exception expected) {
    }
  }

  @Test
  public void testExecute_resultStream() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
    resultSet.rows.add(new RowData(2, "foo", new BigDecimal(20000), 200));
    resultSet.rows.add(new RowData(3, "bar", new BigDecimal(30000), 300));
    runtimeConfig.dataSource.connection = new MockConnection(new MockPreparedStatement(resultSet));

    SqlFileSelectQuery query = new SqlFileSelectQuery();
    query.setConfig(runtimeConfig);
    query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(), method.getName()));
    query.addParameter("salary", BigDecimal.class, new BigDecimal(5000));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getMethod(method.getName()));
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.setFetchType(FetchType.LAZY);
    query.setResultStream(true);
    query.prepare();

    SelectCommand<Stream<Emp>> command =
        new SelectCommand<Stream<Emp>>(
            query,
            new EntityStreamHandler<Emp, Stream<Emp>>(
                _Emp.getSingletonInternal(), Function.identity()));
    try (Stream<Emp> stream = command.execute()) {
      query.complete();

      List<Emp> entities = stream.collect(Collectors.toList());
      assertNotNull(entities);
      assertEquals(3, entities.size());
      Emp entity = entities.get(0);
      assertEquals(new Integer(1), entity.getId());
      assertEquals("hoge", entity.getName());
      assertEquals(new BigDecimal(10000), entity.getSalary());
      assertEquals(new Integer(100), entity.getVersion());
      entity = entities.get(1);
      assertEquals(new Integer(2), entity.getId());
      assertEquals("foo", entity.getName());
      assertEquals(new BigDecimal(20000), entity.getSalary());
      assertEquals(new Integer(200), entity.getVersion());
      entity = entities.get(2);
      assertEquals(new Integer(3), entity.getId());
      assertEquals("bar", entity.getName());
      assertEquals(new BigDecimal(30000), entity.getSalary());
      assertEquals(new Integer(300), entity.getVersion());

      List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
      BindValue bindValue = bindValues.get(0);
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

  @Test
  public void getGetQuery() {
    SqlFileSelectQuery query = new SqlFileSelectQuery();
    SelectCommand<List<Emp>> command =
        new SelectCommand<>(query, new EntityResultListHandler<>(_Emp.getSingletonInternal()));
    assertEquals(query, command.getQuery());
  }
}
