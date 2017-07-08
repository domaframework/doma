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
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.CanonicalName;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.reflection.EmbeddableReflection;

/**
 * @author nakamura-to
 *
 */
public class EmbeddableMeta implements TypeElementMeta {

    private final EmbeddableReflection embeddableReflection;

    private final TypeElement embeddableElement;

    private final List<EmbeddablePropertyMeta> propertyMetas = new ArrayList<>();

    private EmbeddableConstructorMeta constructorMeta;

    private CanonicalName embeddableDescCanonicalName;

    public EmbeddableMeta(EmbeddableReflection embeddableReflection,
            TypeElement embeddableElement) {
        assertNotNull(embeddableReflection, embeddableElement);
        this.embeddableReflection = embeddableReflection;
        this.embeddableElement = embeddableElement;
    }

    public EmbeddableReflection getEmbeddableReflection() {
        return embeddableReflection;
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

    public boolean isAbstract() {
        return embeddableElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    public CanonicalName getEmbeddableDescCanonicalName() {
        return embeddableDescCanonicalName;
    }

    public void setEmbeddableDescCanonicalName(CanonicalName embeddableDescCanonicalName) {
        this.embeddableDescCanonicalName = embeddableDescCanonicalName;
    }

}
