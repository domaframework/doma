package org.seasar.doma.jdbc.dialect;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.internal.jdbc.dialect.PostgresForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.PostgresPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcType;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;


/**
 * @author taedium
 * 
 */
public class PostgresDialect extends StandardDialect {

    protected static final String UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE = "23505";

    @Override
    public String getName() {
        return "postgres";
    }

    @Override
    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode,
            SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        PostgresForUpdateTransformer transformer = new PostgresForUpdateTransformer(
                forUpdateType, waitSeconds, aliases);
        return transformer.transform(sqlNode);
    }

    @Override
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaIllegalArgumentException("sqlException", sqlException);
        }
        String state = getSQLState(sqlException);
        return UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE.equals(state);
    }

    @Override
    public PreparedSql getIdentitySelectSql(String qualifiedTableName,
            String columnName) {
        if (qualifiedTableName == null) {
            throw new DomaIllegalArgumentException("qualifiedTableName",
                    qualifiedTableName);
        }
        if (columnName == null) {
            throw new DomaIllegalArgumentException("columnName", columnName);
        }
        StringBuilder buf = new StringBuilder(64);
        buf.append("select currval('");
        buf.append(qualifiedTableName);
        buf.append('_').append(columnName);
        buf.append("_seq')");
        String rawSql = buf.toString();
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaIllegalArgumentException("qualifiedSequenceName",
                    qualifiedSequenceName);
        }
        String rawSql = "select nextval('" + qualifiedSequenceName + "')";
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public boolean supportsSelectForUpdate(SelectForUpdateType type,
            boolean withTargets) {
        return type == SelectForUpdateType.NORMAL;
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
            callableStatement.registerOutParameter(index, Types.OTHER);
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
