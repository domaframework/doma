package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface NoConfigDaoExtendsDao extends NoConfigEmpDao {

  @Override
  @Select
  Emp selectById(Integer id, SelectOptions options);

  @Update
  int update2(Emp entity);
}
