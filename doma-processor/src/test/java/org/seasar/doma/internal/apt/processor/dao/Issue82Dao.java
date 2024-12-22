package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.processor.entity.Emp2;

@Dao
public interface Issue82Dao {

  @Update(sqlFile = true)
  int update(Emp2 entity, String name);
}
