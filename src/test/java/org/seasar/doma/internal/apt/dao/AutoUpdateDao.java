package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.entity.Emp;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface AutoUpdateDao {

  @Update
  int update(Emp entity);
}
