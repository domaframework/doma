package org.seasar.doma.it.dao;

import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.it.domain.Money;
import org.seasar.doma.it.entity.FulltimeEmployee;

@Dao
public interface FulltimeEmployeeDao {

  @Sql("select salary from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  Money selectSalaryById(int id);

  @Sql("select salary from EMPLOYEE where EMPLOYEE_ID = /* id */0")
  @Select
  Optional<Money> selectOptionalSalaryById(int id);

  @Insert
  int insert(FulltimeEmployee e);
}
