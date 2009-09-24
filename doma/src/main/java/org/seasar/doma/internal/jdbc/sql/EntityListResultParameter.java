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

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;

/**
 * @author taedium
 * 
 */
public class EntityListResultParameter<E> implements ResultParameter<List<E>>,
        ListParameter<EntityType<E>> {

    protected final EntityTypeFactory<E> entityTypeFactory;

    protected final List<E> results = new ArrayList<E>();

    public EntityListResultParameter(EntityTypeFactory<E> entityTypeFactory) {
        assertNotNull(entityTypeFactory);
        this.entityTypeFactory = entityTypeFactory;
    }

    @Override
    public EntityType<E> getElementHolder() {
        return entityTypeFactory.createEntityType();
    }

    @Override
    public void putElementHolder(EntityType<E> entityType) {
        results.add(entityType.getEntity());
    }

    @Override
    public List<E> getResult() {
        return results;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityListResultParameter(this, p);
    }

}
