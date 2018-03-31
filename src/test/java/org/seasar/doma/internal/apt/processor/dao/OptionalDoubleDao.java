package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.Reference;

/** @author nakamura-to */
@Dao(config = MyConfig.class)
public interface OptionalDoubleDao {

  @Select
  Emp selectById(OptionalDouble id);

  @Select
  OptionalDouble selectAgeById(OptionalDouble id);

  @Select
  List<OptionalDouble> selectAllAge();

  @Select(strategy = SelectType.STREAM)
  <R> R selectAllAge(Function<Stream<OptionalDouble>, R> mapper);

  @Select(strategy = SelectType.COLLECT)
  <R> R selectAllAge(Collector<OptionalDouble, ?, R> mapper);

  @org.seasar.doma.Function
  OptionalDouble getSingleResult(
      @In OptionalDouble in,
      @InOut Reference<OptionalDouble> inout,
      @Out Reference<OptionalDouble> out,
      @ResultSet List<OptionalDouble> resultSet);

  @org.seasar.doma.Function
  List<OptionalDouble> getResultList();

  @Procedure
  void execute(
      @In OptionalDouble in,
      @InOut Reference<OptionalDouble> inout,
      @Out Reference<OptionalDouble> out,
      @ResultSet List<OptionalDouble> resultSet);
}
