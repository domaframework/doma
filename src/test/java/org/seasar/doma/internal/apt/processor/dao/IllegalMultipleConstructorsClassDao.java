package org.seasar.doma.internal.apt.processor.dao;

import java.sql.Connection;
import javax.sql.DataSource;
import org.seasar.doma.Dao;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;

@Dao(config = MyConfig.class)
public abstract class IllegalMultipleConstructorsClassDao extends AbstractDao {

  public IllegalMultipleConstructorsClassDao(Config config) {
    super(config);
  }

  public IllegalMultipleConstructorsClassDao(Config config, Connection connection) {
    super(config, connection);
  }

  public IllegalMultipleConstructorsClassDao(Config config, DataSource dataSource) {
    super(config, dataSource);
  }
}
