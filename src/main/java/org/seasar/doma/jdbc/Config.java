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
package org.seasar.doma.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.message.Message;

/**
 * A runtime configuration for DAOs.
 * <p>
 * The implementation must be thread safe.
 */
public interface Config {

    /**
     * Returns the data source.
     * 
     * @return the data source
     */
    DataSource getDataSource();

    /**
     * Returns the SQL dialect.
     * 
     * @return the SQL dialect
     */
    Dialect getDialect();

    /**
     * Returns the name of the data source.
     * <p>
     * Each data source must have an unique name when multiple data sources are
     * used in an application.
     * 
     * @return the name of the data source
     */
    default String getDataSourceName() {
        return getClass().getName();
    }

    /**
     * Returns the SQL file repository.
     * 
     * @return the SQL file repository
     */
    default SqlFileRepository getSqlFileRepository() {
        return ConfigSupport.defaultSqlFileRepository;
    }

    /**
     * Returns the JDBC logger.
     * 
     * @return the JDBC logger
     */
    default JdbcLogger getJdbcLogger() {
        return ConfigSupport.defaultJdbcLogger;
    }

    /**
     * Returns the transaction controller whose transaction attribute is
     * {@code REQUIRES_NEW}.
     * 
     * @return the transaction controller whose transaction attribute is
     *         {@code REQUIRES_NEW}
     */
    default RequiresNewController getRequiresNewController() {
        return ConfigSupport.defaultRequiresNewController;
    }

    /**
     * Returns the class helper.
     * 
     * @return the class helper
     */
    default ClassHelper getClassHelper() {
        return ConfigSupport.defaultClassHelper;
    }

    /**
     * Returns the factory for {@link Command} implementation classes.
     * 
     * @return the factory for {@link Command} implementation classes
     */
    default CommandImplementors getCommandImplementors() {
        return ConfigSupport.defaultCommandImplementors;
    }

    /**
     * Returns the factory for {@link Query} implementation classes.
     * 
     * @return the factory for {@link Query} implementation classes
     */
    default QueryImplementors getQueryImplementors() {
        return ConfigSupport.defaultQueryImplementors;
    }

    /**
     * Returns the SQL log type that determines the SQL log format in
     * exceptions.
     * 
     * @return the SQL log type
     */
    default SqlLogType getExceptionSqlLogType() {
        return SqlLogType.FORMATTED;
    }

    /**
     * Returns the unknown column handler.
     * 
     * @return the unknown column handler
     */
    default UnknownColumnHandler getUnknownColumnHandler() {
        return ConfigSupport.defaultUnknownColumnHandler;
    }

    /**
     * Returns the naming convention controller.
     * 
     * @return the naming convention controller
     */
    default Naming getNaming() {
        return ConfigSupport.defaultNaming;
    }

    /**
     * Returns a naming convention controller for keys contained in a
     * {@code Map<String, Object>} object.
     * 
     * @return a naming convention controller for keys contained in a
     *         {@code Map<String, Object>} object
     */
    default MapKeyNaming getMapKeyNaming() {
        return ConfigSupport.defaultMapKeyNaming;
    }

    /**
     * Returns the transaction manager.
     * 
     * @return the transaction manager
     * @throws UnsupportedOperationException
     *             if this configuration does not support transactions by the
     *             transaction manager
     */
    default TransactionManager getTransactionManager() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the commenter for SQL strings.
     * 
     * @return the commenter for SQL strings
     */
    default Commenter getCommenter() {
        return ConfigSupport.defaultCommenter;
    }

    /**
     * Returns the maximum number of rows for a {@code ResultSet} object.
     * <p>
     * If the value is greater than or equal to 1, it is passed to
     * {@link Statement#setMaxRows(int)}.
     * 
     * @return the maximum number of rows
     * @see Statement#setMaxRows(int)
     */
    default int getMaxRows() {
        return 0;
    }

    /**
     * Returns the fetch size.
     * <p>
     * If the value is greater than or equal to 1, it is passed to
     * {@link Statement#setFetchSize(int)}.
     * 
     * @return the fetch size
     * @see Statement#setFetchSize(int)
     */
    default int getFetchSize() {
        return 0;
    }

    /**
     * Returns the query timeout limit in seconds.
     * <p>
     * If the value is greater than or equal to 1, it is passed to
     * {@link Statement#setQueryTimeout(int)}.
     * 
     * @return the query timeout limit in seconds
     * @see Statement#setQueryTimeout(int)
     */
    default int getQueryTimeout() {
        return 0;
    }

    /**
     * Returns the batch size.
     * <p>
     * If the value is less than 1, it is regarded as 1.
     * 
     * @return the batch size
     * @see PreparedStatement#executeBatch()
     * @see PreparedStatement#addBatch()
     */
    default int getBatchSize() {
        return 0;
    }

    /**
     * Returns the provider for {@link EntityListener}.
     * 
     * @return the provider for {@link EntityListener}
     */
    default EntityListenerProvider getEntityListenerProvider() {
        return ConfigSupport.defaultEntityListenerProvider;
    }

    /**
     * Retrieves a {@link Config} object from the {@code provider} parameter.
     * 
     * @param provider
     *            the instance of {@link ConfigProvider}
     * @return the configuration
     * @throws DomaIllegalArgumentException
     *             if {@code provider} is not {@link ConfigProvider}
     */
    static Config get(Object provider) {
        if (provider instanceof ConfigProvider) {
            ConfigProvider p = (ConfigProvider) provider;
            return p.getConfig();
        }
        throw new DomaIllegalArgumentException("provider",
                Message.DOMA2218.getMessage("provider", ConfigProvider.class.getName()));
    }
}
