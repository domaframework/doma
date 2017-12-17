package org.seasar.doma.internal.apt.processor.dao;

import java.util.function.BiFunction;

import org.seasar.doma.Dao;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;

/**
 * @author nakamura
 *
 */
@Dao(config = MyConfig.class)
public interface SqlProcessorDao {

    @SqlProcessor
    <R> R process_typeParameter(Integer id, BiFunction<Config, PreparedSql, R> handler);

    @SqlProcessor
    String process_string(Integer id, BiFunction<Config, PreparedSql, String> handler);

    @SqlProcessor
    void process_void(Integer id, BiFunction<Config, PreparedSql, Void> handler);
}
