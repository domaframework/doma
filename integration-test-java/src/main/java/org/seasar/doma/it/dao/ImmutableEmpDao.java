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
package org.seasar.doma.it.dao;

import java.util.List;
import java.util.function.BiFunction;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.ImmutableDept;
import org.seasar.doma.it.entity.ImmutableEmp;

@Dao
public interface ImmutableEmpDao {
  @Select(aggregateStrategy = SelectByStrategy.class)
  ImmutableEmp selectById(Integer employeeId);

  @Select(aggregateStrategy = SelectByIdsStrategy.class)
  List<ImmutableEmp> selectByIds(List<Integer> employeeIds);

  @AggregateStrategy(root = ImmutableEmp.class, tableAlias = "e")
  interface SelectByStrategy {
    @AssociationLinker(propertyPath = "dept", tableAlias = "d")
    BiFunction<ImmutableEmp, ImmutableDept, ImmutableEmp> dept =
        (e, d) -> new ImmutableEmp(e.employeeId(), e.employeeName(), d, e.manager());
  }

  @AggregateStrategy(root = ImmutableEmp.class, tableAlias = "e")
  interface SelectByIdsStrategy {
    @AssociationLinker(propertyPath = "dept", tableAlias = "d")
    BiFunction<ImmutableEmp, ImmutableDept, ImmutableEmp> dept = SelectByStrategy.dept;

    @AssociationLinker(propertyPath = "manager", tableAlias = "m")
    BiFunction<ImmutableEmp, ImmutableEmp, ImmutableEmp> manager =
        (e, m) -> new ImmutableEmp(e.employeeId(), e.employeeName(), e.dept(), m);
  }
}
