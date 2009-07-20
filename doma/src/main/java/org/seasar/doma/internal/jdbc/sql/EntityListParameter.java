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

import org.seasar.doma.entity.Entity;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClasseUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntityListParameter<I, E extends Entity<I>> implements
        ListParameter<E> {

    protected final Class<E> entityClass;

    protected final List<I> entities;

    public EntityListParameter(Class<E> entityClass, List<I> entities) {
        assertNotNull(entityClass, entities);
        this.entityClass = entityClass;
        this.entities = entities;
    }

    public E add() {
        E entity = createEntity();
        entities.add(entity.__asInterface());
        return entity;
    }

    protected E createEntity() {
        try {
            return ClasseUtil.newInstance(entityClass);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(MessageCode.DOMA2005, cause, entityClass
                    .getName(), cause);
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityListParameter(this, p);
    }

}
