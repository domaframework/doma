package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.Reference;

@Dao(config = MyConfig.class)
public interface OptionalLongDao {

  @Select
  Emp selectById(OptionalLong id);

  @Select
  OptionalLong selectAgeById(OptionalLong id);

  @Select
  List<OptionalLong> selectAllAge();

  @Select(strategy = SelectType.STREAM)
  <R> R selectAllAge(Function<Stream<OptionalLong>, R> mapper);

  @Select(strategy = SelectType.COLLECT)
  <R> R selectAllAge(Collector<OptionalLong, ?, R> mapper);

  @org.seasar.doma.Function
  OptionalLong getSingleResult(
      @In OptionalLong in,
      @InOut Reference<OptionalLong> inout,
      @Out Reference<OptionalLong> out,
      @ResultSet List<OptionalLong> resultSet);

  @org.seasar.doma.Function
  List<OptionalLong> getResultList();

  @Procedure
  void execute(
      @In OptionalLong in,
      @InOut Reference<OptionalLong> inout,
      @Out Reference<OptionalLong> out,
      @ResultSet List<OptionalLong> resultSet);
}
