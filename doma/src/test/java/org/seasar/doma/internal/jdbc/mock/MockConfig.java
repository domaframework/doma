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
package org.seasar.doma.internal.jdbc.mock;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.domain.DomainVisitor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.StandardJdbcLogger;
import org.seasar.doma.jdbc.StandardNameConvention;
import org.seasar.doma.jdbc.StandardRequiresNewController;
import org.seasar.doma.jdbc.StandardSqlFileRepository;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/**
 * @author taedium
 * 
 */
public class MockConfig implements Config {

    public MockDataSource dataSource = new MockDataSource();

    protected Dialect dialect = new StandardDialect();

    protected NameConvention nameConvention = new StandardNameConvention();

    protected SqlFileRepository sqlFileRepository = new StandardSqlFileRepository();

    protected DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor = new JdbcMappingVisitor();

    protected DomainVisitor<String, SqlLogFormattingFunction, RuntimeException> sqlLogFormattingVisitor = new SqlLogFormattingVisitor();

    protected JdbcLogger sqlLogger = new StandardJdbcLogger();

    protected RequiresNewController requiresNewController = new StandardRequiresNewController();

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public String dataSourceName() {
        return "";
    }

    @Override
    public Dialect dialect() {
        return dialect;
    }

    @Override
    public DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    @Override
    public NameConvention nameConvention() {
        return nameConvention;
    }

    @Override
    public SqlFileRepository sqlFileRepository() {
        return sqlFileRepository;
    }

    @Override
    public DomainVisitor<String, SqlLogFormattingFunction, RuntimeException> sqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    @Override
    public JdbcLogger jdbcLogger() {
        return sqlLogger;
    }

    @Override
    public RequiresNewController requiresNewController() {
        return requiresNewController;
    }

    @Override
    public int fetchSize() {
        return 0;
    }

    @Override
    public int maxRows() {
        return 0;
    }

    @Override
    public int queryTimeout() {
        return 0;
    }

    @Override
    public int batchSize() {
        return 10;
    }

    public MockDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MockDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public NameConvention getNameConvention() {
        return nameConvention;
    }

    public void setNameConvention(NameConvention nameConvention) {
        this.nameConvention = nameConvention;
    }

    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    public void setSqlFileRepository(SqlFileRepository sqlFileRepository) {
        this.sqlFileRepository = sqlFileRepository;
    }

    public DomainVisitor<Void, JdbcMappingFunction, SQLException> getJdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    public void setJdbcMappingVisitor(
            DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor) {
        this.jdbcMappingVisitor = jdbcMappingVisitor;
    }

    public DomainVisitor<String, SqlLogFormattingFunction, RuntimeException> getSqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    public void setSqlLogFormattingVisitor(
            DomainVisitor<String, SqlLogFormattingFunction, RuntimeException> sqlLogFormattingVisitor) {
        this.sqlLogFormattingVisitor = sqlLogFormattingVisitor;
    }

    public JdbcLogger getSqlLogger() {
        return sqlLogger;
    }

    public void setSqlLogger(JdbcLogger sqlLogger) {
        this.sqlLogger = sqlLogger;
    }

    public RequiresNewController getRequiresNewController() {
        return requiresNewController;
    }

    public void setRequiresNewController(
            RequiresNewController requiresNewController) {
        this.requiresNewController = requiresNewController;
    }

}
