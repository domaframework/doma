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

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.ColumnMetaData;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.MockResultSetMetaData;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.NonUniqueResultException;

import example.domain.PhoneNumber;
import example.domain._PhoneNumber;

/**
 * @author taedium
 * 
 */
public class DomainSingleResultHandlerTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testHandle() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("x"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("01-2345-6789"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        DomainSingleResultHandler<PhoneNumber> handler = new DomainSingleResultHandler<PhoneNumber>(
                _PhoneNumber.getSingletonInternal());
        PhoneNumber result = handler.handle(resultSet, query);
        assertEquals("01-2345-6789", result.getValue());
    }

    public void testHandle_NonUniqueResultException() throws Exception {
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.columns.add(new ColumnMetaData("phoneNumber"));
        MockResultSet resultSet = new MockResultSet(metaData);
        resultSet.rows.add(new RowData("01-2345-6789"));
        resultSet.rows.add(new RowData("02-2345-6789"));

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        DomainSingleResultHandler<PhoneNumber> handler = new DomainSingleResultHandler<PhoneNumber>(
                _PhoneNumber.getSingletonInternal());
        try {
            handler.handle(resultSet, query);
            fail();
        } catch (NonUniqueResultException expected) {
        }
    }

    public void testHandle_NoResultException() throws Exception {
        MockResultSet resultSet = new MockResultSet();

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setResultEnsured(true);
        query.prepare();

        DomainSingleResultHandler<PhoneNumber> handler = new DomainSingleResultHandler<PhoneNumber>(
                _PhoneNumber.getSingletonInternal());
        try {
            handler.handle(resultSet, query);
            fail();
        } catch (NoResultException expected) {
        }
    }

}
