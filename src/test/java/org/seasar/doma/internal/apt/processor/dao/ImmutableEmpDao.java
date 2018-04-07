package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.processor.entity.ImmutableEmp;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

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

  @Sql(useFile = true)
  @Insert
  Result<ImmutableEmp> insert2(ImmutableEmp emp);

  @Sql(useFile = true)
  @Update
  Result<ImmutableEmp> update2(ImmutableEmp emp);

  @Sql(useFile = true)
  @Delete
  Result<ImmutableEmp> delete2(ImmutableEmp emp);

  @Sql(useFile = true)
  @BatchInsert
  BatchResult<ImmutableEmp> batchInsert2(List<ImmutableEmp> emp);

  @Sql(useFile = true)
  @BatchUpdate
  BatchResult<ImmutableEmp> batchUpdate2(List<ImmutableEmp> emp);

  @Sql(useFile = true)
  @BatchDelete
  BatchResult<ImmutableEmp> batchDelete2(List<ImmutableEmp> emp);
}
