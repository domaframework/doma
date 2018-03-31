package org.seasar.doma.internal.apt.processor.dao;

import example.holder.PhoneNumber;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao(config = MyConfig.class)
public interface StreamOptionalParameterDao {

  @Select(strategy = SelectType.STREAM)
  <R> R selectById(Integer id, Function<Stream<Optional<PhoneNumber>>, R> mapper);

  @Select(strategy = SelectType.STREAM)
  <R extends Number> R select(Function<Stream<Optional<String>>, R> mapper);
}
