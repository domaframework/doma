/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.dao;

import example.entity.Emp;
import java.util.List;
import java.util.OptionalDouble;
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
