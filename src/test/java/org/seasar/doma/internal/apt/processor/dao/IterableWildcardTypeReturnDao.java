package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao(config = MyConfig.class)
public interface IterableWildcardTypeReturnDao {

  @Select
  List<Height<?>> select();
}
