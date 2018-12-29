package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = MyConfig.class)
public interface IncludeAndExcludeDao {

  @Update(
      include = {"name", "salary"},
      exclude = {"salary"})
  int update(Emp emp);
}
