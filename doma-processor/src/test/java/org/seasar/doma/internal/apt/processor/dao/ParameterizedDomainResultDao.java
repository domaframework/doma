package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao
public interface ParameterizedDomainResultDao {

  @Select
  Height<String> selectSingleResult();

  @Select
  Optional<Height<String>> selectOptionalSingleResult();

  @Select
  List<Height<String>> selectResultList();

  @Select
  List<Optional<Height<String>>> selectOptionalResultList();

  @Select(strategy = SelectType.STREAM)
  <R> R stream(Function<Stream<Height<String>>, R> callback);
}
