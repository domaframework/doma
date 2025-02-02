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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import example.entity._Emp;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnAdditionalInfoException;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SelectQuery;

public class MappingSupportTest {

  private final MockConfig mockConfig = new MockConfig();

  @Test
  void testCreateIndexMap() throws SQLException {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("salary"));
    metaData.columns.add(new ColumnMetaData("version"));

    _Emp emp = _Emp.getSingletonInternal();

    Query query = new MySelectQuery(mockConfig);
    UnknownColumnHandler mockUnknownColumnHandler = mockConfig.getUnknownColumnHandler();
    DuplicateColumnHandler mockDuplicateColumnHandler = mockConfig.getDuplicateColumnHandler();
    Map<String, MappingSupport.PropType> columnNameMap = new HashMap<>();
    columnNameMap.put("version", new MappingSupport.PropType(emp, emp.version, ""));
    columnNameMap.put("id", new MappingSupport.PropType(emp, emp.id, ""));
    columnNameMap.put("name", new MappingSupport.PropType(emp, emp.name, ""));
    columnNameMap.put("salary", new MappingSupport.PropType(emp, emp.salary, ""));

    MappingSupport mappingSupport =
        new MappingSupport(emp, query, false, mockUnknownColumnHandler, mockDuplicateColumnHandler);

    Map<Integer, MappingSupport.PropType> indexMap =
        mappingSupport.createIndexMap(metaData, columnNameMap);
    assertEquals(4, indexMap.size());
    assertEquals("id", indexMap.get(1).name());
    assertEquals("name", indexMap.get(2).name());
    assertEquals("salary", indexMap.get(3).name());
    assertEquals("version", indexMap.get(4).name());
  }

  @Test
  void testCreateIndexMap_unknownColumn() {
    MockResultSetMetaData metaData = new MockResultSetMetaData();
    metaData.columns.add(new ColumnMetaData("id"));
    metaData.columns.add(new ColumnMetaData("name"));
    metaData.columns.add(new ColumnMetaData("unknown"));
    metaData.columns.add(new ColumnMetaData("version"));

    _Emp emp = _Emp.getSingletonInternal();

    Query query = new MySelectQuery(mockConfig);
    UnknownColumnHandler mockUnknownColumnHandler = mockConfig.getUnknownColumnHandler();
    DuplicateColumnHandler mockDuplicateColumnHandler = mockConfig.getDuplicateColumnHandler();
    Map<String, MappingSupport.PropType> columnNameMap = new HashMap<>();
    columnNameMap.put("version", new MappingSupport.PropType(emp, emp.version, ""));
    columnNameMap.put("id", new MappingSupport.PropType(emp, emp.id, ""));
    columnNameMap.put("name", new MappingSupport.PropType(emp, emp.name, ""));
    columnNameMap.put("salary", new MappingSupport.PropType(emp, emp.salary, ""));

    MappingSupport mappingSupport =
        new MappingSupport(emp, query, false, mockUnknownColumnHandler, mockDuplicateColumnHandler);

    UnknownColumnAdditionalInfoException ex =
        assertThrows(
            UnknownColumnAdditionalInfoException.class,
            () -> mappingSupport.createIndexMap(metaData, columnNameMap));

    String expected =
        """
        ------------------------------------------------------
        Lowercase Column Name -> Property Name (Entity Name)
        ------------------------------------------------------
        id -> id (Emp)
        name -> name (Emp)
        salary -> salary (Emp)
        version -> version (Emp)
        ------------------------------------------------------""";

    assertEquals(expected, ex.getAdditionalInfo());
    System.out.println(ex.getMessage());
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
}
