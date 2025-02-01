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
package org.seasar.doma.internal.apt.processor.dao.aggregate;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Sql;

@Dao
interface InvalidSelectType {
  @Sql("select * from emp")
  @Select(strategy = SelectType.STREAM, aggregateStrategy = EmpStrategy.class)
  List<Emp> selectAll(Function<Stream<Emp>, List<Emp>> function);
}
