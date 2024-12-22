package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface EnsureResultMappingDao {

  @Select(ensureResultMapping = true)
  Emp2 selectById(Integer id, SelectOptions options);

  @Select(ensureResultMapping = true)
  List<Emp2> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);

  @Procedure
  void procedure(@ResultSet(ensureResultMapping = true) List<Emp2> emp);

  @Function(ensureResultMapping = true)
  List<Emp2> function(@ResultSet(ensureResultMapping = true) List<Emp2> emp);
}
