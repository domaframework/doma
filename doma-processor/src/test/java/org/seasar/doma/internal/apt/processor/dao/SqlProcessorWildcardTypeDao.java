package org.seasar.doma.internal.apt.processor.dao;

import java.util.function.BiFunction;
import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface SqlProcessorWildcardTypeDao {

  @SqlProcessor
  <R> R process(Integer id, BiFunction<Config, PreparedSql, ?> handler);
}
