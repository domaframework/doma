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
package org.seasar.doma.internal.jdbc.dao;

import java.sql.Connection;

import javax.sql.DataSource;

import org.seasar.doma.Dao;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.RuntimeConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigException;

/**
 * {@link Dao} が注釈されたインタフェースの実装クラスのための骨格実装です。
 * <p>
 * 
 * @author taedium
 * 
 */
public abstract class AbstractDao {

    /**
     * 実行時用の設定です。
     */
    protected final Config config;

    /**
     * 実行時用の設定を作成します。
     * 
     * @param config
     *            JDBCの設定
     * @throws DomaNullPointerException
     *             {@code config} が {@code null} の場合
     * @throws ConfigException
     *             {@code config} の メソッドのどれかが {@code null} を返す場合
     */
    public AbstractDao(Config config) {
        if (config == null) {
            new DomaNullPointerException("config");
        }
        validateConfig(config, null);
        this.config = new RuntimeConfig(config, config.getDataSource());
    }

    /**
     * 実行時用の設定を作成します。
     * 
     * @param config
     *            JDBCの設定
     * @throws DomaNullPointerException
     *             {@code config} が {@code null} の場合
     * @throws ConfigException
     *             {@code config} の メソッドのどれかが {@code null} を返す場合
     */
    public AbstractDao(Config config, Connection connection) {
        if (config == null) {
            new DomaNullPointerException("config");
        }
        if (connection == null) {
            new DomaNullPointerException("connection");
        }
        DataSource dataSource = new NeverClosedConnectionProvider(
                new NeverClosedConnection(connection));
        validateConfig(config, dataSource);
        this.config = new RuntimeConfig(config, dataSource);
    }

    /**
     * 実行時用の設定を作成します。
     * 
     * @param config
     *            JDBCの設定
     * @param dataSource
     *            データソース
     * @throws DomaNullPointerException
     *             {@code config} が {@code null} の場合
     * @throws ConfigException
     *             {@code config} の メソッドのどれかが {@code null} を返す場合
     */
    public AbstractDao(Config config, DataSource dataSource) {
        if (config == null) {
            new DomaNullPointerException("config");
        }
        if (dataSource == null) {
            new DomaNullPointerException("dataSource");
        }
        validateConfig(config, dataSource);
        this.config = new RuntimeConfig(config, dataSource);
    }

    private void validateConfig(Config config, DataSource dataSource) {
        if (dataSource == null) {
            if (config.getDataSource() == null) {
                throw new ConfigException(config.getClass().getName(),
                        "getDataSource");
            }
        }
        if (config.getDataSourceName() == null) {
            throw new ConfigException(config.getClass().getName(),
                    "getDataSourceName");
        }
        if (config.getDialect() == null) {
            throw new ConfigException(config.getClass().getName(), "getDialect");
        }
        if (config.getSqlFileRepository() == null) {
            throw new ConfigException(config.getClass().getName(),
                    "getSqlFileRepository");
        }
        if (config.getJdbcLogger() == null) {
            throw new ConfigException(config.getClass().getName(),
                    "getJdbcLogger");
        }
    }

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    protected DataSource getDataSource() {
        return config.getDataSource();
    }

    /**
     * この {@literal Data Access Object} の {@literal public} なメソッドの実行開始を記録します。
     * 
     * @param callerClassName
     *            クラス名前
     * @param callerMethodName
     *            メソッド名
     * @param args
     *            引数
     */
    protected void entering(String callerClassName, String callerMethodName,
            Object... args) {
        config.getJdbcLogger().logDaoMethodEntering(callerClassName,
                callerMethodName, args);
    }

    /**
     * この {@literal Data Access Object} の {@literal public} なメソッドの実行終了を記録します。
     * 
     * @param callerClassName
     *            クラス名
     * @param callerMethodName
     *            メソッド名
     * @param result
     *            結果の値
     */
    protected void exiting(String callerClassName, String callerMethodName,
            Object result) {
        config.getJdbcLogger().logDaoMethodExiting(callerClassName,
                callerMethodName, result);
    }

}
