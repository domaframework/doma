package org.seasar.doma.internal.apt.processor.config;

import javax.sql.DataSource;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

@SuppressWarnings("deprecation")
@SingletonConfig
public class MethodNotFoundConfig implements Config {

  private MethodNotFoundConfig() {}

  @Override
  public DataSource getDataSource() {
    return null;
  }

  @Override
  public Dialect getDialect() {
    return null;
  }
}
