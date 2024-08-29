package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface SqlFileSelectMapDao {

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  Map<String, Object> selectById(Integer id, SelectOptions options);

  @Select
  List<Map<String, Object>> selectByNameAndSalary(
      String name, BigDecimal salary, SelectOptions options);
}
