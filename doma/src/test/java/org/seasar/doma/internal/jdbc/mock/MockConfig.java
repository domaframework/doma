package org.seasar.doma.internal.jdbc.mock;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.domain.DomainVisitor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.StandardJdbcLogger;
import org.seasar.doma.jdbc.StandardNameConvention;
import org.seasar.doma.jdbc.StandardSqlFileRepository;
import org.seasar.doma.jdbc.dialect.StandardDialect;


/**
 * @author taedium
 * 
 */
public class MockConfig implements Config {

    public MockDataSource dataSource = new MockDataSource();

    protected Dialect dialect = new StandardDialect();

    protected NameConvention nameConvention = new StandardNameConvention();

    protected SqlFileRepository sqlFileRepository = new StandardSqlFileRepository();

    protected DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor = new JdbcMappingVisitor();

    protected DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor = new SqlLogFormattingVisitor();

    protected JdbcLogger sqlLogger = new StandardJdbcLogger();

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public String dataSourceName() {
        return "";
    }

    @Override
    public Dialect dialect() {
        return dialect;
    }

    @Override
    public DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor() {
        return jdbcMappingVisitor;
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
    public DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    @Override
    public JdbcLogger jdbcLogger() {
        return sqlLogger;
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

    @Override
    public int batchSize() {
        return 10;
    }

    public MockDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MockDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public NameConvention getNameConverter() {
        return nameConvention;
    }

    public void setNameConverter(NameConvention nameConvention) {
        this.nameConvention = nameConvention;
    }

    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    public void setSqlFileRepository(SqlFileRepository sqlFileRepository) {
        this.sqlFileRepository = sqlFileRepository;
    }

    public DomainVisitor<Void, JdbcMappingFunction, SQLException> getJdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    public void setJdbcMappingVisitor(
            DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor) {
        this.jdbcMappingVisitor = jdbcMappingVisitor;
    }

    public DomainVisitor<String, Void, RuntimeException> getSqlLogFormattingVisitor() {
        return sqlLogFormattingVisitor;
    }

    public void setSqlLogFormattingVisitor(
            DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor) {
        this.sqlLogFormattingVisitor = sqlLogFormattingVisitor;
    }

    public JdbcLogger getSqlLogger() {
        return sqlLogger;
    }

    public void setSqlLogger(JdbcLogger sqlLogger) {
        this.sqlLogger = sqlLogger;
    }

}
