package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface EmptySqlFileDao {

  @Select
  Emp select();
}
