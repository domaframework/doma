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
