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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.StandardCountGettingTransformer;
import org.seasar.doma.internal.jdbc.dialect.StandardForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.StandardPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.PersistentWrapperVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.EnumType;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.wrapper.ArrayWrapper;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BlobWrapper;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.ByteWrapper;
import org.seasar.doma.wrapper.BytesWrapper;
import org.seasar.doma.wrapper.ClobWrapper;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.NClobWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 標準の方言です。
 * 
 * @author taedium
 * 
 */
public class StandardDialect implements Dialect {

    /** 開始の引用符 */
    protected static final char OPEN_QUOTE = '"';

    /** 終了の引用符 */
    protected static final char CLOSE_QUOTE = '"';

    /** 一意制約違反を表す {@literal SQLState} のセット */
    protected static final Set<String> UNIQUE_CONSTRAINT_VIOLATION_STATE_CODES = new HashSet<String>(
            Arrays.asList("23", "27", "44"));

    /** {@link Wrapper} をJDBCの型とマッピングするビジター */
    protected final JdbcMappingVisitor jdbcMappingVisitor;

    /** SQLのバインド変数にマッピングされる {@link Wrapper} をログ用のフォーマットされた文字列へと変換するビジター */
    protected final SqlLogFormattingVisitor sqlLogFormattingVisitor;

    /** SQLのコメント式で利用可能な関数群 */
    protected final ExpressionFunctions expressionFunctions;

    /**
     * インスタンスを構築します。
     */
    public StandardDialect() {
        this(new StandardJdbcMappingVisitor(),
                new StandardSqlLogFormattingVisitor(),
                new StandardExpressionFunctions());
    }

