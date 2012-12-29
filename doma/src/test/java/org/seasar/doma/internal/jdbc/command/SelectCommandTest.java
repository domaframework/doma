/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;

import example.entity.Emp;
import example.entity._Emp;

/**
 * @author taedium
 * 
 */
public class SelectCommandTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testExecute_singleResult() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("id"));
        metaData.columns.add(new ColumnMetaData("name"));
        metaData.columns.add(new ColumnMetaData("salary"));
        metaData.columns.add(new ColumnMetaData("version"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData(1, "hoge", new BigDecimal(10000), 100));
        runtimeConfig.dataSource.connection = new MockConnection(
                new MockPreparedStatement(resultSet));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.addParameter("name", String.class, "hoge");
        query.addParameter("salary", BigDecimal.class, new BigDecimal(10000));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        SelectCommand<Emp> command = new SelectCommand<Emp>(query,
                new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal()));
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
        runtimeConfig.dataSource.connection = new MockConnection(
                new MockPreparedStatement(resultSet));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.addParameter("salary", BigDecimal.class, new BigDecimal(5000));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        SelectCommand<List<Emp>> command = new SelectCommand<List<Emp>>(query,
                new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
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
}
