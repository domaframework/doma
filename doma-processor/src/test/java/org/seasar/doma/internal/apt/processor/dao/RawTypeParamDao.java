package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface RawTypeParamDao {

  @SuppressWarnings("rawtypes")
  @Select
  int select(Height height);
}
