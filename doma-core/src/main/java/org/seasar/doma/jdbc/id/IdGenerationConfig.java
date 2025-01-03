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
package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.sql.DataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/** A configuration for the identity generation. */
public class IdGenerationConfig {

  protected final Config config;

  protected final EntityType<?> entityType;

  @SuppressWarnings("deprecation")
  protected final IdProvider idProvider;

  public IdGenerationConfig(Config config, EntityType<?> entityType) {
    this(config, entityType, new UnavailableIdProvider());
  }

  @Deprecated
  public IdGenerationConfig(Config config, EntityType<?> entityType, IdProvider idProvider) {
    assertNotNull(config, entityType, idProvider);
    this.config = config;
    this.entityType = entityType;
    this.idProvider = idProvider;
  }

  public DataSource getDataSource() {
    return config.getDataSource();
  }

  public String getDataSourceName() {
    return config.getDataSourceName();
  }

  public Dialect getDialect() {
    return config.getDialect();
  }

  public JdbcLogger getJdbcLogger() {
    return config.getJdbcLogger();
  }

  public RequiresNewController getRequiresNewController() {
    return config.getRequiresNewController();
  }

  public Naming getNaming() {
    return config.getNaming();
  }

  public int getFetchSize() {
    return config.getFetchSize();
  }

  public int getMaxRows() {
    return config.getMaxRows();
  }

  public int getQueryTimeout() {
    return config.getQueryTimeout();
  }

  public EntityType<?> getEntityType() {
    return entityType;
  }

  public StatisticManager statisticManager() {
    return config.getStatisticManager();
  }

  @SuppressWarnings("deprecation")
  public IdProvider getIdProvider() {
    return idProvider;
  }

  @SuppressWarnings("DeprecatedIsStillUsed")
  @Deprecated
  protected static class UnavailableIdProvider implements IdProvider {
    @Override
    public boolean isAvailable() {
      return false;
    }

    @Override
    public long get() {
      throw new UnsupportedOperationException();
    }
  }
}
