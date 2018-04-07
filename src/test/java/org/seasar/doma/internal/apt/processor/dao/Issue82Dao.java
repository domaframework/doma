package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;

@Dao(config = MyConfig.class)
public interface Issue82Dao {

  @Sql(useFile = true)
  @Update
  int update(Emp entity, String name);
}
