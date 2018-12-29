package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

@Dao(config = MyConfig.class)
public interface PrimitiveTypeDao {

  @Select
  int selectById(int id);

  @Update(sqlFile = true)
  int update(int id);

  @Function
  int execute(@In int id);
}
