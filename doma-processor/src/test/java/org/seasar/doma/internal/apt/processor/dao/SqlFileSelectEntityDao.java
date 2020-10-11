package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.jdbc.SelectOptions;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface SqlFileSelectEntityDao {

  @Select
  Emp selectById(Integer id, SelectOptions options);

  @Select
  List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);
}
