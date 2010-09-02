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
package org.seasar.doma.jdbc.id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.message.Message;

/**
 * {@link IdGenerator} の骨格実装です。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractIdGenerator implements IdGenerator {

    /**
     * 生成された識別子を取得するSQLを実行します。
     * 
     * @param config
     *            識別子生成の設定
     * @param sql
     *            生成された識別子を取得するSQL
     * @return SQLにより取得された値
     * @throws JdbcException
     *             生成された識別子の取得に失敗した場合
     */
    protected long getGeneratedValue(IdGenerationConfig config, Sql<?> sql) {
        JdbcLogger logger = config.getJdbcLogger();
        Connection connection = JdbcUtil.getConnection(config.getDataSource());
        try {
            PreparedStatement preparedStatement = JdbcUtil.prepareStatement(
                    connection, sql);
            try {
                logger.logSql(getClass().getName(), "getGeneratedId", sql);
                setupOptions(config, preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                return getGeneratedValue(config, resultSet);
            } catch (SQLException e) {
                throw new JdbcException(Message.DOMA2018, e, config
                        .getEntityType().getName(), e);
            } finally {
                JdbcUtil.close(preparedStatement, logger);
            }
        } finally {
            JdbcUtil.close(connection, logger);
        }
    }

    /**
     * {@code preparedStatement} に対しオプションの設定を行います。
     * 
     * @param config
     *            識別子生成の設定
     * @param preparedStatement
     *            準備された文
     * @throws SQLException
     *             SQL例外が発生した場合
     */
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

    /**
     * {@link ResultSet} から生成された識別子の値を取得します。
     * 
     * @param config
     *            識別子生成の設定
     * @param resultSet
     *            結果セット
     * @return 生成された識別子の値
     * @throws JdbcException
     *             識別子の取得に失敗した場合
     */
    protected long getGeneratedValue(IdGenerationConfig config,
            ResultSet resultSet) {
        JdbcLogger logger = config.getJdbcLogger();
        try {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new JdbcException(Message.DOMA2017, config.getEntityType()
                    .getName());
        } catch (final SQLException e) {
            throw new JdbcException(Message.DOMA2018, e, config.getEntityType()
                    .getName(), e);
        } finally {
            JdbcUtil.close(resultSet, logger);
        }
    }
}
