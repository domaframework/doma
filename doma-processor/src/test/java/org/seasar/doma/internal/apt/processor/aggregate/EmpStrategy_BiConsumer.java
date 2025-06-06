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

import java.util.function.BiConsumer;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;

@AggregateStrategy(root = Emp.class, tableAlias = "e")
interface EmpStrategy_BiConsumer {
  @AssociationLinker(propertyPath = "dept", tableAlias = "d")
  BiConsumer<Emp, Dept> dept = (e, d) -> {};

  @AssociationLinker(propertyPath = "address", tableAlias = "a")
  BiConsumer<Emp, Address> address = (e, a) -> {};
}
