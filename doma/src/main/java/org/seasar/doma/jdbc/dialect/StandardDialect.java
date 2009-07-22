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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
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
import org.seasar.doma.domain.AbstractNClobDomain;
import org.seasar.doma.domain.AbstractShortDomain;
import org.seasar.doma.domain.AbstractStringDomain;
import org.seasar.doma.domain.AbstractTimeDomain;
import org.seasar.doma.domain.AbstractTimestampDomain;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.BlobDomain;
import org.seasar.doma.domain.BooleanDomain;
import org.seasar.doma.domain.BuiltinDomainVisitor;
import org.seasar.doma.domain.BytesDomain;
import org.seasar.doma.domain.ClobDomain;
import org.seasar.doma.domain.DateDomain;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DoubleDomain;
import org.seasar.doma.domain.FloatDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.LongDomain;
import org.seasar.doma.domain.NClobDomain;
import org.seasar.doma.domain.ShortDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.domain.TimeDomain;
import org.seasar.doma.domain.TimestampDomain;
import org.seasar.doma.internal.jdbc.command.JdbcUtil;
import org.seasar.doma.internal.jdbc.dialect.StandardForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.StandardPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.util.TableUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class StandardDialect implements Dialect {

    protected static final char OPEN_QUOTE = '"';

    protected static final char CLOSE_QUOTE = '"';

    protected static final Set<String> UNIQUE_CONSTRAINT_VIOLATION_STATE_CODES = new HashSet<String>(
            Arrays.asList("23", "27", "44"));

    protected final JdbcMappingVisitor jdbcMappingVisitor;

    protected final SqlLogFormattingVisitor sqlLogFormattingVisitor;

    protected final Map<String, Class<? extends Domain<?, ?>>> domainClassMap = new HashMap<String, Class<? extends Domain<?, ?>>>();

    protected final Map<Integer, Class<? extends Domain<?, ?>>> fallbackDomainClassMap = new HashMap<Integer, Class<? extends Domain<?, ?>>>();

    public StandardDialect() {
        this(new StandardJdbcMappingVisitor(),
                new StandardSqlLogFormattingVisitor());
    }

    public StandardDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        if (jdbcMappingVisitor == null) {
            throw new DomaIllegalArgumentException("jdbcMappingVisitor",
                    jdbcMappingVisitor);
        }
        if (sqlLogFormattingVisitor == null) {
            throw new DomaIllegalArgumentException("sqlLogFormattingVisitor",
                    sqlLogFormattingVisitor);
        }
        this.jdbcMappingVisitor = jdbcMappingVisitor;
        this.sqlLogFormattingVisitor = sqlLogFormattingVisitor;

        domainClassMap.put("bigint", LongDomain.class);
        domainClassMap.put("binary", BytesDomain.class);
        domainClassMap.put("bit", BooleanDomain.class);
        domainClassMap.put("blob", BlobDomain.class);
        domainClassMap.put("boolean", BooleanDomain.class);
        domainClassMap.put("char", StringDomain.class);
        domainClassMap.put("clob", ClobDomain.class);
        domainClassMap.put("date", DateDomain.class);
        domainClassMap.put("decimal", BigDecimalDomain.class);
        domainClassMap.put("double", DoubleDomain.class);
        domainClassMap.put("float", FloatDomain.class);
        domainClassMap.put("integer", IntegerDomain.class);
        domainClassMap.put("longnvarchar", StringDomain.class);
        domainClassMap.put("longvarbinary", BytesDomain.class);
        domainClassMap.put("longvarchar", StringDomain.class);
        domainClassMap.put("nclob", NClobDomain.class);
        domainClassMap.put("nchar", StringDomain.class);
        domainClassMap.put("numeric", BigDecimalDomain.class);
        domainClassMap.put("nvarchar", StringDomain.class);
        domainClassMap.put("real", FloatDomain.class);
        domainClassMap.put("smallint", ShortDomain.class);
        domainClassMap.put("time", TimeDomain.class);
        domainClassMap.put("timestamp", TimestampDomain.class);
        domainClassMap.put("tinyint", ShortDomain.class);
        domainClassMap.put("varbinary", BytesDomain.class);
        domainClassMap.put("varchar", StringDomain.class);

        fallbackDomainClassMap.put(Types.BIGINT, LongDomain.class);
        fallbackDomainClassMap.put(Types.BINARY, BytesDomain.class);
        fallbackDomainClassMap.put(Types.BIT, BooleanDomain.class);
        fallbackDomainClassMap.put(Types.BLOB, BlobDomain.class);
        fallbackDomainClassMap.put(Types.BOOLEAN, BooleanDomain.class);
        fallbackDomainClassMap.put(Types.CHAR, StringDomain.class);
        fallbackDomainClassMap.put(Types.CLOB, ClobDomain.class);
        fallbackDomainClassMap.put(Types.DATE, DateDomain.class);
        fallbackDomainClassMap.put(Types.DECIMAL, BigDecimalDomain.class);
        fallbackDomainClassMap.put(Types.DOUBLE, DoubleDomain.class);
        fallbackDomainClassMap.put(Types.FLOAT, FloatDomain.class);
        fallbackDomainClassMap.put(Types.INTEGER, IntegerDomain.class);
        fallbackDomainClassMap.put(Types.LONGNVARCHAR, StringDomain.class);
        fallbackDomainClassMap.put(Types.LONGVARBINARY, BytesDomain.class);
        fallbackDomainClassMap.put(Types.LONGVARCHAR, StringDomain.class);
        fallbackDomainClassMap.put(Types.NCHAR, StringDomain.class);
        fallbackDomainClassMap.put(Types.NCLOB, NClobDomain.class);
        fallbackDomainClassMap.put(Types.NUMERIC, BigDecimalDomain.class);
        fallbackDomainClassMap.put(Types.REAL, FloatDomain.class);
        fallbackDomainClassMap.put(Types.SMALLINT, ShortDomain.class);
        fallbackDomainClassMap.put(Types.TIME, TimeDomain.class);
        fallbackDomainClassMap.put(Types.TIMESTAMP, TimestampDomain.class);
        fallbackDomainClassMap.put(Types.TINYINT, ShortDomain.class);
        fallbackDomainClassMap.put(Types.VARBINARY, BytesDomain.class);
        fallbackDomainClassMap.put(Types.VARCHAR, StringDomain.class);
        fallbackDomainClassMap.put(Types.NVARCHAR, StringDomain.class);
    }

    @Override
    public String getName() {
        return "standard";
    }

    @Override
    public SqlNode transformSelectSqlNode(SqlNode original,
            SelectOptions options) {
        if (original == null) {
            throw new DomaIllegalArgumentException("original", original);
        }
        if (options == null) {
            throw new DomaIllegalArgumentException("options", options);
        }
        SqlNode transformed = original;
        if (options.getOffset() >= 0 || options.getLimit() >= 0) {
            transformed = toPagingSqlNode(transformed, options.getOffset(), options
                    .getLimit());
        }
        if (options.getForUpdateType() != null) {
            SelectForUpdateType forUpdateType = options.getForUpdateType();
            String[] aliases = options.getAliases();
            if (!supportsSelectForUpdate(forUpdateType, false)) {
                throw new JdbcException(MessageCode.DOMA2023, getName());
            }
            if (aliases.length > 0) {
                if (!supportsSelectForUpdate(forUpdateType, true)) {
                    throw new JdbcException(MessageCode.DOMA2024, getName());
                }
            }
            transformed = toForUpdateSqlNode(transformed, forUpdateType, options
                    .getWaitSeconds(), aliases);
        }
        return transformed;
    }

    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
        StandardPagingTransformer transformer = new StandardPagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode,
            SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        StandardForUpdateTransformer transformer = new StandardForUpdateTransformer(
                forUpdateType, waitSeconds, aliases);
        return transformer.transform(sqlNode);
    }

    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaIllegalArgumentException("sqlException", sqlException);
        }
        String state = getSQLState(sqlException);
        if (state != null && state.length() >= 2) {
            String code = state.substring(0, 2);
            return UNIQUE_CONSTRAINT_VIOLATION_STATE_CODES.contains(code);
        }
        return false;
    }

    protected String getSQLState(SQLException sqlException) {
        SQLException cause = getCauseSQLException(sqlException);
        return cause.getSQLState();
    }

    protected int getErrorCode(SQLException sqlException) {
        SQLException cause = getCauseSQLException(sqlException);
        return cause.getErrorCode();
    }

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
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "getResultSetType");
    }

    @Override
    public PreparedSql getIdentitySelectSql(String qualifiedTableName,
            String columnName) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "getIdentitySelectSql");
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
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
    public boolean isJdbcCommentAvailable() {
        return true;
    }

    public String getDefaultSchemaName(String userName) {
        return userName;
    }

    @Override
    public boolean isAutoIncrement(Connection connection, String catalogName,
            String schemaName, String tableName, String columnName)
            throws SQLException {
        if (connection == null) {
            throw new DomaIllegalArgumentException("connection", connection);
        }
        if (tableName == null) {
            throw new DomaIllegalArgumentException("tableName", tableName);
        }
        if (columnName == null) {
            throw new DomaIllegalArgumentException("columnName", columnName);
        }
        String fullTableName = TableUtil
                .buildFullTableName(catalogName, schemaName, tableName);
        String sql = "select " + columnName + " from " + fullTableName
                + " where 1 = 0";
        PreparedStatement preparedStatement = JdbcUtil
                .prepareStatement(connection, sql);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                ResultSetMetaData rsMetaData = resultSet.getMetaData();
                return rsMetaData.isAutoIncrement(1);
            } finally {
                JdbcUtil.close(resultSet, null);
            }
        } finally {
            JdbcUtil.close(preparedStatement, null);
        }
    }

    @Override
    public String getTableComment(Connection connection, String catalogName,
            String schemaName, String tableName) throws SQLException {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "getTableComment");
    }

    @Override
    public Map<String, String> getColumnCommentMap(Connection connection,
            String catalogName, String schemaName, String tableName)
            throws SQLException {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "getColumnCommentMap");
    }

    public SqlBlockContext createSqlBlockContext() {
        return new StandardSqlBlockContext();
    }

    public String getSqlBlockDelimiter() {
        return null;
    }

    /**
     * 標準の{@link SqlBlockContext}の実装クラスです。
     * 
     * @author taedium
     */
    public static class StandardSqlBlockContext implements SqlBlockContext {

        /** SQLブロックの開始を表すキーワードの連なりのリスト */
        protected List<List<String>> sqlBlockStartKeywordsList = new ArrayList<List<String>>();

        /** 追加されたキーワードの連なり */
        protected List<String> keywords = new ArrayList<String>();

        /** SQLブロックの内側の場合{@code true} */
        protected boolean inSqlBlock;

        public void addKeyword(String keyword) {
            if (!inSqlBlock) {
                keywords.add(keyword);
                check();
            }
        }

        /**
         * ブロックの内側かどうかチェックします。
         */
        protected void check() {
            for (List<String> startKeywords : sqlBlockStartKeywordsList) {
                if (startKeywords.size() > keywords.size()) {
                    continue;
                }
                for (int i = 0; i < startKeywords.size(); i++) {
                    String word1 = startKeywords.get(i);
                    String word2 = keywords.get(i);
                    inSqlBlock = word1.equalsIgnoreCase(word2);
                    if (!inSqlBlock) {
                        break;
                    }
                }
                if (inSqlBlock) {
                    break;
                }
            }
        }

        public boolean isInSqlBlock() {
            return inSqlBlock;
        }
    }

    @Override
    public Class<? extends Domain<?, ?>> getDomainClass(String typeName,
            int sqlType, int length, int precision, int scale) {
        Class<? extends Domain<?, ?>> domainClass = null;
        if (typeName != null) {
            domainClass = domainClassMap.get(typeName.toLowerCase());
            if (domainClass != null) {
                return domainClass;
            }
        }
        domainClass = fallbackDomainClassMap.get(sqlType);
        if (domainClass != null) {
            return domainClass;
        }
        throw new UnsupportedColumnTypeException(typeName, sqlType);
    }

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
            throw new JdbcException(MessageCode.DOMA2019, domain.getClass()
                    .getName());
        }
    }

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
            throw new JdbcException(MessageCode.DOMA2019, domain.getClass()
                    .getName());
        }

    }
}
