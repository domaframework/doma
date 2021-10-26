package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.CompKeyDepartment;

@Dao
public interface CompKeyDepartmentDao {

  @Select
  CompKeyDepartment selectById(Integer departmentId1, Integer departmentId2);

  @Insert
  int insert(CompKeyDepartment entity);

  @Update
  int update(CompKeyDepartment entity);

  @BatchInsert
  int[] insert(List<CompKeyDepartment> entities);

  @BatchUpdate
  int[] update(List<CompKeyDepartment> entities);
}
