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
package org.seasar.doma.internal.apt.meta.entity;

import java.util.Objects;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.annot.AssociationLinkerAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;

public record AssociationLinkerMeta(
    AssociationLinkerAnnot associationLinkerAnnot,
    String ancestorPath,
    String propertyPath,
    int propertyPathDepth,
    String tableAlias,
    EntityCtType source,
    EntityCtType target,
    TypeElement classElement,
    VariableElement filedElement) {

  public AssociationLinkerMeta {
    Objects.requireNonNull(associationLinkerAnnot);
    Objects.requireNonNull(propertyPath);
    Objects.requireNonNull(tableAlias);
    Objects.requireNonNull(source);
    Objects.requireNonNull(target);
    Objects.requireNonNull(classElement);
    Objects.requireNonNull(filedElement);
  }
}
