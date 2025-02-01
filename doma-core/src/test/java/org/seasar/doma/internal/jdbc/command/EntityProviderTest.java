/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DuplicateColumnException;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.ThrowingDuplicateColumnHandler;
import org.seasar.doma.jdbc.UnknownColumnException;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityProviderTest {

  @Test
  public void testGetEntity() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa", new BigDecimal(10), 100));
    resultSet.next();

    _Emp entityType = _Emp.getSingletonInternal();
    EntityProvider<Emp> provider =
        new EntityProvider<>(entityType, new MySelectQuery(new MockConfig()), false);
    Emp emp = provider.get(resultSet);

    assertEquals(1, emp.getId());
    assertEquals("aaa", emp.getName());
    assertEquals(new BigDecimal(10), emp.getSalary());
    assertEquals(100, emp.getVersion());
  }

  @Test
  public void testGetEntity_UnknownColumnException() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    metaData.columns.add(new ColumnMetaData("unknown"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa", new BigDecimal(10), 100, "bbb"));
    resultSet.next();

    _Emp entityType = _Emp.getSingletonInternal();
    EntityProvider<Emp> provider =
        new EntityProvider<>(entityType, new MySelectQuery(new MockConfig()), false);
    try {
      provider.get(resultSet);
      fail();
    } catch (UnknownColumnException expected) {
    }
  }

  @Test
  public void testGetEntity_EmptyUnknownColumnHandler() throws Exception {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));
    metaData.columns.add(new ColumnMetaData("unknown"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa", new BigDecimal(10), 100, "bbb"));
    resultSet.next();

    _Emp entityType = _Emp.getSingletonInternal();
    EntityProvider<Emp> provider =
        new EntityProvider<>(
            entityType, new MySelectQuery(new EmptyUnknownColumnHandlerConfig()), false);
    Emp emp = provider.get(resultSet);

    assertEquals(1, emp.getId());
    assertEquals("aaa", emp.getName());
    assertEquals(new BigDecimal(10), emp.getSalary());
    assertEquals(100, emp.getVersion());
  }

  @Test
  public void testCreateIndexMap_WithDuplicateColumnName() throws SQLException {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("name")); // Duplicate column name
    metaData.columns.add(new ColumnMetaData("version"));
    MockResultSet resultSet = new MockResultSet(metaData);
    resultSet.rows.add(new RowData(1, "aaa", "bbb", 100));
    resultSet.next();

    _Emp entityType = _Emp.getSingletonInternal();
    EntityProvider<Emp> provider =
        new EntityProvider<>(entityType, new MySelectQuery(new MockConfig()), false);

    Emp emp = provider.get(resultSet);

    assertEquals(1, emp.getId());
    assertEquals("bbb", emp.getName());
    assertEquals(100, emp.getVersion());
  }

  @Test
  public void testCreateIndexMap_DuplicateColumnHandler() throws SQLException {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("name")); // Duplicate column name
    metaData.columns.add(new ColumnMetaData("version"));
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet(metaData);

    _Emp entityType = _Emp.getSingletonInternal();
    EntityProvider<Emp> provider =
        new EntityProvider<>(
            entityType, new MySelectQuery(new SetDuplicateColumnHandlerConfig()), false);

    assertThrows(DuplicateColumnException.class, () -> provider.get(resultSet));
  }

  protected static class MySelectQuery implements SelectQuery {

    private final Config config;

    MySelectQuery(Config config) {
      this.config = config;
    }

    @Override
    public SelectOptions getOptions() {
      return SelectOptions.get();
    }

    @Override
    public Config getConfig() {
      return config;
    }

    @Override
    public String getClassName() {
      return null;
    }

    @Override
    public String getMethodName() {
      return null;
    }

    @Override
    public PreparedSql getSql() {
      return new PreparedSql(
          SqlKind.SELECT, "dummy", "dummy", "dummy", Collections.emptyList(), SqlLogType.FORMATTED);
    }

    @Override
    public boolean isResultEnsured() {
      return false;
    }

    @Override
    public boolean isResultMappingEnsured() {
      return false;
    }

    @Override
    public FetchType getFetchType() {
      return FetchType.LAZY;
    }

    @Override
    public int getFetchSize() {
      return 0;
    }

    @Override
    public int getMaxRows() {
      return 0;
    }

    @Override
    public int getQueryTimeout() {
      return 0;
    }

    @Override
    public void prepare() {}

    @Override
    public void complete() {}

    @Override
    public Method getMethod() {
      return null;
    }

    @Override
    public SqlLogType getSqlLogType() {
      return null;
    }

    @Override
    public String comment(String sql) {
      return sql;
    }

    @Override
    public boolean isResultStream() {
      return false;
    }
  }

  protected static class EmptyUnknownColumnHandler implements UnknownColumnHandler {
    @Override
    public void handle(Query query, EntityType<?> entityType, String unknownColumnName) {}
  }

  protected static class EmptyUnknownColumnHandlerConfig extends MockConfig {
    @Override
    public UnknownColumnHandler getUnknownColumnHandler() {
      return new EmptyUnknownColumnHandler();
    }
  }

  protected static class SetDuplicateColumnHandlerConfig extends MockConfig {
    @Override
    public DuplicateColumnHandler getDuplicateColumnHandler() {
      return new ThrowingDuplicateColumnHandler();
    }
  }
}
