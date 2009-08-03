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
package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.Entity;

/**
 * 識別子の生成に関する設定です。
 * 
 * @author taedium
 * 
 */
public class IdGenerationConfig {

    /** JDBCの設定 */
    protected final Config config;

    /** 識別子が属するエンティティ */
    protected final Entity<?> entity;

    /** 識別子が属するエンティティに対応するテーブルの完全修飾名 */
    protected final String qualifiedTableName;

    /** 識別子にマッピングされたカラムの名前 */
    protected final String idColumnName;

    /**
     * インスタンスを構築します。
     * 
     * @param config
     *            JDBCの設定
     * @param entity
     *            識別子が属するエンティティ
     * @param qualifiedTableName
     *            識別子が属するエンティティに対応するテーブルの完全修飾名
     * @param idColumnName
     *            識別子にマッピングされたカラムの名前
     */
    public IdGenerationConfig(Config config, Entity<?> entity,
            String qualifiedTableName, String idColumnName) {
        assertNotNull(config, entity, qualifiedTableName, idColumnName);
        this.config = config;
        this.entity = entity;
        this.qualifiedTableName = qualifiedTableName;
        this.idColumnName = idColumnName;
    }

    public DataSource getDataSource() {
        return config.dataSource();
    }

    public String getDataSourceName() {
        return config.dataSourceName();
    }

    public Dialect getDialect() {
        return config.dialect();
    }

    public JdbcLogger getJdbcLogger() {
        return config.jdbcLogger();
    }

    public RequiresNewController getRequiresNewController() {
        return config.requiresNewController();
    }

    public int getFetchSize() {
        return config.fetchSize();
    }

    public int getMaxRows() {
        return config.maxRows();
    }

    public int getQueryTimeout() {
        return config.queryTimeout();
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public String getQualifiedTableName() {
        return qualifiedTableName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

}
