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
package org.seasar.doma.internal.apt.meta.aggregate;

import java.util.List;
import java.util.Objects;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public record AggregateStrategyMeta(
    TypeElement typeElement,
    EntityCtType root,
    String tableAlias,
    List<AssociationLinkerMeta> associationLinkerMetas)
    implements TypeElementMeta {

  public AggregateStrategyMeta {
    Objects.requireNonNull(root);
    Objects.requireNonNull(tableAlias);
    Objects.requireNonNull(associationLinkerMetas);
  }

  @Override
  public boolean isError() {
    return false;
  }
}
