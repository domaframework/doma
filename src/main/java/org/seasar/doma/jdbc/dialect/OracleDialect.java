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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.OracleForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.OraclePagingTransformer;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormatter;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.LocalDateTimeWrapper;
import org.seasar.doma.wrapper.LocalDateWrapper;
import org.seasar.doma.wrapper.LocalTimeWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;
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
     * {@link JdbcMappingVisitor} と {@link SqlLogFormattingVisitor}
     * を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public OracleDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(jdbcMappingVisitor, sqlLogFormattingVisitor,
                new OracleExpressionFunctions());
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
        return new PreparedSql(SqlKind.SELECT, rawSql, rawSql, null,
                Collections.<InParameter<?>> emptyList(), SqlLogType.FORMATTED);
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
    public String getScriptBlockDelimiter() {
        return "/";
    }

    @Override
    public ScriptBlockContext createScriptBlockContext() {
        return new OracleScriptBlockContext();
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
                JdbcMappingFunction p, JdbcMappingHint q) throws SQLException {
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

        /** {@link Date}用日付フォーマッタ */
        protected DateFormatter dateFormatter = new DateFormatter();

        /** 時刻フォーマッタ */
        protected TimeFormatter timeFormatter = new TimeFormatter();

        /** タイムスタンプフォーマッタ */
        protected TimestampFormatter timestampFormatter = new TimestampFormatter();

        /** {@link java.util.Date}用日付フォーマッタ */
        protected UtilDateFormatter utilDateFormatter = new UtilDateFormatter();

        /** {@link LocalDate}用日付フォーマッタ */
        protected LocalDateFormatter localDateFormatter = new LocalDateFormatter(
                dateFormatter);

        /** {@link LocalDateTime}用タイムスタンプフォーマッタ */
        protected LocalDateTimeFormatter localDateTimeFormatter = new LocalDateTimeFormatter(
                timestampFormatter);

        /** {@link LocalTime}用時刻フォーマッタ */
        protected LocalTimeFormatter localTimeFormatter = new LocalTimeFormatter(
                timeFormatter);

        @Override
        public String visitBooleanWrapper(BooleanWrapper wrapper,
                SqlLogFormattingFunction p, Void q) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
        }

        @Override
        public String visitDateWrapper(DateWrapper wrapper,
                SqlLogFormattingFunction p, Void q) {
            return p.apply(wrapper, dateFormatter);
        }

        @Override
        public String visitLocalDateWrapper(LocalDateWrapper wrapper,
                SqlLogFormattingFunction p, Void q) throws RuntimeException {
            return p.apply(wrapper, localDateFormatter);
        }

        @Override
        public String visitLocalDateTimeWrapper(LocalDateTimeWrapper wrapper,
                SqlLogFormattingFunction p, Void q) throws RuntimeException {
            return p.apply(wrapper, localDateTimeFormatter);
        }

        @Override
        public String visitLocalTimeWrapper(LocalTimeWrapper wrapper,
                SqlLogFormattingFunction p, Void q) throws RuntimeException {
            return p.apply(wrapper, localTimeFormatter);
        }

        @Override
        public String visitTimeWrapper(TimeWrapper wrapper,
                SqlLogFormattingFunction p, Void q) {
            return p.apply(wrapper, timeFormatter);
        }

        @Override
        public String visitTimestampWrapper(TimestampWrapper wrapper,
                SqlLogFormattingFunction p, Void q) {
            return p.apply(wrapper, timestampFormatter);
        }

        @Override
        public String visitUtilDateWrapper(UtilDateWrapper wrapper,
                SqlLogFormattingFunction p, Void q) {
            return p.apply(wrapper, utilDateFormatter);
        }

        /**
         * {@link Date} をdateリテラルへ変換するフォーマッタです。
         * 
         * @author taedium
         * 
         */
        protected static class DateFormatter implements SqlLogFormatter<Date> {

            @Override
            public String convertToLogFormat(Date value) {
                if (value == null) {
                    return "null";
                }
                return "date'" + value + "'";
            }
        }

        /**
         * timeリテラルへ変換するフォーマッタです。
         * 
         * @author taedium
         * 
         */
        protected static class TimeFormatter implements SqlLogFormatter<Time> {

            @Override
            public String convertToLogFormat(Time value) {
                if (value == null) {
                    return "null";
                }
                return "time'" + value + "'";
            }
        }

        /**
         * timestampリテラルへ変換するフォーマッタです。
         * 
         * @author taedium
         * 
         */
        protected static class TimestampFormatter implements
                SqlLogFormatter<Timestamp> {

            @Override
            public String convertToLogFormat(Timestamp value) {
                if (value == null) {
                    return "null";
                }
                return "timestamp'" + value + "'";
            }
        }

        /**
         * {@link java.util.Date} をdateリテラルへ変換するフォーマッタです。
         * 
         * @author taedium
         * @since 1.9.0
         */
        protected static class UtilDateFormatter implements
                SqlLogFormatter<java.util.Date> {

            @Override
            public String convertToLogFormat(java.util.Date value) {
                if (value == null) {
                    return "null";
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS");
                return "timestamp'" + dateFormat.format(value) + "'";
            }
        }

        /**
         * {@link LocalDate} をdateリテラルへ変換するフォーマッタです。
         * 
         * @author nakamura-to
         * @since 2.0.0
         */
        protected static class LocalDateFormatter implements
                SqlLogFormatter<LocalDate> {

            protected final DateFormatter delegate;

            protected LocalDateFormatter(DateFormatter delegate) {
                AssertionUtil.assertNotNull(delegate);
                this.delegate = delegate;
            }

            @Override
            public String convertToLogFormat(LocalDate value) {
                if (value == null) {
                    return "null";
                }
                return delegate.convertToLogFormat(Date.valueOf(value));
            }
        }

        /**
         * {@link LocalDateTime} をtimestampリテラルへ変換するフォーマッタです。
         * 
         * @author nakamura-to
         * @since 2.0.0
         */
        protected static class LocalDateTimeFormatter implements
                SqlLogFormatter<LocalDateTime> {

            protected final TimestampFormatter delegate;

            protected LocalDateTimeFormatter(TimestampFormatter delegate) {
                AssertionUtil.assertNotNull(delegate);
                this.delegate = delegate;
            }

            @Override
            public String convertToLogFormat(LocalDateTime value) {
                if (value == null) {
                    return "null";
                }
                return delegate.convertToLogFormat(Timestamp.valueOf(value));
            }
        }

        /**
         * {@link LocalTime} をtimeリテラルへ変換するフォーマッタです。
         * 
         * @author nakamura-to
         * @since 2.0.0
         */
        protected static class LocalTimeFormatter implements
                SqlLogFormatter<LocalTime> {

            protected final TimeFormatter delegate;

            protected LocalTimeFormatter(TimeFormatter delegate) {
                AssertionUtil.assertNotNull(delegate);
                this.delegate = delegate;
            }

            @Override
            public String convertToLogFormat(LocalTime value) {
                if (value == null) {
                    return "null";
                }
                return delegate.convertToLogFormat(Time.valueOf(value));
            }
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

        public OracleExpressionFunctions(char[] wildcards) {
            super(wildcards);
        }

        protected OracleExpressionFunctions(char escapeChar, char[] wildcards) {
            super(escapeChar, wildcards);
        }

    }

    /**
     * Oracle用の {@link ScriptBlockContext} です。
     * 
     * @author taedium
     * @since 1.7.0
     */
    public static class OracleScriptBlockContext extends
            StandardScriptBlockContext {

        protected OracleScriptBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "or",
                    "replace", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "or",
                    "replace", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "or",
                    "replace", "triger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }

}
