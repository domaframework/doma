package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;
import org.seasar.doma.it.entity.Department;

@Dao
public interface DepartmentDao {

  @Select
  Department selectById(Integer departmentId);

  @Insert
  int insert(Department entity);

  @Insert(sqlFile = true)
  int insertBySqlFile(Department entity);

  @Insert(excludeNull = true)
  int insert_excludeNull(Department entity);

  @Update
  int update(Department entity);

  @Update(sqlFile = true)
  int updateBySqlFile(Department entity);

  @Update(sqlFile = true)
  int updateBySqlFileWithPopulate(Department entity);

  @Update(sqlFile = true, ignoreVersion = true)
  int updateBySqlFile_ignoreVersion(Department entity);

  @Update(excludeNull = true)
  int update_excludeNull(Department entity);

  @Update(ignoreVersion = true)
  int update_ignoreVersion(Department entity);

  @Update(suppressOptimisticLockException = true)
  int update_suppressOptimisticLockException(Department entity);

  @Update(sqlFile = true)
  int updateBySqlFile_nonEntity(
      Identity<Department> departmentId,
      Integer departmentNo,
      String departmentName,
      Location<Department> location,
      Integer version);

  @BatchInsert
  int[] insert(List<Department> entity);

  @BatchInsert(sqlFile = true)
  int[] insertBySqlFile(List<Department> entity);

  @BatchUpdate
  int[] update(List<Department> entity);

  @BatchUpdate(sqlFile = true)
  int[] updateBySqlFile(List<Department> entity);

  @BatchUpdate(sqlFile = true, suppressOptimisticLockException = true)
  int[] updateBySqlFile_suppressOptimisticLockException(List<Department> entity);

  @BatchUpdate(ignoreVersion = true)
  int[] update_ignoreVersion(List<Department> entity);

  @BatchUpdate(suppressOptimisticLockException = true)
  int[] update_suppressOptimisticLockException(List<Department> entity);
}
