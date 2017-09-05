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

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.H2ForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.H2PagingTransformer;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;

/**
 * A dialect for H2.
 */
public class H2Dialect extends H212126Dialect {

    /** the error code that represents unique violation */
    protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = 23505;

    public H2Dialect() {
        this(new H2JdbcMappingVisitor(), new H2SqlLogFormattingVisitor(),
                new H2ExpressionFunctions());
    }

    public H2Dialect(JdbcMappingVisitor jdbcMappingVisitor) {
        this(jdbcMappingVisitor, new H2SqlLogFormattingVisitor(), new H2ExpressionFunctions());
    }

    public H2Dialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(new H2JdbcMappingVisitor(), sqlLogFormattingVisitor, new H2ExpressionFunctions());
    }

    public H2Dialect(ExpressionFunctions expressionFunctions) {
        this(new H2JdbcMappingVisitor(), new H2SqlLogFormattingVisitor(), expressionFunctions);
    }

    public H2Dialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(jdbcMappingVisitor, sqlLogFormattingVisitor, new H2ExpressionFunctions());
    }

    public H2Dialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
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
        H2PagingTransformer transformer = new H2PagingTransformer(offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode, SelectForUpdateType forUpdateType,
            int waitSeconds, String... aliases) {
        H2ForUpdateTransformer transformer = new H2ForUpdateTransformer(forUpdateType, waitSeconds,
                aliases);
        return transformer.transform(sqlNode);
    }

    public static class H2JdbcMappingVisitor extends H212126JdbcMappingVisitor {
    }

    public static class H2SqlLogFormattingVisitor extends H212126SqlLogFormattingVisitor {
    }

    public static class H2ExpressionFunctions extends H212126ExpressionFunctions {

        public H2ExpressionFunctions() {
            super();
        }

        public H2ExpressionFunctions(char[] wildcards) {
            super(wildcards);
        }

        protected H2ExpressionFunctions(char escapeChar, char[] wildcards) {
            super(escapeChar, wildcards);
        }

    }

}
