package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Update;

@Dao
interface PackagePrivateDao {

  @Update
  int update(Emp emp);
}
