package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.jdbc.AbstractDao;

@Dao(config = MyConfig.class)
public abstract class IllegalSingleConstructorClassDao extends AbstractDao {

  public IllegalSingleConstructorClassDao() {
    super(null);
  }
}
