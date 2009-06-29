package org.seasar.doma.internal;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.domain.DomainVisitor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.SqlFileRepository;


/**
 * @author taedium
 * 
 */
public class RuntimeConfig implements Config {

    protected final DataSource dataSource;

    protected final String dataSourceName;

    protected final Dialect dialect;

    protected final NameConvention nameConvention;

    protected final DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor;

    protected final DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor;

    protected final SqlFileRepository sqlFileRepository;

    protected final JdbcLogger jdbcLogger;

    protected final int fetchSize;

    protected final int maxRows;

    protected final int queryTimeout;

    protected final int batchSize;

    public RuntimeConfig(Config originalConfig) {
        this(originalConfig, originalConfig.dataSource());
    }

    public RuntimeConfig(Config config, DataSource dataSource) {
        assertNotNull(config);
        assertNotNull(dataSource);
        assertNotNull(config.dataSourceName());
        assertNotNull(config.dialect());
        assertNotNull(config.nameConvention());
        assertNotNull(config.jdbcMappingVisitor());
        assertNotNull(config.sqlLogFormattingVisitor());
        assertNotNull(config.sqlFileRepository());
        assertNotNull(config.jdbcLogger());
        this.dataSource = dataSource;
        this.dataSourceName = config.dataSourceName();
        this.dialect = config.dialect();
        this.nameConvention = config.nameConvention();
        this.jdbcMappingVisitor = config.jdbcMappingVisitor();
        this.sqlLogFormattingVisitor = config.sqlLogFormattingVisitor();
        this.sqlFileRepository = config.sqlFileRepository();
        this.jdbcLogger = config.jdbcLogger();
        this.fetchSize = config.fetchSize();
        this.maxRows = config.maxRows();
        this.queryTimeout = config.queryTimeout();
        this.batchSize = config.batchSize();
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public String dataSourceName() {
        return dataSourceName;
    }

    public Dialect dialect() {
        return dialect;
    }

    public NameConvention nameConvention() {
        return nameConvention;
    }

    public DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    public DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    public SqlFileRepository sqlFileRepository() {
        return sqlFileRepository;
    }

    public JdbcLogger jdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public int fetchSize() {
        return fetchSize;
    }

    @Override
    public int maxRows() {
        return maxRows;
    }

    @Override
    public int queryTimeout() {
        return queryTimeout;
    }

    @Override
    public int batchSize() {
        return batchSize;
    }

}
