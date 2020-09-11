package org.seasar.doma.jdbc.criteria.mock;

import javax.sql.DataSource;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class MockConfig implements Config {

  public MockDataSource dataSource = new MockDataSource();

  public Dialect dialect = new StandardDialect();

  protected SqlFileRepository sqlFileRepository = new GreedyCacheSqlFileRepository();

  protected JdbcLogger sqlLogger = new UtilLoggingJdbcLogger();

  protected RequiresNewController requiresNewController = new RequiresNewController() {};

  protected ClassHelper classHelper = new ClassHelper() {};

  protected CommandImplementors commandImplementors = new CommandImplementors() {};

  protected QueryImplementors queryImplementors = new QueryImplementors() {};

  protected SqlLogType exceptionSqlLogType = SqlLogType.FORMATTED;

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public String getDataSourceName() {
    return "";
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public SqlFileRepository getSqlFileRepository() {
    return sqlFileRepository;
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return sqlLogger;
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return requiresNewController;
  }

  @Override
  public ClassHelper getClassHelper() {
    return classHelper;
  }

  @Override
  public CommandImplementors getCommandImplementors() {
    return commandImplementors;
  }

  @Override
  public QueryImplementors getQueryImplementors() {
    return queryImplementors;
  }

  @Override
  public SqlLogType getExceptionSqlLogType() {
    return SqlLogType.FORMATTED;
  }

  @Override
  public int getFetchSize() {
    return 0;
  }

  @Override
  public int getMaxRows() {
    return 0;
  }

  @Override
  public int getQueryTimeout() {
    return 0;
  }

  @Override
  public int getBatchSize() {
    return 10;
  }

  public MockDataSource getMockDataSource() {
    return dataSource;
  }

  public void setMockDataSource(MockDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setDialect(Dialect dialect) {
    this.dialect = dialect;
  }
}
