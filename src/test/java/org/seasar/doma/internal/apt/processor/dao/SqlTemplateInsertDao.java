package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Sql;

@Dao(config = MyConfig.class)
public interface SqlTemplateInsertDao {

  @Sql(useFile = true)
  @Insert
  int insert(Integer id, BigDecimal salary);
}
