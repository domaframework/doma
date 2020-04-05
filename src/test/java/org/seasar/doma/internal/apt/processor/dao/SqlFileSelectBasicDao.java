package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

@Dao(config = MyConfig.class)
public interface SqlFileSelectBasicDao {

  @Select
  String selectById(Integer id);

  @Select
  List<String> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);
}
