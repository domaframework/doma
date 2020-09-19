package org.seasar.doma.internal.apt.processor.dao;

import example.entity.Emp;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.jdbc.Reference;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Dao(config = MyConfig.class)
public interface OptionalIntDao {

  @Select
  Emp selectById(OptionalInt id);

  @Select
  OptionalInt selectAgeById(OptionalInt id);

  @Select
  List<OptionalInt> selectAllAge();

  @Select(strategy = SelectType.STREAM)
  <R> R selectAllAge(Function<Stream<OptionalInt>, R> mapper);

  @Select(strategy = SelectType.COLLECT)
  <R> R selectAllAge(Collector<OptionalInt, ?, R> mapper);

  @org.seasar.doma.Function
  OptionalInt getSingleResult(
      @In OptionalInt in,
      @InOut Reference<OptionalInt> inout,
      @Out Reference<OptionalInt> out,
      @ResultSet List<OptionalInt> resultSet);

  @org.seasar.doma.Function
  List<OptionalInt> getResultList();

  @Procedure
  void execute(
      @In OptionalInt in,
      @InOut Reference<OptionalInt> inout,
      @Out Reference<OptionalInt> out,
      @ResultSet List<OptionalInt> resultSet);
}
