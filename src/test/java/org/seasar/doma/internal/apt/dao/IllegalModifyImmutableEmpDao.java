package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.ImmutableEmp;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface IllegalModifyImmutableEmpDao {

  @Insert
  int insert(ImmutableEmp emp);
}
