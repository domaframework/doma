package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Staff;

@Dao
public interface StaffDao {

  @Select
  Staff selectById(Integer employeeId);

  @Insert
  int insert(Staff staff);

  @Update
  int update(Staff staff);

  @Update(sqlFile = true)
  int updateBySqlFile(Staff staff);

  @Delete
  int delete(Staff staff);

  @BatchInsert
  int[] insert(List<Staff> staff);

  @BatchUpdate
  int[] update(List<Staff> staff);

  @BatchDelete
  int[] delete(List<Staff> staff);
}
