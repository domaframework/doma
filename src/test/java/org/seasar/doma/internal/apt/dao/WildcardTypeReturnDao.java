package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface WildcardTypeReturnDao {

  @Select
  Height<?> select();
}
