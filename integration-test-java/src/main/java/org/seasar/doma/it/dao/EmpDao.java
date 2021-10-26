package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Emp;

@Dao
public interface EmpDao {

  @Select
  List<Emp> selectAll();

  @Select
  Emp selectById(Integer id);

  @Update(sqlFile = true)
  int createTable();

  @Insert
  int insert(Emp e);

  @Update
  int update(Emp e);

  @Delete
  int delete(Emp e);
}
