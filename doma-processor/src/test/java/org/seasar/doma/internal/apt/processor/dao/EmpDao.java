package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface EmpDao {

  @Select
  Emp2 selectById(Integer id, SelectOptions options);

  @Select
  List<Emp2> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);

  @Insert
  int insert(Emp2 entity);

  @Update
  int update(Emp2 entity);

  @Delete
  int delete(Emp2 entity);
}
