package org.seasar.doma.internal.apt.dao;

import java.util.function.BiFunction;
import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.Config;

@Dao(config = MyConfig.class)
public interface SqlProcessorBiFunction2ndArgCheckDao {

  @SqlProcessor
  <R> R process(Integer id, BiFunction<Config, String, R> handler);
}
