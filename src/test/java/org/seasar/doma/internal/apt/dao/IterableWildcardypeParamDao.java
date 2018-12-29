package org.seasar.doma.internal.apt.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao(config = MyConfig.class)
public interface IterableWildcardypeParamDao {

  @Select
  int select(List<Height<?>> heightList);
}
