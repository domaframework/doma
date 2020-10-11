package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface EnsureResultMappingDao {

  @Select(ensureResultMapping = true)
  Emp selectById(Integer id, SelectOptions options);

  @Select(ensureResultMapping = true)
  List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);

  @Procedure
  void procedure(@ResultSet(ensureResultMapping = true) List<Emp> emp);

  @Function(ensureResultMapping = true)
  List<Emp> function(@ResultSet(ensureResultMapping = true) List<Emp> emp);
}
