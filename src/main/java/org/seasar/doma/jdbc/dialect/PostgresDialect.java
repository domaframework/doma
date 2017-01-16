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
package org.seasar.doma.jdbc.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.function.Function;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.PostgresForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.PostgresPagingTransformer;
import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * PostgreSQL用の方言です。
 * 
 * @author taedium
 * 
 */
public class PostgresDialect extends StandardDialect {

    /** 一意制約違反を表す {@literal SQLState} */
    protected static final String UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE = "23505";

    /** {@link ResultSet} の JDBC型 */
    protected static final JdbcType<ResultSet> RESULT_SET = new PostgresResultSetType();

    /**
     * インスタンスを構築します。
     */
    public PostgresDialect() {
        this(new PostgresJdbcMappingVisitor(),
                new PostgresSqlLogFormattingVisitor(),
                new PostgresExpressionFunctions());
    }

    /**
     * {@link JdbcMappingVisitor} を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     */
    public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        this(jdbcMappingVisitor, new PostgresSqlLogFormattingVisitor(),
                new PostgresExpressionFunctions());
    }

    /**
     * {@link SqlLogFormattingVisitor} を指定してインスタンスを構築します。
     * 
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public PostgresDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(new PostgresJdbcMappingVisitor(), sqlLogFormattingVisitor,
                new PostgresExpressionFunctions());
    }

    /**
     * {@link ExpressionFunctions} を指定してインスタンスを構築します。
     * 
     * @param expressionFunctions
     *            SQLのコメント式で利用可能な関数群
     */
    public PostgresDialect(ExpressionFunctions expressionFunctions) {
        this(new PostgresJdbcMappingVisitor(),
                new PostgresSqlLogFormattingVisitor(), expressionFunctions);
    }

    /**
     * {@link JdbcMappingVisitor} と {@link SqlLogFormattingVisitor}
     * を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(jdbcMappingVisitor, sqlLogFormattingVisitor,
                new PostgresExpressionFunctions());
    }

    /**
     * {@link JdbcMappingVisitor} と {@link SqlLogFormattingVisitor} と
     * {@link ExpressionFunctions} を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     * @param expressionFunctions
     *            SQLのコメント式で利用可能な関数群
     */
    public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    }

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
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaNullPointerException("sqlException");
        }
        String state = getSQLState(sqlException);
        return UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE.equals(state);
    }

    @Override
    public PreparedSql getIdentitySelectSql(String catalogName,
            String schemaName, String tableName, String columnName,
            boolean isQuoteRequired, boolean isIdColumnQuoteRequired) {
        if (tableName == null) {
            throw new DomaNullPointerException("tableName");
        }
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        String identitySeqFuncExpr = createIdentitySequenceFunctionExpression(
        		catalogName, schemaName, tableName, columnName,
        		isQuoteRequired, isIdColumnQuoteRequired);
        StringBuilder buf = new StringBuilder(64);
        buf.append("select currval(");
        buf.append(identitySeqFuncExpr);
        buf.append(")");
        String rawSql = buf.toString();
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<InParameter<?>> emptyList(), SqlLogType.FORMATTED);
    }

    @Override
    public Sql<?> getIdentityReservationSql(String catalogName,
            String schemaName, String tableName, String columnName,
            boolean isQuoteRequired, boolean isIdColumnQuoteRequired,
            int reservationSize) {
        if (tableName == null) {
            throw new DomaNullPointerException("tableName");
        }
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        String identitySeqFuncExpr = createIdentitySequenceFunctionExpression(
        		catalogName, schemaName, tableName, columnName,
        		isQuoteRequired, isIdColumnQuoteRequired);
        StringBuilder buf = new StringBuilder(64);
        buf.append("select nextval(");
        buf.append(identitySeqFuncExpr);
        buf.append(") from generate_series(1, ");
        buf.append(reservationSize);
        buf.append(")");
        String rawSql = buf.toString();
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<InParameter<?>> emptyList(), SqlLogType.FORMATTED);
    }

    protected String createIdentitySequenceFunctionExpression(String catalogName,
            String schemaName, String tableName, String columnName,
            boolean isQuoteRequired, boolean isIdColumnQuoteRequired) {
        String qualifiedTableName = DatabaseObjectUtil.getQualifiedName(
                isQuoteRequired ? this::applyQuote : Function.identity(),
                catalogName, schemaName, tableName);
        String colName = isIdColumnQuoteRequired ? columnName : columnName.toLowerCase();
        return "pg_catalog.pg_get_serial_sequence('" + qualifiedTableName + "', '" + colName + "')";
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaNullPointerException("qualifiedSequenceName");
        }
        String rawSql = "select nextval('" + qualifiedSequenceName + "')";
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<InParameter<?>> emptyList(), SqlLogType.FORMATTED);
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
    public boolean supportsIdentityReservation() {
        return true;
    }

    @Override
    public boolean supportsSelectForUpdate(SelectForUpdateType type,
            boolean withTargets) {
        return type == SelectForUpdateType.NORMAL
                || type == SelectForUpdateType.NOWAIT;
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
    public ScriptBlockContext createScriptBlockContext() {
        return new PostgresScriptBlockContext();
    }

    /**
     * PostgreSQL用の {@link ResultSet} の {@link JdbcType} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class PostgresResultSetType extends AbstractResultSetType {

        public PostgresResultSetType() {
            super(Types.OTHER);
        }
    }

    /**
     * PostgreSQL用の {@link JdbcMappingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class PostgresJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {
    }

    /**
     * PostgreSQL用の {@link SqlLogFormattingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class PostgresSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {
    }

    /**
     * PostgreSQL用の {@link ExpressionFunctions} です。
     * 
     * @author taedium
     * 
     */
    public static class PostgresExpressionFunctions extends
            StandardExpressionFunctions {

        public PostgresExpressionFunctions() {
            super();
        }

        public PostgresExpressionFunctions(char[] wildcards) {
            super(wildcards);
        }

        protected PostgresExpressionFunctions(char escapeChar, char[] wildcards) {
            super(escapeChar, wildcards);
        }

    }

    /**
     * PostgreSQL用の {@link ScriptBlockContext} です。
     * 
     * @author taedium
     * @since 1.7.0
     */
    public static class PostgresScriptBlockContext implements
            ScriptBlockContext {

        protected boolean inBlock;

        @Override
        public void addKeyword(String keyword) {
            if ("$$".equals(keyword)) {
                inBlock = !inBlock;
            }
        }

        @Override
        public boolean isInBlock() {
            return inBlock;
        }
    }

}
