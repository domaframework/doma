/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor.dao;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
public interface CollectorDao {

  @Select(strategy = SelectType.COLLECT)
  Integer selectByIdAndName(Integer id, String name, Collector<Emp, ?, Integer> collector);

  @Select(strategy = SelectType.COLLECT)
  <R> R selectById(Integer id, Collector<PhoneNumber, ?, R> collector);

  @Select(strategy = SelectType.COLLECT)
  <R extends Number> R select(Collector<String, ?, R> collector);

  @Select(strategy = SelectType.COLLECT)
  String selectWithHogeCollector(HogeCollector collector);

  @Select(strategy = SelectType.COLLECT, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  <R> R selectByIdAsMap(Integer id, Collector<Map<String, Object>, ?, R> collector);

  class HogeCollector implements Collector<String, String, String> {

    @Override
    public Supplier<String> supplier() {
      return null;
    }

    @Override
    public BiConsumer<String, String> accumulator() {
      return null;
    }

    @Override
    public BinaryOperator<String> combiner() {
      return null;
    }

    @Override
    public Function<String, String> finisher() {
      return null;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return null;
    }
  }
}
