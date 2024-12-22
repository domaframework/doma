package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.OptionalLong;
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
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.jdbc.Reference;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
@Dao
public interface OptionalLongDao {

  @Select
  Emp2 selectById(OptionalLong id);

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
