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
package org.seasar.doma.internal.apt.processor.aggregate;

import java.util.function.BiFunction;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;

@AggregateStrategy(root = Dept.class, tableAlias = "d")
interface InvalidPropertyPath {

  @AssociationLinker(propertyPath = "employees", tableAlias = "e")
  BiFunction<Dept, Emp, Dept> employees = (d, e) -> null;

  @AssociationLinker(propertyPath = "employees.unknown", tableAlias = "a")
  BiFunction<Emp, Address, Emp> address = (e, a) -> null;
}
