package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

@Dao
public interface DeptDao {

  @Select
  Dept selectById(Integer departmentId);

  @Insert
  Result<Dept> insert(Dept d);

  @Update
  Result<Dept> update(Dept d);

  @BatchInsert
  BatchResult<Dept> insert(List<Dept> entity);

  @BatchUpdate
  BatchResult<Dept> update(List<Dept> entity);

  @Insert(sqlFile = true)
  Result<Dept> insertBySqlFile(Dept entity);

  @Update(sqlFile = true)
  Result<Dept> updateBySqlFile(Dept entity);

  @BatchInsert(sqlFile = true)
  BatchResult<Dept> insertBySqlFile(List<Dept> entity);

  @BatchUpdate(sqlFile = true)
  BatchResult<Dept> updateBySqlFile(List<Dept> entity);
}
