package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;

@Dao
public interface SqlProcessorNoBiFunctionDao {

  @SqlProcessor
  void process(Integer id);
}
