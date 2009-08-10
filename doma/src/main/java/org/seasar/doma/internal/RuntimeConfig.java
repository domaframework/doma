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
import org.seasar.doma.jdbc.NamingConvention;
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

    protected final NamingConvention namingConvention;

    protected final SqlFileRepository sqlFileRepository;

    protected final JdbcLogger jdbcLogger;

    protected final RequiresNewController requiresNewController;

    protected final int fetchSize;

    protected final int maxRows;

    protected final int queryTimeout;

    protected final int batchSize;

    public RuntimeConfig(Config originalConfig) {
        this(originalConfig, originalConfig.dataSource());
    }

    public RuntimeConfig(Config config, DataSource dataSource) {
        assertNotNull(config);
        assertNotNull(dataSource);
        assertNotNull(config.dataSourceName());
        assertNotNull(config.dialect());
        assertNotNull(config.namingConvention());
        assertNotNull(config.sqlFileRepository());
        assertNotNull(config.jdbcLogger());
        assertNotNull(config.requiresNewController());
        this.dataSource = dataSource;
        this.dataSourceName = config.dataSourceName();
        this.dialect = config.dialect();
        this.namingConvention = config.namingConvention();
        this.sqlFileRepository = config.sqlFileRepository();
        this.jdbcLogger = config.jdbcLogger();
        this.requiresNewController = config.requiresNewController();
        this.fetchSize = config.fetchSize();
        this.maxRows = config.maxRows();
        this.queryTimeout = config.queryTimeout();
        this.batchSize = config.batchSize();
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public String dataSourceName() {
        return dataSourceName;
    }

    public Dialect dialect() {
        return dialect;
    }

    public NamingConvention namingConvention() {
        return namingConvention;
    }

    public SqlFileRepository sqlFileRepository() {
        return sqlFileRepository;
    }

    public JdbcLogger jdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public RequiresNewController requiresNewController() {
        return requiresNewController;
    }

    @Override
    public int fetchSize() {
        return fetchSize;
    }

    @Override
    public int maxRows() {
        return maxRows;
    }

    @Override
    public int queryTimeout() {
        return queryTimeout;
    }

    @Override
    public int batchSize() {
        return batchSize;
    }

}
