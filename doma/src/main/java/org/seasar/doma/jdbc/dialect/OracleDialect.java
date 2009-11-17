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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.OracleCountGettingTransformer;
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
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * Oracle用の方言です。
 * 
 * @author taedium
 * 
 */
public class OracleDialect extends StandardDialect {

    /** 一意制約違反を表すエラーコード */
    protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = 1;

    /** {@link ResultSet} の JDBC型 */
    protected static final JdbcType<ResultSet> RESULT_SET = new OracleResultSetType();

    /**
     * インスタンスを構築します。
     */
    public OracleDialect() {
        this(new OracleJdbcMappingVisitor(),
                new OracleSqlLogFormattingVisitor(),
                new OracleExpressionFunctions());
    }

    /**
     * {@link JdbcMappingVisitor} を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     */
    public OracleDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        this(jdbcMappingVisitor, new OracleSqlLogFormattingVisitor(),
                new OracleExpressionFunctions());
    }

    /**
     * {@link SqlLogFormattingVisitor} を指定してインスタンスを構築します。
     * 
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public OracleDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(new OracleJdbcMappingVisitor(), sqlLogFormattingVisitor,
                new OracleExpressionFunctions());
    }

    /**
     * {@link ExpressionFunctions} を指定してインスタンスを構築します。
     * 
     * @param expressionFunctions
     *            SQLのコメント式で利用可能な関数群
     */
    public OracleDialect(ExpressionFunctions expressionFunctions) {
        this(new OracleJdbcMappingVisitor(),
                new OracleSqlLogFormattingVisitor(), expressionFunctions);
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
    public OracleDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
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
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
        OraclePagingTransformer transformer = new OraclePagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    protected SqlNode toCountGettingSqlNode(SqlNode sqlNode) {
        OracleCountGettingTransformer transformer = new OracleCountGettingTransformer();
        return transformer.transform(sqlNode);
    }

    @Override
    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaNullPointerException("sqlException");
        }
        int code = getErrorCode(sqlException);
        return UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE == code;
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaNullPointerException("qualifiedSequenceName");
        }
        String rawSql = "select " + qualifiedSequenceName
                + ".nextval from dual";
        return new PreparedSql(rawSql, rawSql, null, Collections
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

    /**
     * Oracle用の {@link ResultSet} の {@link JdbcType} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class OracleResultSetType extends AbstractResultSetType {

        protected static int CURSOR = -10;

        public OracleResultSetType() {
            super(CURSOR);
        }
    }

    /**
     * Oracle用の {@link JdbcMappingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class OracleJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {

        @Override
        public Void visitBooleanWrapper(BooleanWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
        }
    }

    /**
     * Oracle用の {@link SqlLogFormattingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class OracleSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {

        @Override
        public String visitBooleanWrapper(BooleanWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
        }
    }

    /**
     * Oracle用の {@link ExpressionFunctions} です。
     * 
     * @author taedium
     * 
     */
    public static class OracleExpressionFunctions extends
            StandardExpressionFunctions {

        private final static char[] DEFAULT_WILDCARDS = { '%', '_', '％', '＿' };

        public OracleExpressionFunctions() {
            super(DEFAULT_WILDCARDS);
        }

    }

}
