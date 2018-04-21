package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;

@Dao(config = MyConfig.class)
public abstract class SqlAnnotationOnNonAbstractMethodDao extends AbstractDao {

  public SqlAnnotationOnNonAbstractMethodDao(Config config) {
    super(config);
  }

  @Sql("insert")
  public int insert() {
    return 0;
  }
}
