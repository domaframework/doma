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

  protected final SqlFileRepository sqlFileRepository = new GreedyCacheSqlFileRepository();

  protected final JdbcLogger sqlLogger = new UtilLoggingJdbcLogger();

  protected final RequiresNewController requiresNewController = new RequiresNewController() {};

  protected final ClassHelper classHelper = new ClassHelper() {};

  protected final CommandImplementors commandImplementors = new CommandImplementors() {};

  protected final QueryImplementors queryImplementors = new QueryImplementors() {};

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
