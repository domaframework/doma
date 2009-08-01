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
import java.sql.Types;
import java.util.Collections;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.domain.BlobDomain;
import org.seasar.doma.domain.BooleanDomain;
import org.seasar.doma.domain.BytesDomain;
import org.seasar.doma.domain.DoubleDomain;
import org.seasar.doma.domain.FloatDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.LongDomain;
import org.seasar.doma.domain.ShortDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.domain.TimeDomain;
import org.seasar.doma.domain.TimestampDomain;
import org.seasar.doma.internal.jdbc.dialect.PostgresForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.PostgresPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;

/**
 * @author taedium
 * 
 */
public class PostgresDialect extends StandardDialect {

    protected static final String UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE = "23505";

    protected static final JdbcType<ResultSet> RESULT_SET = new PostgresResultSetType();

    public PostgresDialect() {
        this(new PostgresJdbcMappingVisitor(),
                new PostgresSqlLogFormattingVisitor());
    }

    public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor);

        domainClassMap.put("bigserial", LongDomain.class);
        domainClassMap.put("bit", BytesDomain.class);
        domainClassMap.put("bool", BooleanDomain.class);
        domainClassMap.put("bpchar", StringDomain.class);
        domainClassMap.put("bytea", BytesDomain.class);
        domainClassMap.put("float4", FloatDomain.class);
        domainClassMap.put("float8", DoubleDomain.class);
        domainClassMap.put("int2", ShortDomain.class);
        domainClassMap.put("int4", IntegerDomain.class);
        domainClassMap.put("int8", LongDomain.class);
        domainClassMap.put("money", FloatDomain.class);
        domainClassMap.put("oid", BlobDomain.class);
        domainClassMap.put("serial", IntegerDomain.class);
        domainClassMap.put("text", StringDomain.class);
        domainClassMap.put("timestamptz", TimestampDomain.class);
        domainClassMap.put("timetz", TimeDomain.class);
        domainClassMap.put("varbit", BytesDomain.class);
        domainClassMap.put("varchar", StringDomain.class);
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
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
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
    public PreparedSql getIdentitySelectSql(String qualifiedTableName,
            String columnName) {
        if (qualifiedTableName == null) {
            throw new DomaNullPointerException("qualifiedTableName");
        }
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        StringBuilder buf = new StringBuilder(64);
        buf.append("select currval('");
        buf.append(qualifiedTableName);
        buf.append('_').append(columnName);
        buf.append("_seq')");
        String rawSql = buf.toString();
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaNullPointerException("qualifiedSequenceName");
        }
        String rawSql = "select nextval('" + qualifiedSequenceName + "')";
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
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
    public boolean supportsSelectForUpdate(SelectForUpdateType type,
            boolean withTargets) {
        return type == SelectForUpdateType.NORMAL;
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
    public String getDefaultSchemaName(String userName) {
        return null;
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new PostgresSqlBlockContext();
    }

    public static class PostgresResultSetType extends AbstractResultSetType {

        public PostgresResultSetType() {
            super(Types.OTHER);
        }
    }

    public static class PostgresJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {
    }

    public static class PostgresSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {
    }

    public static class PostgresSqlBlockContext implements SqlBlockContext {

        protected boolean inSqlBlock;

        public void addKeyword(String keyword) {
            if ("$$".equals(keyword)) {
                inSqlBlock = !inSqlBlock;
            }
        }

        public boolean isInSqlBlock() {
            return inSqlBlock;
        }
    }

}