    /**
     * {@link JdbcMappingVisitor} を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Wrapper} をJDBCの型とマッピングするビジター
     */
    public StandardDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        this(jdbcMappingVisitor, new StandardSqlLogFormattingVisitor(),
                new StandardExpressionFunctions());
    }

    /**
     * {@link SqlLogFormattingVisitor} を指定してインスタンスを構築します。
     * 
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Wrapper}
     *            をログ用のフォーマットされた文字列へと変換するビジター
     */
    public StandardDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        this(new StandardJdbcMappingVisitor(), sqlLogFormattingVisitor,
                new StandardExpressionFunctions());
    }

    /**
     * {@link ExpressionFunctions} を指定してインスタンスを構築します。
     * 
     * @param expressionFunctions
     *            SQLのコメント式で利用可能な関数群
     */
    public StandardDialect(ExpressionFunctions expressionFunctions) {
        this(new StandardJdbcMappingVisitor(),
                new StandardSqlLogFormattingVisitor(), expressionFunctions);
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
    public StandardDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        if (jdbcMappingVisitor == null) {
            throw new DomaNullPointerException("jdbcMappingVisitor");
        }
        if (sqlLogFormattingVisitor == null) {
            throw new DomaNullPointerException("sqlLogFormattingVisitor");
        }
        if (expressionFunctions == null) {
            throw new DomaNullPointerException("expressionFunctions");
        }
        this.jdbcMappingVisitor = jdbcMappingVisitor;
        this.sqlLogFormattingVisitor = sqlLogFormattingVisitor;
        this.expressionFunctions = expressionFunctions;
    }

    @Override
    public String getName() {
        return "standard";
    }

    @Override
    public SqlNode transformSelectSqlNode(SqlNode sqlNode, SelectOptions options) {
        if (sqlNode == null) {
            throw new DomaNullPointerException("sqlNode");
        }
        if (options == null) {
            throw new DomaNullPointerException("options");
        }
        SqlNode transformed = sqlNode;
        if (SelectOptionsAccessor.isCount(options)) {
            transformed = toCountCalculatingSqlNode(sqlNode);
        }
        long offset = SelectOptionsAccessor.getOffset(options);
        long limit = SelectOptionsAccessor.getLimit(options);
        if (offset >= 0 || limit >= 0) {
            transformed = toPagingSqlNode(transformed, offset, limit);
        }
        SelectForUpdateType forUpdateType = SelectOptionsAccessor
                .getForUpdateType(options);
        if (forUpdateType != null) {
            String[] aliases = SelectOptionsAccessor.getAliases(options);
            if (!supportsSelectForUpdate(forUpdateType, false)) {
                throw new JdbcException(DomaMessageCode.DOMA2023, getName());
            }
            if (aliases.length > 0) {
                if (!supportsSelectForUpdate(forUpdateType, true)) {
                    throw new JdbcException(DomaMessageCode.DOMA2024, getName());
                }
            }
            int waitSeconds = SelectOptionsAccessor.getWaitSeconds(options);
            transformed = toForUpdateSqlNode(transformed, forUpdateType,
                    waitSeconds, aliases);
        }
        return transformed;
    }

    /**
     * 集計を計算するSQLノードに変換します。
     * 
     * @param sqlNode
     *            SQLノード
     * @return 変換されたSQLノード
     */
    protected SqlNode toCountCalculatingSqlNode(SqlNode sqlNode) {
        return sqlNode;
    }

    /**
     * ページング用のSQLノードに変換します。
     * 
     * @param sqlNode
     *            SQLノード
     * @param offset
     *            オフセット
     * @param limit
     *            リミット
     * @return 変換されたSQLノード
     */
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
        StandardPagingTransformer transformer = new StandardPagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    /**
     * 悲観的排他制御用のSQLノードに変換します。
     * 
     * @param sqlNode
     *            SQLノード
     * @param forUpdateType
     *            悲観的排他制御の種別
     * @param waitSeconds
     *            ロック取得の待機時間（秒）
     * @param aliases
     *            ロック対象のカラムやテーブルのエイリアス
     * @return 変換されたSQLノード
     */
    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode,
            SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        StandardForUpdateTransformer transformer = new StandardForUpdateTransformer(
                forUpdateType, waitSeconds, aliases);
        return transformer.transform(sqlNode);
    }

    @Override
    public SqlNode transformSelectSqlNodeForGettingCount(SqlNode sqlNode) {
        return toCountGettingSqlNode(sqlNode);
    }

    /**
     * 集計取得用のSQLノードに変換します。
     * 
     * @param sqlNode
     *            SQLノード
     * @return 変換されたSQLノード
     */
    protected SqlNode toCountGettingSqlNode(SqlNode sqlNode) {
        StandardCountGettingTransformer transformer = new StandardCountGettingTransformer();
        return transformer.transform(sqlNode);
    }

    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaNullPointerException("sqlException");
        }
        String state = getSQLState(sqlException);
        if (state != null && state.length() >= 2) {
            String code = state.substring(0, 2);
            return UNIQUE_CONSTRAINT_VIOLATION_STATE_CODES.contains(code);
        }
        return false;
    }

    /**
     * {@literal SQLState} を返します。
     * 
     * @param sqlException
     *            SQL例外
     * @return ステータスコード
     */
    protected String getSQLState(SQLException sqlException) {
        SQLException cause = getCauseSQLException(sqlException);
        return cause.getSQLState();
    }

    /**
     * ベンダー固有のエラーコードを返します。
     * 
     * @param sqlException
     *            SQL例外
     * @return エラーコード
     */
    protected int getErrorCode(SQLException sqlException) {
        SQLException cause = getCauseSQLException(sqlException);
        return cause.getErrorCode();
    }

    /**
     * チェーンされたもっとも上位の {@link SQLException} を返します。
     * 
     * @param sqlException
     *            SQL例外
     * @return 原因となったより上位のSQL例外
     */
    protected SQLException getCauseSQLException(SQLException sqlException) {
        SQLException cause = sqlException;
        for (Throwable t : sqlException) {
            if (SQLException.class.isInstance(t)) {
                cause = SQLException.class.cast(t);
            }
        }
        return cause;
    }

    @Override
    public Throwable getRootCause(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaNullPointerException("sqlException");
        }
        Throwable cause = sqlException;
        for (Throwable t : sqlException) {
            cause = t;
        }
        return cause;
    }

    @Override
    public boolean supportsAutoGeneratedKeys() {
        return false;
    }

    @Override
    public boolean supportsBatchUpdateResults() {
        return true;
    }

    @Override
    public boolean supportsIdentity() {
        return false;
    }

    @Override
    public boolean supportsSequence() {
        return false;
    }

    @Override
    public boolean includesIdentityColumn() {
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate(SelectForUpdateType type,
            boolean withTargets) {
        return false;
    }

    @Override
    public boolean supportsResultSetReturningAsOutParameter() {
        return false;
    }

    @Override
    public JdbcType<ResultSet> getResultSetType() {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "getResultSetType");
    }

    @Override
    public PreparedSql getIdentitySelectSql(String qualifiedTableName,
            String columnName) {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "getIdentitySelectSql");
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "getSequenceNextValString");
    }

    @Override
    public String applyQuote(String name) {
        return OPEN_QUOTE + name + CLOSE_QUOTE;
    }

    @Override
    public String removeQuote(String name) {
        if (name == null || name.length() <= 2) {
            return name;
        }
        char[] chars = name.toCharArray();
        if (chars[0] == OPEN_QUOTE && chars[chars.length - 1] == CLOSE_QUOTE) {
            return new String(chars, 1, chars.length - 2);
        }
        return name;
    }

    @Override
    public JdbcMappingVisitor getJdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    @Override
    public SqlLogFormattingVisitor getSqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    @Override
    public ExpressionFunctions getExpressionFunctions() {
        return expressionFunctions;
    }

    /**
     * 標準の {@link JdbcMappingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class StandardJdbcMappingVisitor implements
            JdbcMappingVisitor,
            PersistentWrapperVisitor<Void, JdbcMappingFunction, SQLException> {

        @Override
        public Void visitArrayWrapper(ArrayWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.ARRAY);
        }

        @Override
        public Void visitBigDecimalWrapper(BigDecimalWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.BIG_DECIMAL);
        }

        @Override
        public Void visitBigIntegerWrapper(BigIntegerWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.BIG_INTEGER);
        }

        @Override
        public Void visitBlobWrapper(BlobWrapper wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, JdbcTypes.BLOB);
        }

        @Override
        public Void visitBooleanWrapper(BooleanWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.BOOLEAN);
        }

        @Override
        public Void visitByteWrapper(ByteWrapper wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, JdbcTypes.BYTE);
        }

        @Override
        public Void visitBytesWrapper(BytesWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.BYTES);
        }

        @Override
        public Void visitClobWrapper(ClobWrapper wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, JdbcTypes.CLOB);
        }

        @Override
        public Void visitDateWrapper(DateWrapper wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, JdbcTypes.DATE);
        }

        @Override
        public Void visitDoubleWrapper(DoubleWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.DOUBLE);
        }

        @Override
        public Void visitFloatWrapper(FloatWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.FLOAT);
        }

        @Override
        public Void visitIntegerWrapper(IntegerWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.INTEGER);
        }

        @Override
        public Void visitLongWrapper(LongWrapper wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, JdbcTypes.LONG);
        }

        @Override
        public Void visitNClobWrapper(NClobWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.NCLOB);
        }

        @Override
        public Void visitShortWrapper(ShortWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.SHORT);
        }

        @Override
        public Void visitStringWrapper(StringWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.STRING);
        }

        @Override
        public Void visitTimeWrapper(TimeWrapper wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, JdbcTypes.TIME);
        }

        @Override
        public Void visitTimestampWrapper(TimestampWrapper wrapper,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(wrapper, JdbcTypes.TIMESTAMP);
        }

        @Override
        public <E extends Enum<E>> Void visitEnumWrapper(
                EnumWrapper<E> wrapper, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(wrapper, new EnumType<E>(wrapper.getEnumClass()));
        }

        @Override
        public Void visitUnknownWrapper(Wrapper<?> wrapper,
                JdbcMappingFunction p) throws SQLException {
            throw new JdbcException(DomaMessageCode.DOMA2019, wrapper
                    .getClass().getName());
        }
    }

    /**
     * 標準の {@link SqlLogFormattingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class StandardSqlLogFormattingVisitor
            implements
            SqlLogFormattingVisitor,
            PersistentWrapperVisitor<String, SqlLogFormattingFunction, RuntimeException> {

        @Override
        public String visitArrayWrapper(ArrayWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.ARRAY);
        }

        @Override
        public String visitBigDecimalWrapper(BigDecimalWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.BIG_DECIMAL);
        }

        @Override
        public String visitBigIntegerWrapper(BigIntegerWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.BIG_INTEGER);
        }

        @Override
        public String visitBlobWrapper(BlobWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.BLOB);
        }

        @Override
        public String visitBooleanWrapper(BooleanWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.BOOLEAN);
        }

        @Override
        public String visitByteWrapper(ByteWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.BYTE);
        }

        @Override
        public String visitBytesWrapper(BytesWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.BYTES);
        }

        @Override
        public String visitClobWrapper(ClobWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.CLOB);
        }

        @Override
        public String visitDateWrapper(DateWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.DATE);
        }

        @Override
        public String visitDoubleWrapper(DoubleWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.DOUBLE);
        }

        @Override
        public String visitFloatWrapper(FloatWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.FLOAT);
        }

        @Override
        public String visitIntegerWrapper(IntegerWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.INTEGER);
        }

        @Override
        public String visitLongWrapper(LongWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.LONG);
        }

        @Override
        public String visitNClobWrapper(NClobWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.NCLOB);
        }

        @Override
        public String visitShortWrapper(ShortWrapper wrapper,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(wrapper, JdbcTypes.SHORT);
        }

        @Override
        public String visitStringWrapper(StringWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.STRING);
        }

        @Override
        public String visitTimeWrapper(TimeWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.TIME);
        }

        @Override
        public String visitTimestampWrapper(TimestampWrapper wrapper,
                SqlLogFormattingFunction p) {
            return p.apply(wrapper, JdbcTypes.TIMESTAMP);
        }

        @Override
        public <E extends Enum<E>> String visitEnumWrapper(
                EnumWrapper<E> wrapper, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(wrapper, new EnumType<E>(wrapper.getEnumClass()));
        }

        @Override
        public String visitUnknownWrapper(Wrapper<?> wrapper,
                SqlLogFormattingFunction p) {
            throw new JdbcException(DomaMessageCode.DOMA2019, wrapper
                    .getClass().getName());
        }

    }

    /**
     * 標準の {@link ExpressionFunctions} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class StandardExpressionFunctions implements
            ExpressionFunctions {

        /** デフォルトのエスケープ文字 */
        private static char DEFAULT_ESCAPE = '\\';

        /** デフォルトのワイルドカード */
        private final static char[] DEFAULT_WILDCARDS = { '%', '_' };

        /** エスケープ文字 */
        protected final char escape;

        /** ワイルドカード */
        protected final char[] wildcards;

        /** デフォルトのワイルドカード置換パターン */
        protected final Pattern defaultWildcardReplacementPattern;

        /** デフォルトの置換文字列正規表現 */
        protected final String defaultReplacement;

        /**
         * コンストラクタを構築します。
         */
        protected StandardExpressionFunctions() {
            this(DEFAULT_WILDCARDS);
        }

        /**
         * ワイルドカードを指定してコンストラクタを構築します。
         * 
         * @param wildcards
         *            ワイルドカード
         */
        protected StandardExpressionFunctions(char[] wildcards) {
            this(DEFAULT_ESCAPE, wildcards);
        }

        /**
         * エスケープ文字とワイルドカードを指定してコンストラクタを構築します。
         * 
         * @param escape
         *            エスケープ文字
         * @param wildcards
         *            ワイルドカード
         */
        protected StandardExpressionFunctions(char escape, char[] wildcards) {
            this.escape = escape;
            this.wildcards = wildcards != null ? wildcards : DEFAULT_WILDCARDS;
            this.defaultWildcardReplacementPattern = createWildcardReplacementPattern(
                    escape, this.wildcards);
            this.defaultReplacement = createWildcardReplacement(escape);
        }

        @Override
        public String startWith(String text) {
            if (text == null) {
                return null;
            }
            String escaped = escapeWildcard(defaultWildcardReplacementPattern,
                    text, defaultReplacement);
            return escaped + "%";
        }

        @Override
        public String startWith(String text, char escape) {
            if (text == null) {
                return null;
            }
            return escapeWildcard(text, escape) + "%";
        }

        @Override
        public String endWith(String text) {
            if (text == null) {
                return null;
            }
            String escaped = escapeWildcard(defaultWildcardReplacementPattern,
                    text, defaultReplacement);
            return "%" + escaped;
        }

        @Override
        public String endWith(String text, char escape) {
            if (text == null) {
                return null;
            }
            return "%" + escapeWildcard(text, escape);
        }

        @Override
        public String contain(String text) {
            if (text == null) {
                return null;
            }
            if (text.isEmpty()) {
                return "%";
            }
            String escaped = escapeWildcard(defaultWildcardReplacementPattern,
                    text, defaultReplacement);
            return "%" + escaped + "%";
        }

        @Override
        public String contain(String text, char escape) {
            if (text == null) {
                return null;
            }
            if (text.isEmpty()) {
                return "%";
            }
            return "%" + escapeWildcard(text, escape) + "%";
        }

        /**
         * 入力に含まれるワイルドカードをエスケープします。
         * 
         * @param input
         *            入力
         * @param escape
         *            エスケープ文字
         * @return エスケープされた文字列
         */
        protected String escapeWildcard(String input, char escape) {
            Pattern pattern = createWildcardReplacementPattern(escape,
                    wildcards);
            String replacement = createWildcardReplacement(escape);
            return escapeWildcard(pattern, input, replacement);
        }

        /**
         * ワイルドカードを正規表現でエスケープします。
         * 
         * @param pattern
         *            パターン
         * @param input
         *            入力
         * @param replacement
         *            置換文字列正規表現
         * @return エスケープされた文字列
         */
        protected String escapeWildcard(Pattern pattern, String input,
                String replacement) {
            Matcher matcher = pattern.matcher(input);
            return matcher.replaceAll(replacement);
        }

        @Override
        public Date roundDownTimePart(Date date) {
            if (date == null) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return new Date(calendar.getTimeInMillis());
        }

        @Override
        public Timestamp roundDownTimePart(Timestamp timestamp) {
            if (timestamp == null) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return new Timestamp(calendar.getTimeInMillis());
        }

        @Override
        public Date roundUpTimePart(Date date) {
            if (date == null) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.roll(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return new Date(calendar.getTimeInMillis());
        }

        @Override
        public Timestamp roundUpTimePart(Timestamp timestamp) {
            if (timestamp == null) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            calendar.roll(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return new Timestamp(calendar.getTimeInMillis());
        }

        /**
         * ワイルドカード置換パターンを作成します。
         * 
         * @param escape
         *            エスケープ文字
         * @param wildcards
         *            ワイルドカード
         * @return パターン
         */
        protected static Pattern createWildcardReplacementPattern(char escape,
                char[] wildcards) {
            StringBuilder buf = new StringBuilder();
            buf.append("[");
            buf.append(Matcher.quoteReplacement(String.valueOf(escape)));
            for (char wildcard : wildcards) {
                buf.append(wildcard);
            }
            buf.append("]");
            return Pattern.compile(buf.toString());
        }

        /**
         * ワイルドカード置換文字列正規表現を作成します。
         * 
         * @param escape
         *            エスケープ
         * @return ワイルドカード置換文字列正規表現
         */
        protected static String createWildcardReplacement(char escape) {
            return Matcher.quoteReplacement(String.valueOf(escape)) + "$0";
        }
    }
}
