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

import javax.sql.DataSource;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * {@link Config} のプロキシです。
 * <p>
 * 主に、DIコンテナからインジェクションされる {@link Config} をプロキシします。
 * 
 * @author taedium
 * 
 */
public final class ConfigProxy implements Config {

    /** DIコンテナから注入されるなど外部の {@link Config} */
    private final Config config;

    /**
     * インスタンスを構築します。
     * 
     * @param config
     *            プロキシの対象である {@link Config}
     * @throws DomaNullPointerException
     *             {@code config} が {@code null} の場合
     */
    public ConfigProxy(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        this.config = config;
    }

    public int getBatchSize() {
        return config.getBatchSize();
    }

    public DataSource getDataSource() {
        return config.getDataSource();
    }

    public String getDataSourceName() {
        return config.getDataSourceName();
    }

    public Dialect getDialect() {
        return config.getDialect();
    }

    public int getFetchSize() {
        return config.getFetchSize();
    }

    public JdbcLogger getJdbcLogger() {
        return config.getJdbcLogger();
    }

    public int getMaxRows() {
        return config.getMaxRows();
    }

    public int getQueryTimeout() {
        return config.getQueryTimeout();
    }

    public RequiresNewController getRequiresNewController() {
        return config.getRequiresNewController();
    }

    public SqlFileRepository getSqlFileRepository() {
        return config.getSqlFileRepository();
    }

}
