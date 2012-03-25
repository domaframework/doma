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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.BatchInsertQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.BatchUniqueConstraintException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public class BatchInsertCommand extends BatchModifyCommand<BatchInsertQuery> {

    public BatchInsertCommand(BatchInsertQuery query) {
        super(query);
    }

    @Override
    protected int[] executeInternal(PreparedStatement preparedStatement,
            List<PreparedSql> sqls) throws SQLException {
        if (query.isBatchSupported()) {
            return executeBatch(preparedStatement, sqls);
        }
        int sqlSize = sqls.size();
        int[] updatedRows = new int[sqlSize];
        for (int i = 0; i < sqlSize; i++) {
            PreparedSql sql = sqls.get(i);
            log(sql);
            bindParameters(preparedStatement, sql);
            updatedRows[i] = executeUpdate(preparedStatement, sql);
            query.generateId(preparedStatement, i);
        }
        return updatedRows;
    }

    protected int executeUpdate(PreparedStatement preparedStatement,
            PreparedSql sql) throws SQLException {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Dialect dialect = query.getConfig().getDialect();
            if (dialect.isUniqueConstraintViolated(e)) {
                throw new BatchUniqueConstraintException(query.getConfig()
                        .getExceptionSqlLogType(), sql, e);
            }
            throw e;
        }
    }

}
