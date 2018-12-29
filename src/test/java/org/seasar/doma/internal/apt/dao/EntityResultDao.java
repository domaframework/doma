package org.seasar.doma.internal.apt.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = MyConfig.class)
public interface EntityResultDao {

  @Select
  Emp selectSingleResult();

  @Select
  Optional<Emp> selectOptionalSingleResult();

  @Select
  Optional<Emp> selectOptionalSingleResultWithExpansion();

  @Select
  List<Emp> selectResultList();

  @Select(strategy = SelectType.STREAM)
  <R> R stream(Function<Stream<Emp>, R> mapper);
}
