/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.seasar.doma.domain.StringWrapper;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/**
 * @author taedium
 * 
 */
public class DomainIterationHandlerTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testHandle() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("name"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("aaa"));
        resultSet.rows.add(new RowData("bbb"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        DomainIterationHandler<String, StringWrapper> handler = new DomainIterationHandler<String, StringWrapper>(
                StringWrapper.class,
                new IterationCallback<String, StringWrapper>() {

                    private String result = "";

                    @Override
                    public String iterate(StringWrapper target,
                            IterationContext iterationContext) {
                        result += target.get();
                        return result;
                    }
                });
        String result = handler.handle(resultSet, query);
        assertEquals("aaabbb", result);
    }

    public void testHandle_exits() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("name"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("aaa"));
        resultSet.rows.add(new RowData("bbb"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        DomainIterationHandler<String, StringWrapper> handler = new DomainIterationHandler<String, StringWrapper>(
                StringWrapper.class,
                new IterationCallback<String, StringWrapper>() {

                    private String result = "";

                    @Override
                    public String iterate(StringWrapper target,
                            IterationContext iterationContext) {
                        result += target.get();
                        iterationContext.exit();
                        return result;
                    }
                });
        String result = handler.handle(resultSet, query);
        assertEquals("aaa", result);
    }
}
