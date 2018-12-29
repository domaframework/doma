package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

/** @author taedium */
@Dao
public interface NoConfigEmpDao {

  @Select
  Emp selectById(Integer id, SelectOptions options);

  @Select
  List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);

  @Insert
  int insert(Emp entity);

  @Update
  int update(Emp entity);

  @Delete
  int delete(Emp entity);
}
