package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.sql.DataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityDesc;

/** A configuration for the identity generation. */
public class IdGenerationConfig {

  protected final Config config;

  protected final EntityDesc<?> entityDesc;

  protected final IdProvider idProvider;

  public IdGenerationConfig(Config config, EntityDesc<?> entityDesc) {
    this(config, entityDesc, new UnavailableIdProvider());
  }

  public IdGenerationConfig(Config config, EntityDesc<?> entityDesc, IdProvider idProvider) {
    assertNotNull(config, entityDesc, idProvider);
    this.config = config;
    this.entityDesc = entityDesc;
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

  public EntityDesc<?> getEntityDesc() {
    return entityDesc;
  }

  public IdProvider getIdProvider() {
    return idProvider;
  }

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
