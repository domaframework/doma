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
package org.seasar.doma.internal;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public class RuntimeConfig implements Config {

    protected final DataSource dataSource;

    protected final String dataSourceName;

    protected final Dialect dialect;

    protected final SqlFileRepository sqlFileRepository;

    protected final JdbcLogger jdbcLogger;

    protected final RequiresNewController requiresNewController;

    protected final int fetchSize;

    protected final int maxRows;

    protected final int queryTimeout;

    protected final int batchSize;

    public RuntimeConfig(Config originalConfig) {
        this(originalConfig, originalConfig.getDataSource());
    }

    public RuntimeConfig(Config config, DataSource dataSource) {
        assertNotNull(config);
        assertNotNull(dataSource);
        assertNotNull(config.getDataSourceName());
        assertNotNull(config.getDialect());
        assertNotNull(config.getSqlFileRepository());
        assertNotNull(config.getJdbcLogger());
        assertNotNull(config.getRequiresNewController());
        this.dataSource = dataSource;
        this.dataSourceName = config.getDataSourceName();
        this.dialect = config.getDialect();
        this.sqlFileRepository = config.getSqlFileRepository();
        this.jdbcLogger = config.getJdbcLogger();
        this.requiresNewController = config.getRequiresNewController();
        this.fetchSize = config.getFetchSize();
        this.maxRows = config.getMaxRows();
        this.queryTimeout = config.getQueryTimeout();
        this.batchSize = config.getBatchSize();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getDataSourceName() {
        return dataSourceName;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    public JdbcLogger getJdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public RequiresNewController getRequiresNewController() {
        return requiresNewController;
    }

    @Override
    public int getFetchSize() {
        return fetchSize;
    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

}
