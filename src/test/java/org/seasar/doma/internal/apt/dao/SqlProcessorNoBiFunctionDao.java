package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;

/** @author nakamura */
@Dao(config = MyConfig.class)
public interface SqlProcessorNoBiFunctionDao {

  @SqlProcessor
  void process(Integer id);
}
