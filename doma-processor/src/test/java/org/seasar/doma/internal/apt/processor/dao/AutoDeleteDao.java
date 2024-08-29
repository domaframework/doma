package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface AutoDeleteDao {

  @Delete
  int delete(Emp entity);
}
