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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.jdbc.Config;
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

    protected boolean versionIgnored;

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

    public List<E> getEntities() {
        return elements;
    }

    @Override
    public void complete() {
        if (entityHandler != null) {
            for (E element : elements) {
                currentEntity = element;
                entityHandler.postUpdate();
            }
        }
    }

    @Override
    public void setEntityType(EntityType<E> entityType) {
        entityHandler = new EntityHandler(entityType);
    }

    public void setVersionIncluded(boolean versionIncluded) {
        this.versionIgnored |= versionIncluded;
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored |= versionIgnored;
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

        protected void preUpdate() {
            SqlFileBatchPreUpdateContext<E> context = new SqlFileBatchPreUpdateContext<E>(
                    entityType, method, config);
            entityType.preUpdate(currentEntity, context);
            if (entityType.isImmutable() && context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }

        protected void postUpdate() {
            SqlFileBatchPostUpdateContext<E> context = new SqlFileBatchPostUpdateContext<E>(
                    entityType, method, config);
            entityType.postUpdate(currentEntity, context);
            if (entityType.isImmutable() && context.getNewEntity() != null) {
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

        protected void incrementVersions() {
            if (versionPropertyType != null && !versionIgnored) {
                if (entityType.isImmutable()) {
                    List<E> newEntities = new ArrayList<E>(elements.size());
                    for (E entity : elements) {
                        E newEntity = versionPropertyType
                                .incrementAndMakeNewEntity(entityType, entity);
                        newEntities.add(newEntity);
                    }
                    elements = newEntities;
                } else {
                    for (E entity : elements) {
                        versionPropertyType.increment(entity);
                    }
                }
            }
        }
    }

    protected static class SqlFileBatchPreUpdateContext<E> extends
            AbstractPreUpdateContext<E> {

        public SqlFileBatchPreUpdateContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }

        @Override
        public boolean isEntityChanged() {
            return true;
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return true;
        }
    }

    protected static class SqlFileBatchPostUpdateContext<E> extends
            AbstractPostUpdateContext<E> {

        public SqlFileBatchPostUpdateContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return true;
        }
    }

}
