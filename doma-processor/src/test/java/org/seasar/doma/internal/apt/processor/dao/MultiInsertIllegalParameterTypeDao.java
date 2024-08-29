package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface MultiInsertIllegalParameterTypeDao {

  @MultiInsert
  int insert(Emp entity);
}
