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

@AggregateStrategy(root = ChildEntity.class, tableAlias = "c")
interface ChildEntityStrategy {

  @AssociationLinker(propertyPath = "dept", tableAlias = "d")
  BiFunction<ChildEntity, Dept, ChildEntity> dept = (c, d) -> c;

  @AssociationLinker(propertyPath = "emp", tableAlias = "e")
  BiFunction<ChildEntity, Emp, ChildEntity> emp = (c, d) -> c;
}
