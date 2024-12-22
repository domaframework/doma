package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.processor.entity.Emp2;

@Dao
interface PackagePrivateDao {

  @Update
  int update(Emp2 emp);
}
