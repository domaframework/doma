package org.seasar.doma.internal.apt.processor.dao;

import java.sql.NClob;
import org.seasar.doma.Dao;
import org.seasar.doma.NClobFactory;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface NClobFactoryDao {

  @NClobFactory
  NClob create();
}
