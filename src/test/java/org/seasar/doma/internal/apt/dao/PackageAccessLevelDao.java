package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;

/** @author nakamura-to */
@Dao(config = MyConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface PackageAccessLevelDao {

  @Insert
  int insert(Emp emp);
}
