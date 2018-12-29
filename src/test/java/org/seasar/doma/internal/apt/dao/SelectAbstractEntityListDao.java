package org.seasar.doma.internal.apt.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.entity.AbstractEntity;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface SelectAbstractEntityListDao {

  @Select
  List<AbstractEntity> selectAll();
}
