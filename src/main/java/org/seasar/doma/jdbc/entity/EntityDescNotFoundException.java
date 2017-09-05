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

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that an entity description is not found.
 */
public class EntityDescNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    private final String entityClassName;

    private final String entityDescClassName;

    public EntityDescNotFoundException(Throwable cause, String entityClassName,
            String entityDescClassName) {
        super(Message.DOMA2203, cause, entityClassName, entityDescClassName, cause);
        this.entityClassName = entityClassName;
        this.entityDescClassName = entityDescClassName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getEntityDescClassName() {
        return entityDescClassName;
    }

}
