package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.*;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface ImmutableEmpDao {

  @Insert
  Result<ImmutableEmp> insert(ImmutableEmp emp);

  @Delete
  Result<ImmutableEmp> update(ImmutableEmp emp);

  @Update
  Result<ImmutableEmp> delete(ImmutableEmp emp);

  @BatchInsert
  BatchResult<ImmutableEmp> batchInsert(List<ImmutableEmp> emp);

  @BatchUpdate
  BatchResult<ImmutableEmp> batchUpdate(List<ImmutableEmp> emp);

  @BatchDelete
  BatchResult<ImmutableEmp> batchDelete(List<ImmutableEmp> emp);

  @Insert(sqlFile = true)
  Result<ImmutableEmp> insert2(ImmutableEmp emp);

  @Update(sqlFile = true)
  Result<ImmutableEmp> update2(ImmutableEmp emp);

  @Delete(sqlFile = true)
  Result<ImmutableEmp> delete2(ImmutableEmp emp);

  @BatchInsert(sqlFile = true)
  BatchResult<ImmutableEmp> batchInsert2(List<ImmutableEmp> emp);

  @BatchUpdate(sqlFile = true)
  BatchResult<ImmutableEmp> batchUpdate2(List<ImmutableEmp> emp);

  @BatchDelete(sqlFile = true)
  BatchResult<ImmutableEmp> batchDelete2(List<ImmutableEmp> emp);
}
