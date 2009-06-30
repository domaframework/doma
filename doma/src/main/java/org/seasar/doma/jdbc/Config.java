package org.seasar.doma.jdbc;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public interface Config {

    DataSource dataSource();

    String dataSourceName();

    Dialect dialect();

    NameConvention nameConvention();

    SqlFileRepository sqlFileRepository();

    DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor();

    DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor();

    JdbcLogger jdbcLogger();

    RequiresNewController requiresNewController();

    int maxRows();

    int fetchSize();

    int queryTimeout();

    int batchSize();

}
