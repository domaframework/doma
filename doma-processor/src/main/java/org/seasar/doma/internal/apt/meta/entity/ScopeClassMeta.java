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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.lang.model.element.TypeElement;

public class ScopeClassMeta {

  private final TypeElement typeElement;
  private final List<ScopeMethodMeta> methods;
  private final String identifier;

  public ScopeClassMeta(TypeElement typeElement, List<ScopeMethodMeta> methods) {
    this.typeElement = Objects.requireNonNull(typeElement);
    this.methods = Collections.unmodifiableList(Objects.requireNonNull(methods));
    this.identifier = "__scope__" + typeElement.getQualifiedName().toString().replace(".", "_");
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public List<ScopeMethodMeta> getMethods() {
    return methods;
  }

  public String getIdentifier() {
    return identifier;
  }
}
