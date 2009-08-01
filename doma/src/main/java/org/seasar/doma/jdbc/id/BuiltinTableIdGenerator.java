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
package org.seasar.doma.jdbc.id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.seasar.doma.GenerationType;
import org.seasar.doma.domain.LongDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.JdbcUtil;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class BuiltinTableIdGenerator extends AbstractPreAllocateIdGenerator
        implements TableIdGenerator {

    protected String qualifiedTableName;

    protected String pkColumnName;

    protected String pkColumnValue;

    protected String valueColumnName;

    protected PreparedSql updateSql;

    protected PreparedSql selectSql;

    @Override
    public void setQualifiedTableName(String qualifiedTableName) {
        this.qualifiedTableName = qualifiedTableName;
    }

    @Override
    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    @Override
    public void setPkColumnValue(String pkColumnValue) {
        this.pkColumnValue = pkColumnValue;
    }

    @Override
    public void setValueColumnName(String valueColumnName) {
        this.valueColumnName = valueColumnName;
    }

    @Override
    public void initialize() {
        if (qualifiedTableName == null) {
            throw new JdbcException(DomaMessageCode.DOMA2033,
                    "qualifiedTableName");
        }
        if (pkColumnName == null) {
            throw new JdbcException(DomaMessageCode.DOMA2033, "pkColumnName");
        }
        if (pkColumnValue == null) {
            throw new JdbcException(DomaMessageCode.DOMA2033, "pkColumnValue");
        }
        if (valueColumnName == null) {
            throw new JdbcException(DomaMessageCode.DOMA2033, "valueColumnName");
        }
        LongDomain allocationSizeDomain = new LongDomain(allocationSize);
        StringDomain pkColumnValueDomain = new StringDomain(pkColumnValue);
        updateSql = new PreparedSql(createUpdateRawSql(),
                createUpdateFormattedSql(), Arrays.asList(new InParameter(
                        allocationSizeDomain), new InParameter(
                        pkColumnValueDomain)));
        selectSql = new PreparedSql(createSelectRawSql(),
                createSelectFormattedSql(), Arrays.asList(new InParameter(
                        pkColumnValueDomain)));
    }

    protected String createUpdateRawSql() {
        StringBuilder buf = new StringBuilder(100);
        buf.append("update ");
        buf.append(qualifiedTableName);
        buf.append(" set ");
        buf.append(valueColumnName);
        buf.append(" = ");
        buf.append(valueColumnName);
        buf.append(" + ? where ");
        buf.append(pkColumnName);
        buf.append(" = ?");
        return buf.toString();
    }

    protected String createUpdateFormattedSql() {
        StringBuilder buf = new StringBuilder(100);
        buf.append("update ");
        buf.append(qualifiedTableName);
        buf.append(" set ");
        buf.append(valueColumnName);
        buf.append(" = ");
        buf.append(valueColumnName);
        buf.append(" + ");
        buf.append(allocationSize);
        buf.append(" where ");
        buf.append(pkColumnName);
        buf.append(" = '");
        buf.append(pkColumnValue);
        buf.append("'");
        return buf.toString();
    }

    protected String createSelectRawSql() {
        StringBuilder buf = new StringBuilder(100);
        buf.append("select ");
        buf.append(valueColumnName);
        buf.append(" from ");
        buf.append(qualifiedTableName);
        buf.append(" where ");
        buf.append(pkColumnName);
        buf.append(" = ?");
        return new String(buf);
    }

    protected String createSelectFormattedSql() {
        StringBuilder buf = new StringBuilder(100);
        buf.append("select ");
        buf.append(valueColumnName);
        buf.append(" from ");
        buf.append(qualifiedTableName);
        buf.append(" where ");
        buf.append(pkColumnName);
        buf.append(" = '");
        buf.append(pkColumnValue);
        buf.append("'");
        return new String(buf);
    }

    @Override
    protected long getNewInitialValue(final IdGenerationConfig config) {
        RequiresNewController controller = config.getRequiresNewController();
        try {
            Long value = controller
                    .requiresNew(new RequiresNewController.Callback<Long>() {

                        @Override
                        public Long execute() {
                            updateId(config, updateSql);
                            return selectId(config, selectSql);
                        }
                    });
            return value - allocationSize;
        } catch (Throwable t) {
            throw new JdbcException(DomaMessageCode.DOMA2018, t, config
                    .getEntity().__getName(), t);
        }
    }

    protected void updateId(IdGenerationConfig config, PreparedSql sql) {
        JdbcLogger logger = config.getJdbcLogger();
        Connection connection = JdbcUtil.getConnection(config.getDataSource());
        try {
            PreparedStatement preparedStatement = JdbcUtil.prepareStatement(
                    connection, sql.getRawSql());
            try {
                logger.logSql(getClass().getName(), "updateId", sql);
                setupOptions(config, preparedStatement);
                preparedStatement.setLong(1, allocationSize);
                preparedStatement.setString(2, pkColumnValue);
                int rows = preparedStatement.executeUpdate();
                if (rows != 1) {
                    throw new JdbcException(DomaMessageCode.DOMA2017, config
                            .getEntity().__getName());
                }
            } catch (SQLException e) {
                throw new JdbcException(DomaMessageCode.DOMA2018, e, config
                        .getEntity().__getName(), e);
            } finally {
                JdbcUtil.close(preparedStatement, logger);
            }
        } finally {
            JdbcUtil.close(connection, logger);
        }
    }

    protected long selectId(IdGenerationConfig config, PreparedSql sql) {
        JdbcLogger logger = config.getJdbcLogger();
        Connection connection = JdbcUtil.getConnection(config.getDataSource());
        try {
            PreparedStatement preparedStatement = JdbcUtil.prepareStatement(
                    connection, sql.getRawSql());
            try {
                logger.logSql(getClass().getName(), "selectId", sql);
                setupOptions(config, preparedStatement);
                preparedStatement.setString(1, pkColumnValue);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Object result = resultSet.getObject(1);
                    if (result != null && Number.class.isInstance(result)) {
                        return Number.class.cast(result).longValue();
                    }
                }
                throw new JdbcException(DomaMessageCode.DOMA2017, config
                        .getEntity().__getName());
            } catch (SQLException e) {
                throw new JdbcException(DomaMessageCode.DOMA2018, e, config
                        .getEntity().__getName(), e);
            } finally {
                JdbcUtil.close(preparedStatement, logger);
            }
        } finally {
            JdbcUtil.close(connection, logger);
        }
    }

    @Override
    public GenerationType getGenerationType() {
        return GenerationType.TABLE;
    }

}
