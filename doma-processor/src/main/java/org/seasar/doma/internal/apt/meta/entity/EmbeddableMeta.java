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

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class EmbeddableMeta implements TypeElementMeta {

  private final EmbeddableAnnot embeddableAnnot;

  private final TypeElement typeElement;

  private final TypeMirror type;

  private final List<EmbeddablePropertyMeta> propertyMetas = new ArrayList<>();

  private EmbeddableConstructorMeta constructorMeta;

  private boolean error;

  public EmbeddableMeta(EmbeddableAnnot embeddableAnnot, TypeElement typeElement) {
    assertNotNull(embeddableAnnot, typeElement);
    this.embeddableAnnot = embeddableAnnot;
    this.typeElement = typeElement;
    this.type = typeElement.asType();
  }

  public EmbeddableAnnot getEmbeddableAnnot() {
    return embeddableAnnot;
  }

  public TypeMirror getType() {
    return type;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public void addEmbeddablePropertyMeta(EmbeddablePropertyMeta propertyMeta) {
    propertyMetas.add(propertyMeta);
  }

  public List<EmbeddablePropertyMeta> getEmbeddablePropertyMetas() {
    if (constructorMeta != null) {
      return constructorMeta.getEmbeddablePropertyMetas();
    }
    return propertyMetas;
  }

  public void setConstructorMeta(EmbeddableConstructorMeta constructorMeta) {
    this.constructorMeta = constructorMeta;
  }

  @Override
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }
}
