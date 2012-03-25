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

import static org.seasar.doma.internal.Constants.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class BasicFetcher implements ResultFetcher<ResultSet, Wrapper<?>> {

    protected final Query query;

    protected boolean columnCountValidated;

    public BasicFetcher(Query query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public void fetch(ResultSet resultSet, Wrapper<?> wrapper)
            throws SQLException {
        assertNotNull(resultSet, wrapper);

        if (!columnCountValidated) {
            validateColumnCount(resultSet);
        }

        JdbcMappingVisitor jdbcMappingVisitor = query.getConfig().getDialect()
                .getJdbcMappingVisitor();
        GetValueFunction function = new GetValueFunction(resultSet, 1);
        wrapper.accept(jdbcMappingVisitor, function);
    }

    protected void validateColumnCount(ResultSet resultSet) throws SQLException {
        int columnCount = getColumnCount(resultSet);
        if (columnCount != 1) {
            Sql<?> sql = query.getSql();
            throw new NonSingleColumnException(query.getConfig()
                    .getExceptionSqlLogType(), sql);
        }
        columnCountValidated = true;
    }

    protected int getColumnCount(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        int columnCount = resultSetMeta.getColumnCount();
        if (columnCount == 2) {
            String columnName = resultSetMeta.getColumnLabel(2).toLowerCase();
            return ROWNUMBER_COLUMN_NAME.equals(columnName) ? 1 : 2;
        }
        return columnCount;
    }

}
