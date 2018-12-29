package org.seasar.doma.internal.apt.dao;

import java.math.BigDecimal;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;

@Dao(config = MyConfig.class)
public interface SqlFileInsertDao {

  @Insert(sqlFile = true)
  int insert(Integer id, BigDecimal salary);
}
