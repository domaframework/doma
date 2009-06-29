package example.dao;

import javax.sql.DataSource;

import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.dialect.StandardDialect;


/**
 * @author taedium
 * 
 */
public class ExampleConfig extends DomaAbstractConfig {

    protected MockDataSource dataSource = new MockDataSource();

    protected StandardDialect dialect = new StandardDialect();

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public Dialect dialect() {
        return dialect;
    }

}
