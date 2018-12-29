package org.seasar.doma.internal.apt.dao;

import example.domain.PhoneNumber;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao(config = MyConfig.class)
public interface DomainResultDao {

  @Select
  PhoneNumber selectSingleResult();

  @Select
  Optional<PhoneNumber> selectOptionalSingleResult();

  @Select
  List<PhoneNumber> selectResultList();

  @Select
  List<Optional<PhoneNumber>> selectOptionalResultList();

  @Select(strategy = SelectType.STREAM)
  <R> R stream(Function<Stream<PhoneNumber>, R> mapper);
}
