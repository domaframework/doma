package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.ParentEntity;

@Dao(config = MyConfig.class)
public interface SqlFileInsertEntityDao {

  @Insert(sqlFile = true)
  int insert(Emp emp);

  @Insert(sqlFile = true)
  int insertMultipleEntities(Emp emp, ParentEntity parentEntity);
}
