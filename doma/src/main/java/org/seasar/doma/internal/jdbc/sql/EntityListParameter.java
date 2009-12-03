/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;

/**
 * @author taedium
 * 
 */
public class EntityListParameter<E> implements ListParameter<EntityType<E>> {

    protected final EntityTypeFactory<E> entityTypeFactory;

    protected final List<E> entities;

    protected EntityType<E> entityType;

    public EntityListParameter(EntityTypeFactory<E> entityTypeFactory,
            List<E> entities) {
        assertNotNull(entityTypeFactory, entities);
        this.entityTypeFactory = entityTypeFactory;
        this.entities = entities;
    }

    @Override
    public Object getValue() {
        return entities;
    }

    @Override
    public EntityType<E> getElementHolder() {
        entityType = entityTypeFactory.createEntityType();
        return entityType;
    }

    @Override
    public void add() {
        entities.add(entityType.getEntity());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityListParameter(this, p);
    }

}
