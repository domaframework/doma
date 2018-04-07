package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Sql;

@Dao(config = MyConfig.class)
public interface SqlAnnotationOnDefaultMethodDao {
  @Sql(value = "select * from emp")
  default int select() {
    return 0;
  }
}
