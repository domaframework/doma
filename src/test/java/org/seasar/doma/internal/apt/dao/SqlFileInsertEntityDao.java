package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.internal.apt.entity.ParentEntity;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface SqlFileInsertEntityDao {

  @Insert(sqlFile = true)
  int insert(Emp emp);

  @Insert(sqlFile = true)
  int insertMultipleEntities(Emp emp, ParentEntity parentEntity);
}
