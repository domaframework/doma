package org.seasar.doma.internal.apt.processor.dao;

import example.holder.PhoneNumber;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

@Dao(config = MyConfig.class)
public interface CollectorOptionalParameterDao {

  @Select(strategy = SelectType.COLLECT)
  <R> R selectById(Integer id, Collector<Optional<PhoneNumber>, ?, R> collector);

  @Select(strategy = SelectType.COLLECT)
  <R extends Number> R select(Collector<Optional<String>, ?, R> mapper);
}
