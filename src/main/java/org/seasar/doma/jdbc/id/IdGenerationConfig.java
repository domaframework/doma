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
package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;

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
    protected final EntityType<?> entityType;

    /**
     * インスタンスを構築します。
     * 
     * @param config
     *            JDBCの設定
     * @param entityType
     *            識別子が属するエンティティ
     */
    public IdGenerationConfig(Config config, EntityType<?> entityType) {
        assertNotNull(config, entityType);
        this.config = config;
        this.entityType = entityType;
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

    public JdbcLogger getJdbcLogger() {
        return config.getJdbcLogger();
    }

    public RequiresNewController getRequiresNewController() {
        return config.getRequiresNewController();
    }

    public Naming getNaming() {
        return config.getNaming();
    }

    public int getFetchSize() {
        return config.getFetchSize();
    }

    public int getMaxRows() {
        return config.getMaxRows();
    }

    public int getQueryTimeout() {
        return config.getQueryTimeout();
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

}
