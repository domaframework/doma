package org.seasar.doma.internal.apt.processor.dao;

import java.sql.Clob;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.Dao;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface ClobFactoryDao {

  @ClobFactory
  Clob create();
}
