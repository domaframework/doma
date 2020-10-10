package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface EnumDao {

  @Select
  MyEnum selectById(MyEnum id, SelectOptions options);

  @Select
  List<MyEnum> selectByNameAndSalary(MyEnum name, MyEnum salary, SelectOptions options);

  enum MyEnum {}
}
