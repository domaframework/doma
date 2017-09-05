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
package org.seasar.doma.jdbc.entity;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.entity.PropertyField;

/**
 * A description for an embedded property.
 * 
 * @param <ENTITY>
 *            the entity type
 * @param <EMBEDDABLE>
 *            the embeddable type
 */
public class EmbeddedPropertyDesc<ENTITY, EMBEDDABLE> {

    protected final String name;

    protected final List<EntityPropertyDesc<ENTITY, ?>> embeddablePropertyDescs;

    protected final Map<String, EntityPropertyDesc<ENTITY, ?>> embeddablePropertyDescMap;

    protected final PropertyField<ENTITY> field;

    public EmbeddedPropertyDesc(String name, Class<ENTITY> entityClass,
            List<EntityPropertyDesc<ENTITY, ?>> embeddablePropertyDesc) {
        if (name == null) {
            throw new DomaNullPointerException("name");
        }
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (embeddablePropertyDesc == null) {
            throw new DomaNullPointerException("embeddablePropertyDesc");
        }
        this.name = name;
        this.embeddablePropertyDescs = embeddablePropertyDesc;
        this.embeddablePropertyDescMap = this.embeddablePropertyDescs.stream()
                .collect(toMap(EntityPropertyDesc::getName, Function.identity()));
        this.field = new PropertyField<>(name, entityClass);
    }

    public List<EntityPropertyDesc<ENTITY, ?>> getEmbeddablePropertyDescs() {
        return embeddablePropertyDescs;
    }

    public Map<String, EntityPropertyDesc<ENTITY, ?>> getEmbeddablePropertyDescMap() {
        return embeddablePropertyDescMap;
    }

    public void save(ENTITY entity, EMBEDDABLE value) {
        field.setValue(entity, value);
    }
}
