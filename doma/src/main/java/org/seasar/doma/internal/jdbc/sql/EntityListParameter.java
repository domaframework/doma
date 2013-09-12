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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public class EntityListParameter<E> implements ListParameter<EntityType<E>, E> {

    protected final List<E> entities;

    protected final EntityType<E> entityType;

    protected final String name;

    protected final boolean resultMappingEnsured;

    public EntityListParameter(EntityType<E> entityType, List<E> entities,
            String name, boolean resultMappingEnsured) {
        assertNotNull(entityType, entities, name);
        this.entityType = entityType;
        this.entities = entities;
        this.name = name;
        this.resultMappingEnsured = resultMappingEnsured;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return entities;
    }

    public EntityType<E> getEntityType() {
        return entityType;
    }

    @Override
    public void add(E entity) {
        entities.add(entity);
    }

    public boolean isResultMappingEnsured() {
        return resultMappingEnsured;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityListParameter(this, p);
    }

}
