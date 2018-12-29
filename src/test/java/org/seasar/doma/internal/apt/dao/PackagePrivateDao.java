package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Update;

@Dao(config = MyConfig.class)
interface PackagePrivateDao {

  @Update
  int update(Emp emp);
}
