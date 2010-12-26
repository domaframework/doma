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

import java.util.Iterator;

import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
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
        Iterator<E> it = elements.iterator();
        if (it.hasNext()) {
            executable = true;
            sqlExecutionSkipCause = null;
            currentEntity = it.next();
            preDelete();
            prepareSqlFile();
            prepareOptions();
            prepareOptimisticLock();
            prepareSql();
        } else {
            return;
        }
        while (it.hasNext()) {
            currentEntity = it.next();
            preDelete();
            prepareSql();
        }
        assertEquals(elements.size(), sqls.size());
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

        protected VersionPropertyType<E, ?> versionPropertyType;

        protected EntityHandler(EntityType<E> entityType) {
            assertNotNull(entityType);
            this.entityType = entityType;
            this.versionPropertyType = entityType.getVersionPropertyType();
        }

        protected void preDelete() {
            SqlFileBatchPreDeleteContext context = new SqlFileBatchPreDeleteContext(
                    entityType);
            entityType.preDelete(currentEntity, context);
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyType != null && !versionIgnored) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }
    }

    protected static class SqlFileBatchPreDeleteContext extends
            AbstractPreDeleteContext {

        public SqlFileBatchPreDeleteContext(EntityType<?> entityType) {
            super(entityType);
        }
    }
}
