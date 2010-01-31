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

import java.sql.SQLException;
import java.util.Collections;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.HsqldbPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.wrapper.Wrapper;

/**
 * HSQLDB用の方言です。
 * 
 * @author taedium
 * 
 */
public class HsqldbDialect extends StandardDialect {

    /** 一意制約違反を表すエラーコード */
    protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = -104;

    /**
     * インスタンスを構築します。
     */
    public HsqldbDialect() {
        this(new HsqldbJdbcMappingVisitor(),
                new HsqldbSqlLogFormattingVisitor(),
                new HsqldbExpressionFunctions());
    }

    /**
     * {@link JdbcMappingVisitor} を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     */
    public HsqldbDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        this(jdbcMappingVisitor, new HsqldbSqlLogFormattingVisitor(),
                new HsqldbExpressionFunctions());
    }

    /**
     * {@link SqlLogFormattingVisitor} を指定してインスタンスを構築します。
     * 
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public HsqldbDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(new HsqldbJdbcMappingVisitor(), sqlLogFormattingVisitor,
                new HsqldbExpressionFunctions());
    }

    /**
     * {@link ExpressionFunctions} を指定してインスタンスを構築します。
     * 
     * @param expressionFunctions
     *            SQLのコメント式で利用可能な関数群
     */
    public HsqldbDialect(ExpressionFunctions expressionFunctions) {
        this(new HsqldbJdbcMappingVisitor(),
                new HsqldbSqlLogFormattingVisitor(), expressionFunctions);
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
    public HsqldbDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(jdbcMappingVisitor, sqlLogFormattingVisitor,
                new HsqldbExpressionFunctions());
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
    public HsqldbDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    }

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
            throw new DomaNullPointerException("qualifiedTableName");
        }
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        String rawSql = "call identity()";
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<PreparedSqlParameter> emptyList());
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaNullPointerException("qualifiedSequenceName");
        }
        String rawSql = "select next value for "
                + qualifiedSequenceName
                + " from information_schema.system_tables where table_name = 'SYSTEM_TABLES'";
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<PreparedSqlParameter> emptyList());
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
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
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

    /**
     * HSQLDB用の {@link JdbcMappingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class HsqldbJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {
    }

    /**
     * HSQLDB用の {@link SqlLogFormattingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class HsqldbSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {
    }

    /**
     * HSQLDB用の {@link ExpressionFunctions} です。
     * 
     * @author taedium
     * 
     */
    public static class HsqldbExpressionFunctions extends
            StandardExpressionFunctions {
    }

}
