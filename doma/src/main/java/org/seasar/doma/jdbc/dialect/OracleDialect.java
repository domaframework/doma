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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.AbstractBooleanDomain;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.BigIntegerDomain;
import org.seasar.doma.domain.BytesDomain;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DoubleDomain;
import org.seasar.doma.domain.FloatDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.LongDomain;
import org.seasar.doma.domain.ShortDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.JdbcUtil;
import org.seasar.doma.internal.jdbc.dialect.OracleForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.OraclePagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;

/**
 * @author taedium
 * 
 */
public class OracleDialect extends StandardDialect {

    protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = 1;

    protected static final JdbcType<ResultSet> RESULT_SET = new OracleResultSetType();

    public OracleDialect() {
        super(new OracleJdbcMappingVisitor(),
                new OracleSqlLogFormattingVisitor());
    }

    public OracleDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor);

        domainClassMap.put("binary_double", DoubleDomain.class);
        domainClassMap.put("binary_float", FloatDomain.class);
        domainClassMap.put("long", StringDomain.class);
        domainClassMap.put("long raw", BytesDomain.class);
        domainClassMap.put("nvarchar2", StringDomain.class);
        domainClassMap.put("raw", BytesDomain.class);
        domainClassMap.put("varchar2", StringDomain.class);
    }

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
        return RESULT_SET;
    }

    @Override
    public boolean isJdbcCommentAvailable() {
        return false;
    }

    @Override
    public String getTableComment(Connection connection, String catalogName,
            String schemaName, String tableName) throws SQLException {
        if (connection == null) {
            throw new DomaIllegalArgumentException("connection", connection);
        }
        if (tableName == null) {
            throw new DomaIllegalArgumentException("tableName", tableName);
        }
        String sql = "select comments from all_tab_comments where owner = "
                + schemaName + " and table_name = " + tableName
                + " and table_type = 'TABLE'";
        PreparedStatement preparedStatement = JdbcUtil
                .prepareStatement(connection, sql);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
                return null;
            } finally {
                JdbcUtil.close(resultSet, null);
            }
        } finally {
            JdbcUtil.close(preparedStatement, null);
        }
    }

    @Override
    public Map<String, String> getColumnCommentMap(Connection connection,
            String catalogName, String schemaName, String tableName)
            throws SQLException {
        if (connection == null) {
            throw new DomaIllegalArgumentException("connection", connection);
        }
        if (tableName == null) {
            throw new DomaIllegalArgumentException("tableName", tableName);
        }
        String sql = "select column_name, comments from all_col_comments where owner = "
                + schemaName + " and table_name = " + tableName;
        PreparedStatement preparedStatement = JdbcUtil
                .prepareStatement(connection, sql);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                Map<String, String> commentMap = new HashMap<String, String>();
                if (resultSet.next()) {
                    commentMap.put(resultSet.getString(1), resultSet
                            .getString(2));
                }
                return commentMap;
            } finally {
                JdbcUtil.close(resultSet, null);
            }
        } finally {
            JdbcUtil.close(preparedStatement, null);
        }
    }

    @Override
    public Class<? extends Domain<?, ?>> getDomainClass(String typeName,
            int sqlType, int length, int precision, int scale) {
        if ("number".equalsIgnoreCase(typeName)) {
            if (scale != 0) {
                return BigDecimalDomain.class;
            }
            if (precision < 5) {
                return ShortDomain.class;
            }
            if (precision < 10) {
                return IntegerDomain.class;
            }
            if (precision < 19) {
                return LongDomain.class;
            }
            return BigIntegerDomain.class;
        }
        return super
                .getDomainClass(typeName, sqlType, length, precision, scale);
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new OracleSqlBlockContext();
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "/";
    }

    public static class OracleResultSetType extends AbstractResultSetType {

        protected static int CURSOR = -10;

        public OracleResultSetType() {
            super(CURSOR);
        }
    }

    public static class OracleJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {

        @Override
        public Void visitAbstractBooleanDomain(AbstractBooleanDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
        }
    }

    public static class OracleSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {

        @Override
        public String visitAbstractBooleanDomain(
                AbstractBooleanDomain<?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
        }
    }

    public static class OracleSqlBlockContext extends StandardSqlBlockContext {

        protected OracleSqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays
                    .asList("create", "or", "replace", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays
                    .asList("create", "or", "replace", "function"));
            sqlBlockStartKeywordsList.add(Arrays
                    .asList("create", "or", "replace", "triger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }
}
