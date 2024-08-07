package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface DeptDao {

  @Select
  Dept selectById(Integer departmentId);

  @Select
  Dept selectByDepartmentNo(Integer departmentNo);

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

  @Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
  Result<Dept> insertOnDuplicateKeyUpdate(Dept entity);

  @Insert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"departmentNo"})
  Result<Dept> insertOnDuplicateKeyUpdateWithDepartmentNo(Dept entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  BatchResult<Dept> insertOnDuplicateKeyUpdate(List<Dept> entities);

  @BatchInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"departmentNo"})
  BatchResult<Dept> insertOnDuplicateKeyUpdateWithDepartmentNo(List<Dept> entities);

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  Result<Dept> insertOnDuplicateKeyIgnore(Dept entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  BatchResult<Dept> insertOnDuplicateKeyIgnore(List<Dept> entities);

  @MultiInsert
  MultiResult<Dept> insertMultiRows(List<Dept> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  MultiResult<Dept> insertMultiRowsOnDuplicateKeyIgnore(List<Dept> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  MultiResult<Dept> insertMultiRowsOnDuplicateKeyUpdate(List<Dept> entities);

  @MultiInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"departmentNo"})
  MultiResult<Dept> insertMultiRowsOnDuplicateKeyUpdateWithDepartmentNo(List<Dept> entities);
}
