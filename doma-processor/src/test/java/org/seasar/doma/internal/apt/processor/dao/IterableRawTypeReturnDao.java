package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface IterableRawTypeReturnDao {

  @SuppressWarnings("rawtypes")
  @Select
  List<Height> select();
}
