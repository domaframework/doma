package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Procedure;
import org.seasar.doma.Sql;

@Dao(config = MyConfig.class)
public interface IllegalSqlAnnotationCombinationDao {
  @Sql
  @Procedure
  int select();
}
