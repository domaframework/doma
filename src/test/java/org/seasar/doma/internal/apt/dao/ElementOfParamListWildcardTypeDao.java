package org.seasar.doma.internal.apt.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.entity.Emp;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface ElementOfParamListWildcardTypeDao {

  @Select
  List<Emp> select(List<?> param);
}
