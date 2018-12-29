package org.seasar.doma.internal.apt.dao;

import javax.sql.DataSource;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

public class PlainSingletonConfig implements Config {

  public static final PlainSingletonConfig INSTANCE = new PlainSingletonConfig();

  private PlainSingletonConfig() {}

  @Override
  public DataSource getDataSource() {
    return null;
  }

  @Override
  public Dialect getDialect() {
    return null;
  }
}
