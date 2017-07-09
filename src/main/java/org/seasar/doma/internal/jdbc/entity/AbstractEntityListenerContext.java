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
package org.seasar.doma.internal.jdbc.entity;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Method;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityDesc;

/**
 * @author taedium
 * 
 */
public abstract class AbstractEntityListenerContext<E> {

    protected final EntityDesc<E> entityDesc;

    protected final Method method;

    protected final Config config;

    protected E newEntity;

    protected AbstractEntityListenerContext(EntityDesc<E> entityDesc,
            Method method, Config config) {
        assertNotNull(entityDesc, method, config);
        this.entityDesc = entityDesc;
        this.method = method;
        this.config = config;
    }

    protected boolean isPropertyDefinedInternal(String propertyName) {
        assertNotNull(propertyName);
        return entityDesc.getEntityPropertyDesc(propertyName) != null;
    }

    public EntityDesc<E> getEntityType() {
        return entityDesc;
    }

    public Method getMethod() {
        return method;
    }

    public Config getConfig() {
        return config;
    }

    public E getNewEntity() {
        return this.newEntity;
    }

    public void setNewEntity(E newEntity) {
        if (newEntity == null) {
            throw new DomaNullPointerException("newEntity");
        }
        this.newEntity = newEntity;
    }

}
