package org.seasar.doma.internal.apt.processor.dao;

import java.util.function.BiFunction;
import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;

@Dao
public interface SqlProcessorMultiBiFunctionsDao {

  @SqlProcessor
  void process(
      Integer id,
      BiFunction<Config, PreparedSql, Void> handler1,
      BiFunction<Config, PreparedSql, Void> handler2);
}
