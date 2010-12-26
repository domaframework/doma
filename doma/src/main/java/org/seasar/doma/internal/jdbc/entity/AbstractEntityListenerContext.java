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

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public abstract class AbstractEntityListenerContext {

    protected final EntityType<?> entityType;

    protected AbstractEntityListenerContext(EntityType<?> entityType) {
        assertNotNull(entityType);
        this.entityType = entityType;
    }

    public boolean isPropertyDefined(String propertyName) {
        if (propertyName == null) {
            throw new DomaNullPointerException("propertyName");
        }
        return isPropertyDefinedInternal(propertyName);
    }

    protected boolean isPropertyDefinedInternal(String propertyName) {
        assertNotNull(propertyName);
        return entityType.getEntityPropertyType(propertyName) != null;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }
}
