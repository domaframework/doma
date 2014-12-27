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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * @param <ENTITY>
 *            エンティティ
 */
public class AutoUpdateQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements
        UpdateQuery {

    protected boolean nullExcluded;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    protected boolean unchangedPropertyIncluded;

    public AutoUpdateQuery(EntityType<ENTITY> entityType) {
        super(entityType);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, entityType, entity);
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
        List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes = getTargetPropertyTypes();
        AutoPreUpdateContext<ENTITY> context = new AutoPreUpdateContext<ENTITY>(
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

    protected List<EntityPropertyType<ENTITY, ?>> getTargetPropertyTypes() {
        int capacity = entityType.getEntityPropertyTypes().size();
        List<EntityPropertyType<ENTITY, ?>> results = new ArrayList<>(capacity);
        ENTITY originalStates = entityType.getOriginalStates(entity);
        for (EntityPropertyType<ENTITY, ?> propertyType : entityType
                .getEntityPropertyTypes()) {
            if (!propertyType.isUpdatable()) {
                continue;
            }
            if (propertyType.isId()) {
                continue;
            }
            if (!versionIgnored && propertyType.isVersion()) {
                continue;
            }
            if (nullExcluded) {
                Property<ENTITY, ?> property = propertyType.createProperty();
                property.load(entity);
                if (property.getWrapper().get() == null) {
                    continue;
                }
            }
            if (unchangedPropertyIncluded || originalStates == null
                    || isChanged(originalStates, propertyType)) {
                String name = propertyType.getName();
                if (!isTargetPropertyName(name)) {
                    continue;
                }
                results.add(propertyType);
            }
        }
        return results;
    }

    protected boolean isChanged(ENTITY originalStates,
            EntityPropertyType<ENTITY, ?> propertyType) {
        Wrapper<?> originalWrapper = propertyType.createProperty()
                .load(originalStates).getWrapper();
        Wrapper<?> wrapper = propertyType.createProperty().load(entity)
                .getWrapper();
        return !wrapper.hasEqualValue(originalWrapper.get());
    }

    protected void prepareSql() {
        Dialect dialect = config.getDialect();
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.UPDATE, sqlLogType);
        builder.appendSql("update ");
        builder.appendSql(entityType.getQualifiedTableName(dialect::applyQuote));
        builder.appendSql(" set ");
        for (EntityPropertyType<ENTITY, ?> propertyType : targetPropertyTypes) {
            Property<ENTITY, ?> property = propertyType.createProperty();
            property.load(entity);
            builder.appendSql(propertyType.getColumnName(dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property);
            builder.appendSql(", ");
        }
        if (!versionIgnored && versionPropertyType != null) {
            Property<ENTITY, ?> property = versionPropertyType.createProperty();
            property.load(entity);
            builder.appendSql(versionPropertyType
                    .getColumnName(dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property);
            builder.appendSql(" + 1");
        } else {
            builder.cutBackSql(2);
        }
        if (idPropertyTypes.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyType<ENTITY, ?> propertyType : idPropertyTypes) {
                Property<ENTITY, ?> property = propertyType.createProperty();
                property.load(entity);
                builder.appendSql(propertyType
                        .getColumnName(dialect::applyQuote));
                builder.appendSql(" = ");
                builder.appendParameter(property);
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
            Property<ENTITY, ?> property = versionPropertyType.createProperty();
            property.load(entity);
            builder.appendSql(versionPropertyType
                    .getColumnName(dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property);
        }
        sql = builder.build(this::comment);
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
        List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes = getTargetPropertyTypes();
        if (!versionIgnored && versionPropertyType != null) {
            targetPropertyTypes.add(versionPropertyType);
        }
        AutoPostUpdateContext<ENTITY> context = new AutoPostUpdateContext<ENTITY>(
                entityType, method, config, targetPropertyTypes);
        entityType.postUpdate(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
        entityType.saveCurrentStates(entity);
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
                List<EntityPropertyType<E, ?>> targetPropertyTypes) {
            super(entityType, method, config);
            assertNotNull(targetPropertyTypes);
            changedPropertyNames = new HashSet<String>(
                    targetPropertyTypes.size());
            for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
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
                List<EntityPropertyType<E, ?>> targetPropertyTypes) {
            super(entityType, method, config);
            assertNotNull(targetPropertyTypes);
            changedPropertyNames = new HashSet<String>(
                    targetPropertyTypes.size());
            for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
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
