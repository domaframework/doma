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
 * 
 * @author nakamura-to
 * @since 2.10.0
 *
 * @param <ENTITY>
 *            エンティティクラスの型
 * @param <EMBEDDABLE>
 *            エンベッダブルクラスの型
 */
public class EmbeddedPropertyType<ENTITY, EMBEDDABLE> {

    protected final String name;

    protected final List<EntityPropertyType<ENTITY, ?>> embeddablePropertyTypes;

    protected final Map<String, EntityPropertyType<ENTITY, ?>> embeddablePropertyTypeMap;

    protected final PropertyField<ENTITY> field;

    public EmbeddedPropertyType(String name, Class<ENTITY> entityClass,
            List<EntityPropertyType<ENTITY, ?>> embeddablePropertyType) {
        if (name == null) {
            throw new DomaNullPointerException("name");
        }
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (embeddablePropertyType == null) {
            throw new DomaNullPointerException("embeddablePropertyType");
        }
        this.name = name;
        this.embeddablePropertyTypes = embeddablePropertyType;
        this.embeddablePropertyTypeMap = this.embeddablePropertyTypes
                .stream()
                .collect(
                        toMap(EntityPropertyType::getName, Function.identity()));
        this.field = new PropertyField<>(name, entityClass);
    }

    public List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes() {
        return embeddablePropertyTypes;
    }

    public Map<String, EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypeMap() {
        return embeddablePropertyTypeMap;
    }

    public void save(ENTITY entity, EMBEDDABLE value) {
        field.setValue(entity, value);
    }
}
