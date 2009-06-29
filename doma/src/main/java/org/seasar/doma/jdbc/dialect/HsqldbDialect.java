package org.seasar.doma.jdbc.dialect;

import java.sql.SQLException;
import java.util.Collections;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.dialect.HsqldbPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlNode;


/**
 * @author taedium
 * 
 */
public class HsqldbDialect extends StandardDialect {

    protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = -104;

    @Override
    public String getName() {
        return "hsqldb";
    }

    @Override
    public boolean includesIdentityColumn() {
        return true;
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
        String rawSql = "call identity()";
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
        String rawSql = "select next value for "
                + qualifiedSequenceName
                + " from information_schema.system_tables where table_name = 'SYSTEM_TABLES'";
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
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
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
        HsqldbPagingTransformer transformer = new HsqldbPagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode,
            SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        return sqlNode;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }
}
