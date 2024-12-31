package org.seasar.doma.it;

import java.util.Objects;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.QueryImplementors;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.ScriptFileLoader;
import org.seasar.doma.jdbc.SqlBuilderSettings;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class DelegatingConfig implements Config {
  private final Config delegatee;

  public DelegatingConfig(Config delegatee) {
    this.delegatee = Objects.requireNonNull(delegatee);
  }

  @Override
  public DataSource getDataSource() {
    return delegatee.getDataSource();
  }

  @Override
  public Dialect getDialect() {
    return delegatee.getDialect();
  }

  @Override
  public String getDataSourceName() {
    return delegatee.getDataSourceName();
  }

  @Override
  public SqlFileRepository getSqlFileRepository() {
    return delegatee.getSqlFileRepository();
  }

  @Override
  public ScriptFileLoader getScriptFileLoader() {
    return delegatee.getScriptFileLoader();
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return delegatee.getJdbcLogger();
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return delegatee.getRequiresNewController();
  }

  @Override
  public ClassHelper getClassHelper() {
    return delegatee.getClassHelper();
  }

  @Override
  public CommandImplementors getCommandImplementors() {
    return delegatee.getCommandImplementors();
  }

  @Override
  public QueryImplementors getQueryImplementors() {
    return delegatee.getQueryImplementors();
  }

  @Override
  public SqlLogType getExceptionSqlLogType() {
    return delegatee.getExceptionSqlLogType();
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    return delegatee.getUnknownColumnHandler();
  }

  @Override
  public Naming getNaming() {
    return delegatee.getNaming();
  }

  @Override
  public MapKeyNaming getMapKeyNaming() {
    return delegatee.getMapKeyNaming();
  }

  @Override
  public TransactionManager getTransactionManager() {
    return delegatee.getTransactionManager();
  }

  @Override
  public Commenter getCommenter() {
    return delegatee.getCommenter();
  }

  @Override
  public int getMaxRows() {
    return delegatee.getMaxRows();
  }

  @Override
  public int getFetchSize() {
    return delegatee.getFetchSize();
  }

  @Override
  public int getQueryTimeout() {
    return delegatee.getQueryTimeout();
  }

  @Override
  public int getBatchSize() {
    return delegatee.getBatchSize();
  }

  @Override
  public EntityListenerProvider getEntityListenerProvider() {
    return delegatee.getEntityListenerProvider();
  }

  @Override
  public SqlBuilderSettings getSqlBuilderSettings() {
    return delegatee.getSqlBuilderSettings();
  }
}
