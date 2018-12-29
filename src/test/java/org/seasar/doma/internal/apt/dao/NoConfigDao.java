package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;

/** @author taedium */
@Dao
public interface NoConfigDao {

  @Insert
  int insert(Emp emp);
}
