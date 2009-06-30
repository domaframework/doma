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
package org.seasar.doma.jdbc.dialect;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.internal.jdbc.dialect.OracleForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.OraclePagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcType;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;


/**
 * @author taedium
 * 
 */
public class OracleDialect extends StandardDialect {

    protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = 1;

    @Override
    public String getName() {
        return "oracle";
    }

    @Override
    public boolean supportsBatchUpdateResults() {
        return false;
    }

    @Override
    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode,
            SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        OracleForUpdateTransformer transformer = new OracleForUpdateTransformer(
                forUpdateType, waitSeconds, aliases);
        return transformer.transform(sqlNode);
    }

    @Override
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
        OraclePagingTransformer transformer = new OraclePagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaIllegalArgumentException("sqlException", sqlException);
        }
        int code = getErrorCode(sqlException);
        return UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE == code;
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaIllegalArgumentException("qualifiedSequenceName",
                    qualifiedSequenceName);
        }
        String rawSql = "select " + qualifiedSequenceName
                + ".nextval from dual";
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
    }

    @Override
    public boolean supportsIdentity() {
        return false;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public boolean supportsSelectForUpdate(SelectForUpdateType type,
            boolean withTargets) {
        return true;
    }

    @Override
    public boolean supportsResultSetReturningAsOutParameter() {
        return true;
    }

    @Override
    public JdbcType<ResultSet> getResultSetType() {
        return new ResultSetType();
    }

    public static class ResultSetType implements JdbcType<ResultSet> {

        protected static int CURSOR = -10;

        @Override
        public ResultSet getValue(ResultSet resultSet, int index)
                throws SQLException {
            throw new DomaUnsupportedOperationException(getClass().getName(),
                    "getValue");
        }

        @Override
        public void setValue(PreparedStatement preparedStatement, int index,
                ResultSet value) throws SQLException {
            throw new DomaUnsupportedOperationException(getClass().getName(),
                    "setValue");
        }

        @Override
        public void registerOutParameter(CallableStatement callableStatement,
                int index) throws SQLException {
            if (callableStatement == null) {
                throw new DomaIllegalArgumentException("callableStatement",
                        callableStatement);
            }
            if (index < 1) {
                throw new DomaIllegalArgumentException("index", index);
            }
            callableStatement.registerOutParameter(index, CURSOR);
        }

        @Override
        public ResultSet getValue(CallableStatement callableStatement, int index)
                throws SQLException {
            if (callableStatement == null) {
                throw new DomaIllegalArgumentException("callableStatement",
                        callableStatement);
            }
            if (index < 1) {
                throw new DomaIllegalArgumentException("index", index);
            }
            Object resultSet = callableStatement.getObject(index);
            return ResultSet.class.cast(resultSet);
        }
    }

}
