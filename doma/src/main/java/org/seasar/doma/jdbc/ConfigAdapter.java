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
package org.seasar.doma.jdbc;

import javax.sql.DataSource;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * DIコンテナから注入されるなど外部の {@link Config} をアダプタするクラスです。
 * 
 * @author taedium
 * 
 */
public final class ConfigAdapter implements Config {

    /** DIコンテナから注入されるなど外部の {@link Config} */
    private final Config config;

    /**
     * インスタンスを構築します。
     * 
     * @param config
     *            DIコンテナから注入されるなど外部の {@link Config}
     * @throws DomaNullPointerException
     *             {@code config} が {@code null} の場合
     */
    public ConfigAdapter(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        this.config = config;
    }

    public int batchSize() {
        return config.batchSize();
    }

    public DataSource dataSource() {
        return config.dataSource();
    }

    public String dataSourceName() {
        return config.dataSourceName();
    }

    public Dialect dialect() {
        return config.dialect();
    }

    public int fetchSize() {
        return config.fetchSize();
    }

    public JdbcLogger jdbcLogger() {
        return config.jdbcLogger();
    }

    public int maxRows() {
        return config.maxRows();
    }

    public NamingConvention namingConvention() {
        return config.namingConvention();
    }

    public int queryTimeout() {
        return config.queryTimeout();
    }

    public RequiresNewController requiresNewController() {
        return config.requiresNewController();
    }

    public SqlFileRepository sqlFileRepository() {
        return config.sqlFileRepository();
    }

}
