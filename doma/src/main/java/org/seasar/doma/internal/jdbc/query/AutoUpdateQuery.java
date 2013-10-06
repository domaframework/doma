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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.Accessor;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class AutoUpdateQuery<E> extends AutoModifyQuery<E> implements
        UpdateQuery {

    protected boolean nullExcluded;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    protected boolean unchangedPropertyIncluded;

    public AutoUpdateQuery(EntityType<E> entityType) {
        super(entityType);
    }

    @Override
    public void prepare() {
        assertNotNull(method, config, entityType, entity, callerClassName,
                callerMethodName);
        preUpdate();
        prepareIdAndVersionPropertyTypes();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareTargetPropertyTypes();
        prepareSql();
        assertNotNull(sql);
    }

    protected void preUpdate() {
        List<EntityPropertyType<E, ?, ?>> targetPropertyTypes = getTargetPropertyTypes();
        AutoPreUpdateContext<E> context = new AutoPreUpdateContext<E>(
                entityType, method, config, targetPropertyTypes);
        entityType.preUpdate(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
    }

    protected void prepareOptimisticLock() {
        if (!versionIgnored && versionPropertyType != null) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetPropertyTypes() {
        targetPropertyTypes = getTargetPropertyTypes();
        if (!targetPropertyTypes.isEmpty()) {
            executable = true;
            sqlExecutionSkipCause = null;
        }
    }

    protected List<EntityPropertyType<E, ?, ?>> getTargetPropertyTypes() {
        int capacity = entityType.getEntityPropertyTypes().size();
        List<EntityPropertyType<E, ?, ?>> results = new ArrayList<>(capacity);
        E originalStates = entityType.getOriginalStates(entity);
        for (EntityPropertyType<E, ?, ?> p : entityType
                .getEntityPropertyTypes()) {
            if (!p.isUpdatable()) {
                continue;
            }
            if (p.isId()) {
                continue;
            }
            if (!versionIgnored && p.isVersion()) {
                continue;
            }
            if (nullExcluded) {
                Accessor<E, ?, ?> accessor = p.getAccessor();
                accessor.load(entity);
                if (accessor.getWrapper().get() == null) {
                    continue;
                }
            }
            if (unchangedPropertyIncluded || originalStates == null
                    || isChanged(originalStates, p)) {
                String name = p.getName();
                if (!isTargetPropertyName(name)) {
                    continue;
                }
                results.add(p);
            }
        }
        return results;
    }

    protected boolean isChanged(E originalStates,
            EntityPropertyType<E, ?, ?> propertyType) {
        Wrapper<?> originalWrapper = propertyType.getAccessor()
                .load(originalStates).getWrapper();
        Wrapper<?> wrapper = propertyType.getAccessor().load(entity)
                .getWrapper();
        return !wrapper.hasEqualValue(originalWrapper.get());
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.UPDATE);
        builder.appendSql("update ");
        builder.appendSql(entityType.getQualifiedTableName());
        builder.appendSql(" set ");
        for (EntityPropertyType<E, ?, ?> p : targetPropertyTypes) {
            Accessor<E, ?, ?> accessor = p.getAccessor();
            accessor.load(entity);
            builder.appendSql(p.getColumnName());
            builder.appendSql(" = ");
            builder.appendWrapper(accessor.getWrapper());
            builder.appendSql(", ");
        }
        if (!versionIgnored && versionPropertyType != null) {
            Accessor<E, ?, ?> accessor = versionPropertyType.getAccessor();
            accessor.load(entity);
            builder.appendSql(versionPropertyType.getColumnName());
            builder.appendSql(" = ");
            builder.appendWrapper(accessor.getWrapper());
            builder.appendSql(" + 1");
        } else {
            builder.cutBackSql(2);
        }
        if (idPropertyTypes.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyType<E, ?, ?> p : idPropertyTypes) {
                Accessor<E, ?, ?> accessor = p.getAccessor();
                accessor.load(entity);
                builder.appendSql(p.getColumnName());
                builder.appendSql(" = ");
                builder.appendWrapper(accessor.getWrapper());
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (!versionIgnored && versionPropertyType != null) {
            if (idPropertyTypes.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            Accessor<E, ?, ?> accessor = versionPropertyType.getAccessor();
            accessor.load(entity);
            builder.appendSql(versionPropertyType.getColumnName());
            builder.appendSql(" = ");
            builder.appendWrapper(accessor.getWrapper());
        }
        sql = builder.build();
    }

    @Override
    public void incrementVersion() {
        if (!versionIgnored && versionPropertyType != null) {
            entity = versionPropertyType.increment(entityType, entity);
        }
    }

    @Override
    public void complete() {
        postUpdate();
    }

    protected void postUpdate() {
        List<EntityPropertyType<E, ?, ?>> targetPropertyTypes = getTargetPropertyTypes();
        if (!versionIgnored && versionPropertyType != null) {
            targetPropertyTypes.add(versionPropertyType);
        }
        AutoPostUpdateContext<E> context = new AutoPostUpdateContext<E>(
                entityType, method, config, targetPropertyTypes);
        entityType.postUpdate(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
    }

    public void setNullExcluded(boolean nullExcluded) {
        this.nullExcluded = nullExcluded;
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored |= versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
        this.unchangedPropertyIncluded = unchangedPropertyIncluded;
    }

    protected static class AutoPreUpdateContext<E> extends
            AbstractPreUpdateContext<E> {

        protected final Set<String> changedPropertyNames;

        public AutoPreUpdateContext(EntityType<E> entityType, Method method,
                Config config,
                List<EntityPropertyType<E, ?, ?>> targetPropertyTypes) {
            super(entityType, method, config);
            assertNotNull(targetPropertyTypes);
            changedPropertyNames = new HashSet<String>(
                    targetPropertyTypes.size());
            for (EntityPropertyType<E, ?, ?> propertyType : targetPropertyTypes) {
                changedPropertyNames.add(propertyType.getName());
            }
        }

        @Override
        public boolean isEntityChanged() {
            return !changedPropertyNames.isEmpty();
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return changedPropertyNames.contains(propertyName);
        }
    }

    protected static class AutoPostUpdateContext<E> extends
            AbstractPostUpdateContext<E> {

        protected final Set<String> changedPropertyNames;

        public AutoPostUpdateContext(EntityType<E> entityType, Method method,
                Config config,
                List<EntityPropertyType<E, ?, ?>> targetPropertyTypes) {
            super(entityType, method, config);
            assertNotNull(targetPropertyTypes);
            changedPropertyNames = new HashSet<String>(
                    targetPropertyTypes.size());
            for (EntityPropertyType<E, ?, ?> propertyType : targetPropertyTypes) {
                changedPropertyNames.add(propertyType.getName());
            }
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return changedPropertyNames.contains(propertyName);
        }
    }

}
