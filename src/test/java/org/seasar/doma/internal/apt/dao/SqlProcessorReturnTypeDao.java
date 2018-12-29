package org.seasar.doma.internal.apt.dao;

import java.util.function.BiFunction;
import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;

/** @author nakamura */
@Dao(config = MyConfig.class)
public interface SqlProcessorReturnTypeDao {

  @SqlProcessor
  String process(Integer id, BiFunction<Config, PreparedSql, Integer> handler);
}
