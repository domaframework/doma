package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface AutoInsertDao {

  @Insert
  int insert(Emp entity);
}
