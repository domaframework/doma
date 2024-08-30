package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface SqlValidationSkipDao {

  @Select
  String selectById(String name);
}
