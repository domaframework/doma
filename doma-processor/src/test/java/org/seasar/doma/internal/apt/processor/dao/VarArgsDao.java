package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface VarArgsDao {

  @Select
  String select(String name, String... keywords);
}
