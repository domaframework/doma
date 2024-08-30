package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface ElementOfParamListWildcardTypeDao {

  @Select
  List<Emp> select(List<?> param);
}
