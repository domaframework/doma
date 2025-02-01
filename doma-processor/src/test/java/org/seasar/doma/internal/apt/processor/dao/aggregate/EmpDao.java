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
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;

@Dao
interface EmpDao {
  @Sql("select * from emp")
  @Select(aggregateStrategy = EmpStrategy.class)
  List<Emp> selectAll();

  @Sql("select * from emp where id = /*id*/0")
  @Select(aggregateStrategy = EmpStrategy.class)
  Emp selectById(Integer id);

  @Sql("select * from emp where id = /*id*/0")
  @Select(aggregateStrategy = EmpStrategy.class)
  Optional<Emp> selectOptionalById(Integer id);
}
