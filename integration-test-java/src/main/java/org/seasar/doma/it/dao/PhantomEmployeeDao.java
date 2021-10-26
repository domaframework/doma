package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.it.entity.PhantomEmployee;

@Dao
public interface PhantomEmployeeDao {
  @Sql("select * from EMPLOYEE")
  @Select
  List<PhantomEmployee> selectAll();

  @Sql("select * from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  PhantomEmployee selectById(int id);

  @Insert
  int insert(PhantomEmployee e);
}
