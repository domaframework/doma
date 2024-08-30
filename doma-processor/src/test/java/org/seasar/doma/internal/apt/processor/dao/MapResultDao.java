package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao
public interface MapResultDao {

  @Select
  Map<String, Object> selectSingleResult();

  @Select
  Optional<Map<String, Object>> selectOptionalSingleResult();

  @Select
  List<Map<String, Object>> selectResultList();

  @Select(strategy = SelectType.STREAM)
  <R> R stream(Function<Stream<Map<String, Object>>, R> mapper);
}
