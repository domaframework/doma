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
package org.seasar.doma.internal.jdbc.mock;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DefaultClassHelper;
import org.seasar.doma.jdbc.ExceptionSqlLogType;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.NullRequiresNewController;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/**
 * @author taedium
 * 
 */
public class MockConfig implements Config {

    public MockDataSource dataSource = new MockDataSource();

    public Dialect dialect = new StandardDialect();

    protected SqlFileRepository sqlFileRepository = new GreedyCacheSqlFileRepository();

    protected JdbcLogger sqlLogger = new UtilLoggingJdbcLogger();

    protected RequiresNewController requiresNewController = new NullRequiresNewController();

    protected ClassHelper classHelper = new DefaultClassHelper();

    protected ExceptionSqlLogType exceptionSqlLogType = ExceptionSqlLogType.FORMATTED_SQL;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getDataSourceName() {
        return "";
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return sqlLogger;
    }

    @Override
    public RequiresNewController getRequiresNewController() {
        return requiresNewController;
    }

    @Override
    public ClassHelper getClassHelper() {
        return classHelper;
    }

    @Override
    public ExceptionSqlLogType getExceptionSqlLogType() {
        return ExceptionSqlLogType.FORMATTED_SQL;
    }

    @Override
    public int getFetchSize() {
        return 0;
    }

    @Override
    public int getMaxRows() {
        return 0;
    }

    @Override
    public int getQueryTimeout() {
        return 0;
    }

    @Override
    public int getBatchSize() {
        return 10;
    }

    @Override
    public boolean ignoreUnknownColumn() {
        return false;
    }

    public MockDataSource getMockDataSource() {
        return dataSource;
    }

    public void setMockDataSource(MockDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

}
