package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface EnsureResultDao {

  @Select(ensureResult = true)
  Emp selectById(Integer id, SelectOptions options);

  @Select(ensureResult = true)
  List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);
}
