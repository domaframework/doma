package org.seasar.doma.internal.apt.processor.config;

import javax.sql.DataSource;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

@SuppressWarnings("deprecation")
@SingletonConfig
public class PublicConstructorConfig implements Config {

  public PublicConstructorConfig() {}

  public static PublicConstructorConfig singleton() {
    return new PublicConstructorConfig();
  }

  @Override
  public DataSource getDataSource() {
    return null;
  }

  @Override
  public Dialect getDialect() {
    return null;
  }
}
