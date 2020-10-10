package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Update;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface Issue82Dao {

  @Update(sqlFile = true)
  int update(Emp entity, String name);
}
