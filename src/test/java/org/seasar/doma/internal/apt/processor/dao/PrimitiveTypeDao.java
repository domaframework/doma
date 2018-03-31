package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.*;

@Dao(config = MyConfig.class)
public interface PrimitiveTypeDao {

  @Select
  int selectById(int id);

  @Update(sqlFile = true)
  int update(int id);

  @Function
  int execute(@In int id);
}
