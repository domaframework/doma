package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Sql;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.ParentEntity;

@Dao(config = MyConfig.class)
public interface SqlTemplateInsertEntityDao {

  @Sql(useFile = true)
  @Insert
  int insert(Emp emp);

  @Sql(useFile = true)
  @Insert
  int insertMultipleEntities(Emp emp, ParentEntity parentEntity);
}
