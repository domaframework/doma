package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp2;

@Dao
public interface NoTestLiteralDao {

  @Select
  Emp2 selectById(Integer id);
}
