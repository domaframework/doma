package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.CompKeyEmployee;

@Dao
public interface CompKeyEmployeeDao {

  @Select
  CompKeyEmployee selectById(Integer employeeId1, Integer employeeId2);

  @Delete
  int delete(CompKeyEmployee entity);

  @BatchDelete
  int[] delete(List<CompKeyEmployee> entities);
}
