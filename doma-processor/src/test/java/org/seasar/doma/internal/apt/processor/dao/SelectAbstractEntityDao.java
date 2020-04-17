package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.AbstractEntity;

@Dao(config = MyConfig.class)
public interface SelectAbstractEntityDao {

  @Select
  AbstractEntity selectById(int id);
}
