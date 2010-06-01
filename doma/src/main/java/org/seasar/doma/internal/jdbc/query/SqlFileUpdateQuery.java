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

import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * @author taedium
 * 
 */
public class SqlFileUpdateQuery extends SqlFileModifyQuery implements
        UpdateQuery {

    protected EntityHandler<?> entityHandler;

    protected boolean versionIncluded;

    protected boolean optimisticLockExceptionSuppressed;

    public SqlFileUpdateQuery() {
        super(SqlKind.UPDATE);
    }

    public void prepare() {
        assertNotNull(config, sqlFilePath, callerClassName, callerMethodName);
        preUpdate();
        prepareOptimisticLock();
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
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
    public void incrementVersion() {
        if (entityHandler != null) {
            entityHandler.incrementVersion();
        }
    }

    @Override
    public <E> void setEntityAndEntityType(E entity, EntityType<E> entityType) {
        entityHandler = new EntityHandler<E>(entity, entityType);
    }

    public void setVersionIncluded(boolean versionIncluded) {
        this.versionIncluded = versionIncluded;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected class EntityHandler<E> {

        protected E entity;

        protected EntityType<E> entityType;

        protected VersionPropertyType<E, ?> versionPropertyType;

        protected EntityHandler(E entity, EntityType<E> entityType) {
            assertNotNull(entity, entityType);
            this.entity = entity;
            this.entityType = entityType;
            this.versionPropertyType = entityType.getVersionPropertyType();
        }

        protected void preUpdate() {
            entityType.preUpdate(entity);
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyType != null && !versionIncluded) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }

        protected void incrementVersion() {
            if (versionPropertyType != null && !versionIncluded) {
                versionPropertyType.increment(entity);
            }
        }

    }
}
