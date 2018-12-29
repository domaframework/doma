package org.seasar.doma.internal.apt.dao;

import java.util.function.BiFunction;
import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;

@Dao(config = MyConfig.class)
public interface SqlProcessorRawTypeDao {

  @SqlProcessor
  void process(Integer id, @SuppressWarnings("rawtypes") BiFunction handler);
}
