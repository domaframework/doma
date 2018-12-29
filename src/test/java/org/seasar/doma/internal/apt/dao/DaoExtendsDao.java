package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

@Dao(config = MyConfig.class)
public interface DaoExtendsDao extends EmpDao {

  @Override
  @Select
  public Emp selectById(Integer id, SelectOptions options);

  @Update
  public int update2(Emp entity);
}
