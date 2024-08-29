package org.seasar.doma.internal.apt.processor.dao;

import java.util.function.BiFunction;
import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;

@Dao
public interface SqlProcessorRawTypeDao {

  @SqlProcessor
  void process(Integer id, @SuppressWarnings("rawtypes") BiFunction handler);
}
