package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface NoTestLiteralDao {

  @Select
  Emp selectById(Integer id);
}
