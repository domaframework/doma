/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it;

import java.lang.reflect.Method;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
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
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.query.ChunkedAutoBatchUpdateQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.jdbc.tx.TransactionManager;

/**
 * A {@link Config} decorator that constructs {@link ChunkedAutoBatchUpdateQuery} instead of {@link
 * AutoBatchUpdateQuery}, so batch updates execute one chunk of SQL at a time and keep memory
 * bounded for large entity lists.
 */
public class ChunkedBatchUpdateConfig implements Config {

  private final Config delegate;

  private final QueryImplementors queryImplementors;

  public ChunkedBatchUpdateConfig(Config delegate) {
    this.delegate = delegate;
    this.queryImplementors =
        new QueryImplementors() {
          @Override
          public <ENTITY> AutoBatchUpdateQuery<ENTITY> createAutoBatchUpdateQuery(
              Method method, EntityType<ENTITY> entityType) {
            return new ChunkedAutoBatchUpdateQuery<>(entityType);
          }
        };
  }

  @Override
  public DataSource getDataSource() {
    return delegate.getDataSource();
  }

  @Override
  public Dialect getDialect() {
    return delegate.getDialect();
  }

  @Override
  public String getDataSourceName() {
    return delegate.getDataSourceName();
  }

  @Override
  public SqlFileRepository getSqlFileRepository() {
    return delegate.getSqlFileRepository();
  }

  @Override
  public ScriptFileLoader getScriptFileLoader() {
    return delegate.getScriptFileLoader();
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return delegate.getJdbcLogger();
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return delegate.getRequiresNewController();
  }

  @Override
  public ClassHelper getClassHelper() {
    return delegate.getClassHelper();
  }

  @Override
  public CommandImplementors getCommandImplementors() {
    return delegate.getCommandImplementors();
  }

  @Override
  public QueryImplementors getQueryImplementors() {
    return queryImplementors;
  }

  @Override
  public SqlLogType getExceptionSqlLogType() {
    return delegate.getExceptionSqlLogType();
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    return delegate.getUnknownColumnHandler();
  }

  @Override
  public DuplicateColumnHandler getDuplicateColumnHandler() {
    return delegate.getDuplicateColumnHandler();
  }

  @Override
  public Naming getNaming() {
    return delegate.getNaming();
  }

  @Override
  public MapKeyNaming getMapKeyNaming() {
    return delegate.getMapKeyNaming();
  }

  @Override
  public TransactionManager getTransactionManager() {
    return delegate.getTransactionManager();
  }

  @Override
  public Commenter getCommenter() {
    return delegate.getCommenter();
  }

  @Override
  public int getMaxRows() {
    return delegate.getMaxRows();
  }

  @Override
  public int getFetchSize() {
    return delegate.getFetchSize();
  }

  @Override
  public int getQueryTimeout() {
    return delegate.getQueryTimeout();
  }

  @Override
  public int getBatchSize() {
    return delegate.getBatchSize();
  }

  @Override
  public EntityListenerProvider getEntityListenerProvider() {
    return delegate.getEntityListenerProvider();
  }

  @Override
  public SqlBuilderSettings getSqlBuilderSettings() {
    return delegate.getSqlBuilderSettings();
  }

  @Override
  public StatisticManager getStatisticManager() {
    return delegate.getStatisticManager();
  }
}
