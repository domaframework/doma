package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface NoDefaultConstructorDao {
  @Insert
  int insert(Emp emp);
}
