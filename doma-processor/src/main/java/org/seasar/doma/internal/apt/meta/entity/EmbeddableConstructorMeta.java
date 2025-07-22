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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import javax.lang.model.element.ExecutableElement;

public class EmbeddableConstructorMeta {

  private final ExecutableElement constructorElement;
  private final List<EmbeddableFieldMeta> embeddableFieldMetas;

  public EmbeddableConstructorMeta(
      ExecutableElement constructorElement, List<EmbeddableFieldMeta> embeddableFieldMetas) {
    assertNotNull(constructorElement, embeddableFieldMetas);
    this.constructorElement = constructorElement;
    this.embeddableFieldMetas = Collections.unmodifiableList(embeddableFieldMetas);
  }

  public ExecutableElement getConstructorElement() {
    return constructorElement;
  }

  public List<EmbeddableFieldMeta> getEmbeddableFieldMetas() {
    return embeddableFieldMetas;
  }
}
