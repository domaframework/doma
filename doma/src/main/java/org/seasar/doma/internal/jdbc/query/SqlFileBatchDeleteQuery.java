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
package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Method;

import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchDeleteQuery<E> extends SqlFileBatchModifyQuery<E>
        implements BatchDeleteQuery {

    protected EntityHandler entityHandler;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public SqlFileBatchDeleteQuery(Class<E> elementClass) {
        super(elementClass, SqlKind.BATCH_DELETE);
    }

    @Override
    public void prepare() {
        super.prepare();
        int size = elements.size();
        if (size == 0) {
            return;
        }
        executable = true;
        sqlExecutionSkipCause = null;
        currentEntity = elements.get(0);
        preDelete();
        prepareSqlFile();
        prepareOptions();
        prepareOptimisticLock();
        prepareSql();
        elements.set(0, currentEntity);
        for (int i = 1; i < size; i++) {
            currentEntity = elements.get(i);
            preDelete();
            prepareSql();
            elements.set(i, currentEntity);
        }
        assertEquals(size, sqls.size());
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

    @Override
    public void complete() {
        if (entityHandler != null) {
            for (int i = 0, len = elements.size(); i < len; i++) {
                currentEntity = elements.get(i);
                entityHandler.postDelete();
                elements.set(i, currentEntity);
            }
        }
    }

    @Override
    public void setEntityType(EntityType<E> entityType) {
        entityHandler = new EntityHandler(entityType);
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored = versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected class EntityHandler {

        protected EntityType<E> entityType;

        protected VersionPropertyType<? super E, E, ?, ?> versionPropertyType;

        protected EntityHandler(EntityType<E> entityType) {
            assertNotNull(entityType);
            this.entityType = entityType;
            this.versionPropertyType = entityType.getVersionPropertyType();
        }

        protected void preDelete() {
            SqlFileBatchPreDeleteContext<E> context = new SqlFileBatchPreDeleteContext<E>(
                    entityType, method, config);
            entityType.preDelete(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }

        protected void postDelete() {
            SqlFileBatchPostDeleteContext<E> context = new SqlFileBatchPostDeleteContext<E>(
                    entityType, method, config);
            entityType.postDelete(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyType != null && !versionIgnored) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }
    }

    protected static class SqlFileBatchPreDeleteContext<E> extends
            AbstractPreDeleteContext<E> {

        public SqlFileBatchPreDeleteContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }

    protected static class SqlFileBatchPostDeleteContext<E> extends
            AbstractPostDeleteContext<E> {

        public SqlFileBatchPostDeleteContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }
}
