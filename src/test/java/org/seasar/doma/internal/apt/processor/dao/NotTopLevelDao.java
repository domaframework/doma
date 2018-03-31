package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;

public interface NotTopLevelDao {

  @Dao(config = MyConfig.class)
  interface Hoge {}
}
