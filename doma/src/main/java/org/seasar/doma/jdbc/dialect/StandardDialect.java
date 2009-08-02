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
import org.seasar.doma.domain.AbstractArrayDomain;
import org.seasar.doma.domain.AbstractBigDecimalDomain;
import org.seasar.doma.domain.AbstractBigIntegerDomain;
import org.seasar.doma.domain.AbstractBlobDomain;
import org.seasar.doma.domain.AbstractBooleanDomain;
import org.seasar.doma.domain.AbstractByteDomain;
import org.seasar.doma.domain.AbstractBytesDomain;
import org.seasar.doma.domain.AbstractClobDomain;
import org.seasar.doma.domain.AbstractDateDomain;
import org.seasar.doma.domain.AbstractDoubleDomain;
import org.seasar.doma.domain.AbstractFloatDomain;
import org.seasar.doma.domain.AbstractIntegerDomain;
import org.seasar.doma.domain.AbstractLongDomain;
import org.seasar.doma.domain.AbstractNClobDomain;
import org.seasar.doma.domain.AbstractShortDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
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
    public SqlNode transformSelectSqlNode(SqlNode original,
            SelectOptions options) {
        if (original == null) {
            throw new DomaNullPointerException("original");
        }
        if (options == null) {
            throw new DomaNullPointerException("options");
        }
        SqlNode transformed = original;
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
     * @param forUpdateType
     * @param waitSeconds
     * @param aliases
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
        public Void visitAbstractArrayDomain(AbstractArrayDomain<?, ?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.ARRAY);
        }

        @Override
        public Void visitAbstractBigDecimalDomain(
                AbstractBigDecimalDomain<?> domain, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(domain, JdbcTypes.BIG_DECIMAL);
        }

        @Override
        public Void visitAbstractBigIntegerDomain(
                AbstractBigIntegerDomain<?> domain, JdbcMappingFunction p)
                throws SQLException {
            return p.apply(domain, JdbcTypes.BIG_INTEGER);
        }

        @Override
        public Void visitAbstractBlobDomain(AbstractBlobDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BLOB);
        }

        @Override
        public Void visitAbstractBooleanDomain(AbstractBooleanDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BOOLEAN);
        }

        @Override
        public Void visitAbstractByteDomain(AbstractByteDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BYTE);
        }

        @Override
        public Void visitAbstractBytesDomain(AbstractBytesDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.BYTES);
        }

        @Override
        public Void visitAbstractClobDomain(AbstractClobDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.CLOB);
        }

        @Override
        public Void visitAbstractDateDomain(AbstractDateDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.DATE);
        }

        @Override
        public Void visitAbstractDoubleDomain(AbstractDoubleDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.DOUBLE);
        }

        @Override
        public Void visitAbstractFloatDomain(AbstractFloatDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.FLOAT);
        }

        @Override
        public Void visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.INTEGER);
        }

        @Override
        public Void visitAbstractLongDomain(AbstractLongDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.LONG);
        }

        @Override
        public Void visitAbstractNClobDomain(AbstractNClobDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.NCLOB);
        }

        @Override
        public Void visitAbstractShortDomain(AbstractShortDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.SHORT);
        }

        @Override
        public Void visitAbstractStringDomain(AbstractStringDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.STRING);
        }

        @Override
        public Void visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
                JdbcMappingFunction p) throws SQLException {
            return p.apply(domain, JdbcTypes.TIME);
        }

        @Override
        public Void visitAbstractTimestampDomain(
                AbstractTimestampDomain<?> domain, JdbcMappingFunction p)
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
                AbstractArrayDomain<?, ?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.ARRAY);
        }

        @Override
        public String visitAbstractBigDecimalDomain(
                AbstractBigDecimalDomain<?> domain, SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.BIG_DECIMAL);
        }

        @Override
        public String visitAbstractBigIntegerDomain(
                AbstractBigIntegerDomain<?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.BIG_INTEGER);
        }

        @Override
        public String visitAbstractBlobDomain(AbstractBlobDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.BLOB);
        }

        @Override
        public String visitAbstractBooleanDomain(
                AbstractBooleanDomain<?> domain, SqlLogFormattingFunction p)
                throws RuntimeException {
            return p.apply(domain, JdbcTypes.BOOLEAN);
        }

        @Override
        public String visitAbstractByteDomain(AbstractByteDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.BYTE);
        }

        @Override
        public String visitAbstractBytesDomain(AbstractBytesDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.BYTES);
        }

        @Override
        public String visitAbstractClobDomain(AbstractClobDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.CLOB);
        }

        @Override
        public String visitAbstractDateDomain(AbstractDateDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.DATE);
        }

        @Override
        public String visitAbstractDoubleDomain(AbstractDoubleDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.DOUBLE);
        }

        @Override
        public String visitAbstractFloatDomain(AbstractFloatDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.FLOAT);
        }

        @Override
        public String visitAbstractIntegerDomain(
                AbstractIntegerDomain<?> domain, SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.INTEGER);
        }

        @Override
        public String visitAbstractLongDomain(AbstractLongDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.LONG);
        }

        @Override
        public String visitAbstractNClobDomain(AbstractNClobDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.NCLOB);
        }

        @Override
        public String visitAbstractShortDomain(AbstractShortDomain<?> domain,
                SqlLogFormattingFunction p) throws RuntimeException {
            return p.apply(domain, JdbcTypes.SHORT);
        }

        @Override
        public String visitAbstractStringDomain(AbstractStringDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.STRING);
        }

        @Override
        public String visitAbstractTimeDomain(AbstractTimeDomain<?> domain,
                SqlLogFormattingFunction p) {
            return p.apply(domain, JdbcTypes.TIME);
        }

        @Override
        public String visitAbstractTimestampDomain(
                AbstractTimestampDomain<?> domain, SqlLogFormattingFunction p) {
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
