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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.domain.ArrayDomain;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.BigIntegerDomain;
import org.seasar.doma.domain.BlobDomain;
import org.seasar.doma.domain.BooleanDomain;
import org.seasar.doma.domain.ByteDomain;
import org.seasar.doma.domain.BytesDomain;
import org.seasar.doma.domain.ClobDomain;
import org.seasar.doma.domain.DateDomain;
import org.seasar.doma.domain.DoubleDomain;
import org.seasar.doma.domain.FloatDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.LongDomain;
import org.seasar.doma.domain.NClobDomain;
import org.seasar.doma.domain.ShortDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.domain.TimeDomain;
import org.seasar.doma.domain.TimestampDomain;
import org.seasar.doma.domain.BuiltinDomainVisitor;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.jdbc.dialect.StandardForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.StandardPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.message.DomaMessageCode;

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

    /** {@link Domain} をJDBCの型とマッピングするビジター */
    protected final JdbcMappingVisitor jdbcMappingVisitor;

    /** SQLのバインド変数にマッピングされる {@link Domain} をログ用のフォーマットされた文字列へと変換するビジター */
    protected final SqlLogFormattingVisitor sqlLogFormattingVisitor;

    /**
     * インスタンスを構築します。
     */
    public StandardDialect() {
        this(new StandardJdbcMappingVisitor(),
                new StandardSqlLogFormattingVisitor());
    }

    /**
     * {@link JdbcMappingVisitor} と {@link SqlLogFormattingVisitor}
     * を指定してインスタンスを構築します。
     * 
     * @param jdbcMappingVisitor
     *            {@link Domain} をJDBCの型とマッピングするビジター
     * @param sqlLogFormattingVisitor
     *            SQLのバインド変数にマッピングされる {@link Domain} をログ用のフォーマットされた文字列へと変換するビジター
     */
    public StandardDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        if (jdbcMappingVisitor == null) {
            throw new DomaNullPointerException("jdbcMappingVisitor");
        }
        if (sqlLogFormattingVisitor == null) {
            throw new DomaNullPointerException("sqlLogFormattingVisitor");
        }
        this.jdbcMappingVisitor = jdbcMappingVisitor;
        this.sqlLogFormattingVisitor = sqlLogFormattingVisitor;
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
        if (options.getOffset() >= 0 || options.getLimit() >= 0) {
            transformed = toPagingSqlNode(transformed, options.getOffset(),
                    options.getLimit());
        }
        if (options.getForUpdateType() != null) {
            SelectForUpdateType forUpdateType = options.getForUpdateType();
            String[] aliases = options.getAliases();
            if (!supportsSelectForUpdate(forUpdateType, false)) {
                throw new JdbcException(DomaMessageCode.DOMA2023, getName());
            }
            if (aliases.length > 0) {
                if (!supportsSelectForUpdate(forUpdateType, true)) {
                    throw new JdbcException(DomaMessageCode.DOMA2024, getName());
                }
            }
            transformed = toForUpdateSqlNode(transformed, forUpdateType,
                    options.getWaitSeconds(), aliases);
        }
        return transformed;
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
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
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

    /**
     * 標準の {@link JdbcMappingVisitor} の実装です。
     * 
     * @author taedium
     * 
     */
    public static class StandardJdbcMappingVisitor implements
            JdbcMappingVisitor,
            BuiltinDomainVisitor<Void, JdbcMappingFunction, SQLException> {

        @Override
        public Void visitAbstractArrayDomain(ArrayDomain<?, ?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.ARRAY);
        }

        @Override
        public Void visitAbstractBigDecimalDomain(
                BigDecimalDomain<?> domain, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(domain, JdbcTypes.BIG_DECIMAL);
        }

        @Override
        public Void visitAbstractBigIntegerDomain(
                BigIntegerDomain<?> domain, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(domain, JdbcTypes.BIG_INTEGER);
        }

        @Override
        public Void visitAbstractBlobDomain(BlobDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BLOB);
        }

        @Override
        public Void visitAbstractBooleanDomain(BooleanDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BOOLEAN);
        }

        @Override
        public Void visitAbstractByteDomain(ByteDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BYTE);
        }

        @Override
        public Void visitAbstractBytesDomain(BytesDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BYTES);
        }

        @Override
        public Void visitAbstractClobDomain(ClobDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.CLOB);
        }

        @Override
        public Void visitAbstractDateDomain(DateDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.DATE);
        }

        @Override
        public Void visitAbstractDoubleDomain(DoubleDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.DOUBLE);
        }

        @Override
        public Void visitAbstractFloatDomain(FloatDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.FLOAT);
        }

        @Override
        public Void visitAbstractIntegerDomain(IntegerDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.INTEGER);
        }

        @Override
        public Void visitAbstractLongDomain(LongDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.LONG);
        }

        @Override
        public Void visitAbstractNClobDomain(NClobDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.NCLOB);
        }

        @Override
        public Void visitAbstractShortDomain(ShortDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.SHORT);
        }

        @Override
        public Void visitAbstractStringDomain(StringDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.STRING);
        }

        @Override
        public Void visitAbstractTimeDomain(TimeDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.TIME);
        }

        @Override
        public Void visitAbstractTimestampDomain(
                TimestampDomain<?> domain, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(domain, JdbcTypes.TIMESTAMP);
        }

        @Override
        public Void visitUnknownDomain(Domain<?, ?> domain,
                JdbcMappingFunction p) throws SQLException {
            throw new JdbcException(DomaMessageCode.DOMA2019, domain.getClass()
                    .getName());
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
            BuiltinDomainVisitor<String, SqlLogFormattingFunction, RuntimeException> {

        @Override
        public String visitAbstractArrayDomain(
                ArrayDomain<?, ?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.ARRAY);
        }

        @Override
        public String visitAbstractBigDecimalDomain(
                BigDecimalDomain<?> domain, SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.BIG_DECIMAL);
        }

        @Override
        public String visitAbstractBigIntegerDomain(
                BigIntegerDomain<?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.BIG_INTEGER);
        }

        @Override
        public String visitAbstractBlobDomain(BlobDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.BLOB);
        }

        @Override
        public String visitAbstractBooleanDomain(
                BooleanDomain<?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.BOOLEAN);
        }

        @Override
        public String visitAbstractByteDomain(ByteDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.BYTE);
        }

        @Override
        public String visitAbstractBytesDomain(BytesDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.BYTES);
        }

        @Override
        public String visitAbstractClobDomain(ClobDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.CLOB);
        }

        @Override
        public String visitAbstractDateDomain(DateDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.DATE);
        }

        @Override
        public String visitAbstractDoubleDomain(DoubleDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.DOUBLE);
        }

        @Override
        public String visitAbstractFloatDomain(FloatDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.FLOAT);
        }

        @Override
        public String visitAbstractIntegerDomain(
                IntegerDomain<?> domain, SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.INTEGER);
        }

        @Override
        public String visitAbstractLongDomain(LongDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.LONG);
        }

        @Override
        public String visitAbstractNClobDomain(NClobDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.NCLOB);
        }

        @Override
        public String visitAbstractShortDomain(ShortDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.SHORT);
        }

        @Override
        public String visitAbstractStringDomain(StringDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.STRING);
        }

        @Override
        public String visitAbstractTimeDomain(TimeDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.TIME);
        }

        @Override
        public String visitAbstractTimestampDomain(
                TimestampDomain<?> domain, SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.TIMESTAMP);
        }

        @Override
        public String visitUnknownDomain(Domain<?, ?> domain,
                SqlLogFormattingFunction p) {
            throw new JdbcException(DomaMessageCode.DOMA2019, domain.getClass()
                    .getName());
        }

    }
}
