package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;

@Dao(config = MyConfig.class)
public interface IllegalSqlAnnotationDao {
  @Sql(value = "select * from emp", useFile = true)
  @Select
  int select();
}
