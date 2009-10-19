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

import org.seasar.doma.Dao;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.RuntimeConfig;

/**
 * {@link Dao}が注釈されたインタフェースの実装クラスのための骨格実装です。
 * <p>
 * 
 * @author taedium
 * 
 */
public abstract class DomaAbstractDao {

    /**
     * この {@literal Data Access Object} の実行時用の設定です。
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
    public DomaAbstractDao(Config config) {
        this(config, null);
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
    public DomaAbstractDao(Config config, DataSource dataSource) {
        if (config == null) {
            new DomaNullPointerException("config");
        }
        if (dataSource == null) {
            if (config.dataSource() == null) {
                throw new ConfigException(config.getClass().getName(),
                        "dataSource");
            }
        }
        if (config.dataSourceName() == null) {
            throw new ConfigException(config.getClass().getName(),
                    "dataSourceName");
        }
        if (config.dialect() == null) {
            throw new ConfigException(config.getClass().getName(), "dialect");
        }
        if (config.sqlFileRepository() == null) {
            throw new ConfigException(config.getClass().getName(),
                    "sqlFileRepository");
        }
        if (config.jdbcLogger() == null) {
            throw new ConfigException(config.getClass().getName(), "jdbcLogger");
        }
        this.config = new RuntimeConfig(config, dataSource != null ? dataSource
                : config.dataSource());
    }

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    protected DataSource getDataSource() {
        return config.dataSource();
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
        config.jdbcLogger().logDaoMethodEntering(callerClassName,
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
        config.jdbcLogger().logDaoMethodExiting(callerClassName,
                callerMethodName, result);
    }

}
