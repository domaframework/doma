/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.mirror.EmbeddableMirror;

/** @author nakamura-to */
public class EmbeddableMeta implements TypeElementMeta {

  protected final EmbeddableMirror embeddableMirror;

  protected final TypeElement embeddableElement;

  protected final List<EmbeddablePropertyMeta> propertyMetas = new ArrayList<>();

  protected EmbeddableConstructorMeta constructorMeta;

  protected boolean error;

  public EmbeddableMeta(EmbeddableMirror embeddableMirror, TypeElement embeddableElement) {
    assertNotNull(embeddableMirror, embeddableElement);
    this.embeddableMirror = embeddableMirror;
    this.embeddableElement = embeddableElement;
  }

  public EmbeddableMirror getEmbeddableMirror() {
    return embeddableMirror;
  }

  public TypeElement getEmbeddableElement() {
    return embeddableElement;
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
    return embeddableElement.getModifiers().contains(Modifier.ABSTRACT);
  }
}
