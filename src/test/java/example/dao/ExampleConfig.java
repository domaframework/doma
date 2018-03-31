package example.dao;

import javax.sql.DataSource;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/** @author taedium */
public class ExampleConfig implements Config {

  protected MockDataSource dataSource = new MockDataSource();

  protected StandardDialect dialect = new StandardDialect();

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }
}
