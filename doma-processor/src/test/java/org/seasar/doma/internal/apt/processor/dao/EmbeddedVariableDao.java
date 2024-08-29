package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface EmbeddedVariableDao {

  @Select
  Emp select(String orderBy);
}
