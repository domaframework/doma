package org.seasar.doma.jdbc;

import java.sql.SQLException;

import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractConfig implements Config {

    protected static NameConvention nameConvention = new StandardNameConvention();

    protected static SqlFileRepository sqlFileRepository = new StandardSqlFileRepository();

    protected static JdbcLogger jdbcLogger = new StandardJdbcLogger();

    protected static RequiresNewController requiresNewController = new StandardRequiresNewController();

    protected static DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor = new JdbcMappingVisitor();

    protected static DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor = new SqlLogFormattingVisitor();

    @Override
    public String dataSourceName() {
        return getClass().getName();
    }

    @Override
    public NameConvention nameConvention() {
        return nameConvention;
    }

    @Override
    public SqlFileRepository sqlFileRepository() {
        return sqlFileRepository;
    }

    @Override
    public DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    @Override
    public DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    @Override
    public JdbcLogger jdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public RequiresNewController requiresNewController() {
        return requiresNewController;
    }

    @Override
    public int fetchSize() {
        return 0;
    }

    @Override
    public int maxRows() {
        return 0;
    }

    @Override
    public int queryTimeout() {
        return 0;
    }

    public int batchSize() {
        return 10;
    }
}
