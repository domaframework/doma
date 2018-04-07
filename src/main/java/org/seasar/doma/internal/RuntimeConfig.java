package org.seasar.doma.internal;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.sql.DataSource;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigException;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlTemplateRepository;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class RuntimeConfig implements Config {

  protected final Config config;

  protected final DataSource dataSource;

  public RuntimeConfig(Config originalConfig) {
    this(originalConfig, originalConfig.getDataSource());
  }

  public RuntimeConfig(Config config, DataSource dataSource) {
    assertNotNull(config);
    assertNotNull(dataSource);
    this.config = config;
    this.dataSource = dataSource;
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public String getDataSourceName() {
    return config.getDataSourceName();
  }

  @Override
  public Dialect getDialect() {
    return config.getDialect();
  }

  @Override
  public SqlTemplateRepository getSqlTemplateRepository() {
    return config.getSqlTemplateRepository();
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return config.getJdbcLogger();
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return config.getRequiresNewController();
  }

  @Override
  public ClassHelper getClassHelper() {
    return config.getClassHelper();
  }

  @Override
  public CommandImplementors getCommandImplementors() {
    return config.getCommandImplementors();
  }

  @Override
  public QueryImplementors getQueryImplementors() {
    return config.getQueryImplementors();
  }

  @Override
  public SqlLogType getExceptionSqlLogType() {
    return config.getExceptionSqlLogType();
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    return config.getUnknownColumnHandler();
  }

  @Override
  public TransactionManager getTransactionManager() {
    return config.getTransactionManager();
  }

  @Override
  public Commenter getCommenter() {
    return config.getCommenter();
  }

  @Override
  public Naming getNaming() {
    return config.getNaming();
  }

  @Override
  public MapKeyNaming getMapKeyNaming() {
    return config.getMapKeyNaming();
  }

  @Override
  public int getFetchSize() {
    return config.getFetchSize();
  }

  @Override
  public int getMaxRows() {
    return config.getMaxRows();
  }

  @Override
  public int getQueryTimeout() {
    return config.getQueryTimeout();
  }

  @Override
  public int getBatchSize() {
    return config.getBatchSize();
  }

  @Override
  public EntityListenerProvider getEntityListenerProvider() {
    var provider = config.getEntityListenerProvider();
    if (provider == null) {
      throw new ConfigException(config.getClass().getName(), "getEntityListenerProvider");
    }
    return provider;
  }
}
