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

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.RuntimeConfig;


/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractDao {

    protected final Config config;

    public DomaAbstractDao(Config config, DataSource dataSource) {
        if (config == null) {
            new DomaIllegalArgumentException("config", config);
        }
        if (dataSource == null) {
            if (config.dataSource() == null) {
                throw new DomaConfigException(config.getClass().getName(),
                        "dataSource");
            }
        }
        if (config.dataSourceName() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "dataSourceName");
        }
        if (config.dialect() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "dialect");
        }
        if (config.nameConvention() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "nameConvention");
        }
        if (config.jdbcMappingVisitor() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "jdbcMappingVisitor");
        }
        if (config.sqlLogFormattingVisitor() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "sqlLogFormattingVisitor");
        }
        if (config.sqlFileRepository() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "sqlFileRepository");
        }
        if (config.jdbcLogger() == null) {
            throw new DomaConfigException(config.getClass().getName(),
                    "jdbcLogger");
        }
        this.config = new RuntimeConfig(config, dataSource != null ? dataSource
                : config.dataSource());
    }

    protected DataSource getDataSource() {
        return config.dataSource();
    }

    protected void entering(String callerClassName, String callerMethodName,
            Object... parameters) {
        config.jdbcLogger()
                .logMethodEntering(callerClassName, callerMethodName, parameters);
    }

    protected void exiting(String callerClassName, String callerMethodName,
            Object result) {
        config.jdbcLogger()
                .logMethodExiting(callerClassName, callerMethodName, result);
    }

}
