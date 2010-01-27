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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
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

    /** 識別子が属するエンティティに対応するテーブルの完全修飾名 */
    protected final String qualifiedTableName;

    /** 識別子にマッピングされたカラムの名前 */
    protected final String idColumnName;

    /**
     * インスタンスを構築します。
     * 
     * @param config
     *            JDBCの設定
     * @param entityType
     *            識別子が属するエンティティ
     */
    public IdGenerationConfig(Config config, EntityType<?> entityType) {
        this(config, entityType, entityType.getQualifiedTableName(), entityType
                .getGeneratedIdPropertyType().getColumnName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param config
     *            JDBCの設定
     * @param entityType
     *            識別子が属するエンティティ
     * @param qualifiedTableName
     *            識別子が属するエンティティに対応するテーブルの完全修飾名
     * @param idColumnName
     *            識別子にマッピングされたカラムの名前
     */
    protected IdGenerationConfig(Config config, EntityType<?> entityType,
            String qualifiedTableName, String idColumnName) {
        assertNotNull(config, entityType, qualifiedTableName, idColumnName);
        this.config = config;
        this.entityType = entityType;
        this.qualifiedTableName = qualifiedTableName;
        this.idColumnName = idColumnName;
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

    public String getQualifiedTableName() {
        return qualifiedTableName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

}
