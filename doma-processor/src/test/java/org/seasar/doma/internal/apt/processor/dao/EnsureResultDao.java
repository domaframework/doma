package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface EnsureResultDao {

  @Select(ensureResult = true)
  Emp2 selectById(Integer id, SelectOptions options);

  @Select(ensureResult = true)
  List<Emp2> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);
}
