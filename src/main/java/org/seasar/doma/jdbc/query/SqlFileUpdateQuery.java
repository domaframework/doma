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
import java.util.List;

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
 * 
 */
public class SqlFileUpdateQuery extends SqlFileModifyQuery implements UpdateQuery {

    protected EntityHandler<?> entityHandler;

    protected boolean nullExcluded;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    protected boolean unchangedPropertyIncluded;

    public SqlFileUpdateQuery() {
        super(SqlKind.UPDATE);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, sqlFilePath);
        initEntityHandler();
        preUpdate();
        prepareOptimisticLock();
        prepareOptions();
        prepareTargetPropertyDescs();
        prepareExecutable();
        prepareSql();
        assertNotNull(sql);
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

    protected void prepareExecutable() {
        if (entityHandler == null || entityHandler.hasTargetPropertyDescs()) {
            executable = true;
            sqlExecutionSkipCause = null;
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
    public void incrementVersion() {
        if (entityHandler != null) {
            entityHandler.incrementVersion();
        }
    }

    @SuppressWarnings("unchecked")
    public <E> E getEntity(Class<E> entityDesc) {
        if (entityHandler != null) {
            return (E) entityHandler.entity;
        }
        return null;
    }

    @Override
    public void complete() {
        if (entityHandler != null) {
            entityHandler.postUpdate();
        }
    }

    @Override
    public <E> void setEntityAndEntityDesc(String name, E entity, EntityDesc<E> entityDesc) {
        entityHandler = new EntityHandler<E>(name, entity, entityDesc);
    }

    public void setNullExcluded(boolean nullExcluded) {
        this.nullExcluded = nullExcluded;
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored |= versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
        this.unchangedPropertyIncluded = unchangedPropertyIncluded;
    }

    protected class EntityHandler<E> {

        protected String name;

        protected E entity;

        protected EntityDesc<E> entityDesc;

        protected VersionPropertyDesc<E, ?, ?> versionPropertyDesc;

        protected List<EntityPropertyDesc<E, ?>> targetPropertyDescs;

        protected UpdateQueryHelper<E> helper;

        protected EntityHandler(String name, E entity, EntityDesc<E> entityDesc) {
            assertNotNull(name, entity, entityDesc);
            this.name = name;
            this.entity = entity;
            this.entityDesc = entityDesc;
            this.versionPropertyDesc = entityDesc.getVersionPropertyDesc();
        }

        protected void init() {
            helper = new UpdateQueryHelper<E>(config, entityDesc, includedPropertyNames,
                    excludedPropertyNames, nullExcluded, versionIgnored,
                    optimisticLockExceptionSuppressed, unchangedPropertyIncluded);
        }

        protected void preUpdate() {
            SqlFilePreUpdateContext<E> context = new SqlFilePreUpdateContext<E>(entityDesc, method,
                    config);
            entityDesc.preUpdate(entity, context);
            if (context.getNewEntity() != null) {
                entity = context.getNewEntity();
                addParameterInternal(name, entityDesc.getEntityClass(), entity);
            }

        }

        protected void prepareTargetPropertyDescs() {
            targetPropertyDescs = helper.getTargetPropertyDescs(entity);
        }

        protected boolean hasTargetPropertyDescs() {
            return targetPropertyDescs != null && !targetPropertyDescs.isEmpty();
        }

        protected void postUpdate() {
            SqlFilePostUpdateContext<E> context = new SqlFilePostUpdateContext<E>(entityDesc,
                    method, config);
            entityDesc.postUpdate(entity, context);
            if (context.getNewEntity() != null) {
                entity = context.getNewEntity();
            }
            entityDesc.saveCurrentStates(entity);
        }

        protected void prepareOptimisticLock() {
            if (versionPropertyDesc != null && !versionIgnored) {
                if (!optimisticLockExceptionSuppressed) {
                    optimisticLockCheckRequired = true;
                }
            }
        }

        protected void incrementVersion() {
            if (versionPropertyDesc != null && !versionIgnored) {
                entity = versionPropertyDesc.increment(entityDesc, entity);
            }
        }

        protected void populateValues(SqlContext context) {
            helper.populateValues(entity, targetPropertyDescs, versionPropertyDesc, context);
        }

    }

    protected static class SqlFilePreUpdateContext<E> extends AbstractPreUpdateContext<E> {

        public SqlFilePreUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
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

    protected static class SqlFilePostUpdateContext<E> extends AbstractPostUpdateContext<E> {

        public SqlFilePostUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
            super(entityDesc, method, config);
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return true;
        }
    }

}
