package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao
public interface BasicResultDao {

  @Select
  String selectSingleResult();

  @Select
  Optional<String> selectOptionalSingleResult();

  @Select
  List<String> selectResultList();

  @Select
  List<Optional<String>> selectOptionalResultList();

  @Select(strategy = SelectType.STREAM)
  <R> R stream(Function<Stream<String>, R> mapper);
}
