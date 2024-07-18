package org.seasar.doma.it;

import java.util.Objects;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;

public class AppConfig implements Config {

  private final Dialect dialect;

  private final Dbms dbms;

  private final JdbcLogger jdbcLogger;

  private final DataSource originalDataSource;

  private final LocalTransactionDataSource dataSource;

  private final LocalTransactionManager transactionManager;

  public AppConfig(Dialect dialect, Dbms dbms, String url, String user, String password) {
    Objects.requireNonNull(dialect);
    Objects.requireNonNull(dbms);
    Objects.requireNonNull(url);
    Objects.requireNonNull(user);
    Objects.requireNonNull(password);
    this.dialect = dialect;
    this.dbms = dbms;
    jdbcLogger = new Slf4jJdbcLogger();
    originalDataSource = createDataSource(url, user, password);
    dataSource = new LocalTransactionDataSource(originalDataSource);
    transactionManager = new LocalTransactionManager(dataSource.getLocalTransaction(jdbcLogger));
  }

  private DataSource createDataSource(String url, String user, String password) {
    SimpleDataSource dataSource = new SimpleDataSource();
    dataSource.setUrl(url);
    dataSource.setUser(user);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public String getDataSourceName() {
    return dbms.name();
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return new RequiresNewController() {
      @Override
      public <R> R requiresNew(Callback<R> callback) throws Throwable {
        return transactionManager.requiresNew(callback::execute);
      }
    };
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  public LocalTransaction getLocalTransaction() {
    return dataSource.getLocalTransaction(getJdbcLogger());
  }

  public Dbms getDbms() {
    return dbms;
  }

  public DataSource getOriginalDataSource() {
    return originalDataSource;
  }

  @Override
  public Naming getNaming() {
    return Naming.SNAKE_UPPER_CASE;
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return jdbcLogger;
  }
}
