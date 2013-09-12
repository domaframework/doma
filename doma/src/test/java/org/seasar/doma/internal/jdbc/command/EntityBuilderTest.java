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

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;
import example.entity._Emp;

/**
 * @author taedium
 * 
 */
public class EntityBuilderTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testBuild() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("id"));
        metaData.columns.add(new ColumnMetaData("name"));
        metaData.columns.add(new ColumnMetaData("salary"));
        metaData.columns.add(new ColumnMetaData("version"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData(1, "aaa", new BigDecimal(10), 100));
        resultSet.next();

        _Emp entityType = _Emp.getSingletonInternal();
        EntityBuilder<Emp> builder = new EntityBuilder<Emp>(
                new MySelectQuery(), entityType, false);
        Emp emp = builder.build(resultSet);

        assertEquals(new Integer(1), emp.getId());
        assertEquals("aaa", emp.getName());
        assertEquals(new BigDecimal(10), emp.getSalary());
        assertEquals(new Integer(100), emp.getVersion());
    }

    protected class MySelectQuery implements SelectQuery {

        @Override
        public SelectOptions getOptions() {
            return SelectOptions.get();
        }

        @Override
        public Config getConfig() {
            return runtimeConfig;
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
            return null;
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
        public void prepare() {

        }

        @Override
        public void complete() {
        }

    }
}
