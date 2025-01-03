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
package org.seasar.doma.internal.apt.def;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;

public class TypeParametersDef {

  private final Map<TypeParameterElement, String> typeParameterNameMap;

  public TypeParametersDef(Map<TypeParameterElement, String> typeParameterNameMap) {
    assertNotNull(typeParameterNameMap);
    this.typeParameterNameMap = typeParameterNameMap;
  }

  public List<String> getTypeVariables() {
    return typeParameterNameMap.keySet().stream()
        .map(Element::getSimpleName)
        .map(Name::toString)
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }

  public List<String> getTypeParameters() {
    return List.copyOf(typeParameterNameMap.values());
  }
}
