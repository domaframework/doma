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

import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchUpdateQuery<E> extends SqlFileBatchModifyQuery<E>
        implements BatchUpdateQuery {

    protected EntityHandler entityHandler;

    protected boolean versionIncluded;

    protected boolean optimisticLockExceptionSuppressed;

    public SqlFileBatchUpdateQuery(Class<E> elementClass) {
        super(elementClass, SqlKind.BATCH_UPDATE);
    }

    @Override
    public void prepare() {
        super.prepare();
        Iterator<E> it = elements.iterator();
        if (it.hasNext()) {
            executable = true;
            sqlExecutionSkipCause = null;
            currentEntity = it.next();
            preUpdate();
            prepareSqlFile();
            prepareOptions();
            prepareOptimisticLock();
            prepareSql();
        } else {
            return;
        }
        while (it.hasNext()) {
            currentEntity = it.next();
            preUpdate();
            prepareSql();
        }
        assertEquals(elements.size(), sqls.size());
    }

    protected void preUpdate() {
        if (entityHandler != null) {
            entityHandler.preUpdate();
        }
    }

    protected void prepareOptimisticLock() {
        if (entityHandler != null) {
            entityHandler.prepareOptimisticLock();
        }
    }

    @Override
    public void incrementVersions() {
        if (entityHandler != null) {
            entityHandler.incrementVersions();
        }
    }

    @Override
    public void setEntityType(EntityType<E> entityType) {
        entityHandler = new EntityHandler(entityType);
    }

    public void setVersionIncluded(boolean versionIncluded) {
        this.versionIncluded = versionIncluded;
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

        protected void preUpdate() {
            entityType.preUpdate(currentEntity);
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyType != null && !versionIncluded) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }

        protected void incrementVersions() {
            if (versionPropertyType != null && !versionIncluded) {
                for (E entity : elements) {
                    versionPropertyType.increment(entity);
                }
            }
        }
    }
}
