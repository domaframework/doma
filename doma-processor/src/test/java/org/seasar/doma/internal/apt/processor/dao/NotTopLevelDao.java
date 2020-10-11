package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;

public interface NotTopLevelDao {

  @SuppressWarnings("deprecation")
  @Dao(config = MyConfig.class)
  interface Hoge {}
}
