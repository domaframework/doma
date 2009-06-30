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

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.domain.DomainVisitor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;

/**
 * @author taedium
 * 
 */
public class RuntimeConfig implements Config {

    protected final DataSource dataSource;

    protected final String dataSourceName;

    protected final Dialect dialect;

    protected final NameConvention nameConvention;

    protected final DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor;

    protected final DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor;

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
        assertNotNull(config.nameConvention());
        assertNotNull(config.jdbcMappingVisitor());
        assertNotNull(config.sqlLogFormattingVisitor());
        assertNotNull(config.sqlFileRepository());
        assertNotNull(config.jdbcLogger());
        assertNotNull(config.requiresNewController());
        this.dataSource = dataSource;
        this.dataSourceName = config.dataSourceName();
        this.dialect = config.dialect();
        this.nameConvention = config.nameConvention();
        this.jdbcMappingVisitor = config.jdbcMappingVisitor();
        this.sqlLogFormattingVisitor = config.sqlLogFormattingVisitor();
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

    public NameConvention nameConvention() {
        return nameConvention;
    }

    public DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    public DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
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
