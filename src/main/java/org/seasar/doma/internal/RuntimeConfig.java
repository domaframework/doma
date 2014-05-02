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
package org.seasar.doma.internal;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ExceptionSqlLogType;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.TransactionManager;

/**
 * @author taedium
 * 
 */
public class RuntimeConfig implements Config {

    protected final Config config;

    protected final DataSource dataSource;

    public RuntimeConfig(Config originalConfig) {
        this(originalConfig, originalConfig.getDataSource());
    }

    public RuntimeConfig(Config config, DataSource dataSource) {
        assertNotNull(config);
        assertNotNull(dataSource);
        this.config = config;
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getDataSourceName() {
        return config.getDataSourceName();
    }

    @Override
    public Dialect getDialect() {
        return config.getDialect();
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return config.getSqlFileRepository();
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return config.getJdbcLogger();
    }

    @Override
    public RequiresNewController getRequiresNewController() {
        return config.getRequiresNewController();
    }

    @Override
    public ClassHelper getClassHelper() {
        return config.getClassHelper();
    }

    @Override
    public CommandImplementors getCommandImplementors() {
        return config.getCommandImplementors();
    }

    @Override
    public QueryImplementors getQueryImplementors() {
        return config.getQueryImplementors();
    }

    @Override
    public ExceptionSqlLogType getExceptionSqlLogType() {
        return config.getExceptionSqlLogType();
    }

    @Override
    public UnknownColumnHandler getUnknownColumnHandler() {
        return config.getUnknownColumnHandler();
    }

    @Override
    public TransactionManager getTransactionManager() {
        return config.getTransactionManager();
    }

    @Override
    public MapKeyNaming getMapKeyNaming() {
        return config.getMapKeyNaming();
    }

    @Override
    public int getFetchSize() {
        return config.getFetchSize();
    }

    @Override
    public int getMaxRows() {
        return config.getMaxRows();
    }

    @Override
    public int getQueryTimeout() {
        return config.getQueryTimeout();
    }

    @Override
    public int getBatchSize() {
        return config.getBatchSize();
    }

}
