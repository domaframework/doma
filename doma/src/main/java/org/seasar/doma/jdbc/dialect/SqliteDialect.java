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
import org.seasar.doma.internal.jdbc.dialect.SqlitePagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.wrapper.Wrapper;

/**
 * SQLite用の方言です。
 * 
 * @author taedium
 * 
 */
public class SqliteDialect extends StandardDialect {

    /**
     * インスタンスを構築します。
     */
    public SqliteDialect() {
        this(new SqliteJdbcMappingVisitor(),
                new SqliteSqlLogFormattingVisitor(),
                new SqliteExpressionFunctions());
    }

    /**
     * {@link JdbcMappingVisitor} を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     */
    public SqliteDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        this(jdbcMappingVisitor, new SqliteSqlLogFormattingVisitor(),
                new SqliteExpressionFunctions());
    }

    /**
     * {@link SqlLogFormattingVisitor} を指定してインスタンスを構築します。
     * 
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public SqliteDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(new SqliteJdbcMappingVisitor(), sqlLogFormattingVisitor,
                new SqliteExpressionFunctions());
    }

    /**
     * {@link ExpressionFunctions} を指定してインスタンスを構築します。
     * 
     * @param expressionFunctions
     *            SQLのコメント式で利用可能な関数群
     */
    public SqliteDialect(ExpressionFunctions expressionFunctions) {
        this(new SqliteJdbcMappingVisitor(),
                new SqliteSqlLogFormattingVisitor(), expressionFunctions);
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
    public SqliteDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(jdbcMappingVisitor, sqlLogFormattingVisitor,
                new SqliteExpressionFunctions());
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
    public SqliteDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    }

    @Override
    public String getName() {
        return "sqlite";
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
        String rawSql = "select last_insert_rowid()";
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<PreparedSqlParameter> emptyList());
    }

    @Override
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
        SqlitePagingTransformer transformer = new SqlitePagingTransformer(
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
    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaNullPointerException("sqlException");
        }
        SQLException cause = getCauseSQLException(sqlException);
        String message = cause.getMessage();
        return message != null && message.startsWith("[SQLITE_CONSTRAINT]")
                && message.contains(" unique)");
    }

    /**
     * SQLite用の {@link JdbcMappingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class SqliteJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {
    }

    /**
     * SQLite用の {@link SqlLogFormattingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class SqliteSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {
    }

    /**
     * SQLite用の {@link ExpressionFunctions} です。
     * 
     * @author taedium
     * 
     */
    public static class SqliteExpressionFunctions extends
            StandardExpressionFunctions {
    }

}
