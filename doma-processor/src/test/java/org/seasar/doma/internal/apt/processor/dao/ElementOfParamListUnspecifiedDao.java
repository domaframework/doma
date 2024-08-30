package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface ElementOfParamListUnspecifiedDao {

  @Select
  List<Emp> select(@SuppressWarnings("rawtypes") List param);
}
