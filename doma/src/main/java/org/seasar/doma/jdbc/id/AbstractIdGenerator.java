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

import org.seasar.doma.internal.jdbc.command.Jdbcs;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AbstractIdGenerator implements IdGenerator {

    protected long getGeneratedValue(IdGenerationConfig config, Sql<?> sql) {
        JdbcLogger logger = config.getJdbcLogger();
        Connection connection = Jdbcs.getConnection(config.getDataSource());
        try {
            PreparedStatement preparedStatement = Jdbcs
                    .prepareStatement(connection, sql.getRawSql());
            try {
                logger.logSql(getClass().getName(), "getGeneratedId", sql);
                setupOptions(config, preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                return getGeneratedValue(config, resultSet);
            } catch (SQLException e) {
                throw new JdbcException(MessageCode.DOMA2018, e, config
                        .getEntity().__getName(), e);
            } finally {
                Jdbcs.close(preparedStatement, logger);
            }
        } finally {
            Jdbcs.close(connection, logger);
        }
    }

    protected void setupOptions(IdGenerationConfig config,
            PreparedStatement preparedStatement) throws SQLException {
        if (config.getFetchSize() > 0) {
            preparedStatement.setFetchSize(config.getFetchSize());
        }
        if (config.getMaxRows() > 0) {
            preparedStatement.setMaxRows(config.getMaxRows());
        }
        if (config.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(config.getQueryTimeout());
        }
    }

    protected long getGeneratedValue(IdGenerationConfig context,
            ResultSet resultSet) throws SQLException {
        JdbcLogger logger = context.getJdbcLogger();
        try {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new JdbcException(MessageCode.DOMA2017, context.getEntity()
                    .__getName());
        } catch (final SQLException e) {
            throw new JdbcException(MessageCode.DOMA2018, e, context
                    .getEntity().__getName(), e);
        } finally {
            Jdbcs.close(resultSet, logger);
        }
    }
}
