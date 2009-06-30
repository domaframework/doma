package org.seasar.doma.internal.jdbc.id;

import static org.seasar.doma.internal.util.Assertions.*;

import javax.sql.DataSource;

import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;

/**
 * @author taedium
 * 
 */
public class IdGenerationConfig {

    protected final Config config;

    protected final Entity<?> entity;

    protected final String qualifiedTableName;

    protected final String idColumnName;

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
