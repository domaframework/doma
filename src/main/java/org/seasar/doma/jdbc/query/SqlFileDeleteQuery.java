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
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;

import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

/**
 * @author taedium
 * 
 */
public class SqlFileDeleteQuery extends SqlFileModifyQuery implements
        DeleteQuery {

    protected EntityHandler<?> entityHandler;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public SqlFileDeleteQuery() {
        super(SqlKind.DELETE);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, sqlFilePath);
        preDelete();
        prepareOptions();
        prepareOptimisticLock();
        prepareExecutable();
        prepareSql();
        assertNotNull(sql);
    }

    protected void preDelete() {
        if (entityHandler != null) {
            entityHandler.preDelete();
        }
    }

    protected void prepareOptimisticLock() {
        if (entityHandler != null) {
            entityHandler.prepareOptimisticLock();
        }
    }

    protected void prepareExecutable() {
        executable = true;
        sqlExecutionSkipCause = null;
    }

    @Override
    public void complete() {
        if (entityHandler != null) {
            entityHandler.postDelete();
        }
    }

    @Override
    public <E> void setEntityAndEntityType(String name, E entity,
            EntityDesc<E> entityType) {
        entityHandler = new EntityHandler<E>(name, entity, entityType);
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored = versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    @SuppressWarnings("unchecked")
    public <E> E getEntity(Class<E> entityType) {
        if (entityHandler != null) {
            return (E) entityHandler.entity;
        }
        return null;
    }

    protected class EntityHandler<E> {

        protected String name;

        protected E entity;

        protected EntityDesc<E> entityDesc;

        protected VersionPropertyDesc<E, ?, ?> versionPropertyDesc;

        protected EntityHandler(String name, E entity, EntityDesc<E> entityType) {
            assertNotNull(name, entity, entityType);
            this.name = name;
            this.entity = entity;
            this.entityDesc = entityType;
            this.versionPropertyDesc = entityType.getVersionPropertyDesc();
        }

        protected void preDelete() {
            SqlFilePreDeleteContext<E> context = new SqlFilePreDeleteContext<E>(
                    entityDesc, method, config);
            entityDesc.preDelete(entity, context);
            if (context.getNewEntity() != null) {
                entity = context.getNewEntity();
                addParameterInternal(name, entityDesc.getEntityClass(), entity);
            }
        }

        protected void postDelete() {
            SqlFilePostDeleteContext<E> context = new SqlFilePostDeleteContext<E>(
                    entityDesc, method, config);
            entityDesc.postDelete(entity, context);
            if (context.getNewEntity() != null) {
                entity = context.getNewEntity();
            }
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyDesc != null && !versionIgnored) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }
    }

    protected static class SqlFilePreDeleteContext<E> extends
            AbstractPreDeleteContext<E> {

        public SqlFilePreDeleteContext(EntityDesc<E> entityType, Method method,
                Config config) {
            super(entityType, method, config);
        }
    }

    protected static class SqlFilePostDeleteContext<E> extends
            AbstractPostDeleteContext<E> {

        public SqlFilePostDeleteContext(EntityDesc<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }
}
