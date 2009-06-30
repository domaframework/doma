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

import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.DomainSingleResultHandler;
import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.sql.SqlFiles;
import org.seasar.doma.jdbc.NonUniqueResultException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainSingleResultHandlerTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testHandle() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("x"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("aaa"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFiles
                .buildPath(getClass().getName(), getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        DomainSingleResultHandler<StringDomain> handler = new DomainSingleResultHandler<StringDomain>(
                StringDomain.class);
        StringDomain domain = handler.handle(resultSet, query);
        assertEquals(domain, new StringDomain("aaa"));
    }

    public void testHandle_NonUniqueResultException() throws Exception {
        MockResultSet resultSet = new MockResultSet();
        resultSet.rows.add(new RowData("aaa"));
        resultSet.rows.add(new RowData("bbb"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFiles
                .buildPath(getClass().getName(), getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        DomainSingleResultHandler<StringDomain> handler = new DomainSingleResultHandler<StringDomain>(
                StringDomain.class);
        try {
            handler.handle(resultSet, query);
            fail();
        } catch (NonUniqueResultException ignore) {
        }
    }

}
