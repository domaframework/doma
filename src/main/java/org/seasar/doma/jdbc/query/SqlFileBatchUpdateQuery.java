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

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

/**
 * @author taedium
 * @param <ELEMENT>
 *            リストの要素
 */
public class SqlFileBatchUpdateQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
        implements BatchUpdateQuery {

    protected EntityHandler entityHandler;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public SqlFileBatchUpdateQuery(Class<ELEMENT> elementClass) {
        super(elementClass, SqlKind.BATCH_UPDATE);
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
        initEntityHandler();
        preUpdate();
        prepareSqlFile();
        prepareOptions();
        prepareOptimisticLock();
        prepareTargetPropertyDescs();
        prepareSql();
        elements.set(0, currentEntity);
        for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext();) {
            currentEntity = it.next();
            preUpdate();
            prepareSql();
            it.set(currentEntity);
        }
        assertEquals(size, sqls.size());
    }

    protected void initEntityHandler() {
        if (entityHandler != null) {
            entityHandler.init();
        }
    }

    protected void preUpdate() {
        if (entityHandler != null) {
            entityHandler.preUpdate();
        }
    }

    protected void prepareTargetPropertyDescs() {
        if (entityHandler != null) {
            entityHandler.prepareTargetPropertyDescs();
        }
    }

    @Override
    protected void populateValues(PopulateNode node, SqlContext context) {
        if (entityHandler == null) {
            throw new UnsupportedOperationException();
        }
        entityHandler.populateValues(context);
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
    public void complete() {
        if (entityHandler != null) {
            for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext();) {
                currentEntity = it.next();
                entityHandler.postUpdate();
                it.set(currentEntity);
            }
        }
    }

    @Override
    public void setEntityDesc(EntityDesc<ELEMENT> entityDesc) {
        entityHandler = new EntityHandler(entityDesc);
    }

    public void setVersionIncluded(boolean versionIncluded) {
        this.versionIgnored |= versionIncluded;
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored |= versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected class EntityHandler {

        protected EntityDesc<ELEMENT> entityDesc;

        protected VersionPropertyDesc<ELEMENT, ?, ?> versionPropertyDesc;

        protected List<EntityPropertyDesc<ELEMENT, ?>> targetPropertyDescs;

        protected BatchUpdateQueryHelper<ELEMENT> helper;

        protected EntityHandler(EntityDesc<ELEMENT> entityDesc) {
            assertNotNull(entityDesc);
            this.entityDesc = entityDesc;
            this.versionPropertyDesc = entityDesc.getVersionPropertyDesc();
        }

        protected void init() {
            helper = new BatchUpdateQueryHelper<>(config, entityDesc, includedPropertyNames,
                    excludedPropertyNames, versionIgnored, optimisticLockExceptionSuppressed);
        }

        protected void preUpdate() {
            SqlFileBatchPreUpdateContext<ELEMENT> context = new SqlFileBatchPreUpdateContext<ELEMENT>(
                    entityDesc, method, config);
            entityDesc.preUpdate(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }

        protected void prepareTargetPropertyDescs() {
            targetPropertyDescs = helper.getTargetPropertyDescs();
        }

        protected void postUpdate() {
            SqlFileBatchPostUpdateContext<ELEMENT> context = new SqlFileBatchPostUpdateContext<ELEMENT>(
                    entityDesc, method, config);
            entityDesc.postUpdate(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
            entityDesc.saveCurrentStates(currentEntity);
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyDesc != null && !versionIgnored) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }

        protected void incrementVersions() {
            if (versionPropertyDesc != null && !versionIgnored) {
                for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext();) {
                    ELEMENT newEntity = versionPropertyDesc.increment(entityDesc, it.next());
                    it.set(newEntity);
                }
            }
        }

        protected void populateValues(SqlContext context) {
            helper.populateValues(currentEntity, targetPropertyDescs, versionPropertyDesc, context);
        }

    }

    protected static class SqlFileBatchPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

        public SqlFileBatchPreUpdateContext(EntityDesc<E> entityDesc, Method method,
                Config config) {
            super(entityDesc, method, config);
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

    protected static class SqlFileBatchPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

        public SqlFileBatchPostUpdateContext(EntityDesc<E> entityDesc, Method method,
                Config config) {
            super(entityDesc, method, config);
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return true;
        }
    }

}
