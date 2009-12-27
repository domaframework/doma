/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class AutoUpdateQuery<E> extends AutoModifyQuery<E> implements
        UpdateQuery {

    protected boolean nullExcluded;

    protected boolean versionIncluded;

    protected boolean optimisticLockExceptionSuppressed;

    protected boolean unchangedPropertyIncluded;

    public AutoUpdateQuery(EntityType<E> entityType) {
        super(entityType);
    }

    public void prepare() {
        assertNotNull(config, entityType, entity, callerClassName,
                callerMethodName);
        entityType.preUpdate(entity);
        prepareTable();
        prepareIdAndVersionPropertyTypes();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareTargetProperties();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareOptimisticLock() {
        if (versionPropertyType != null && !versionIncluded) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetProperties() {
        E originalStates = entityType.getOriginalStates(entity);
        for (EntityPropertyType<E, ?> p : entityType.getEntityPropertyTypes()) {
            if (!p.isUpdatable()) {
                continue;
            }
            if (p.isId()) {
                continue;
            }
            if (p.isVersion()) {
                targetPropertyTypes.add(p);
                continue;
            }
            if (nullExcluded && p.getWrapper(entity).get() == null) {
                continue;
            }
            if (unchangedPropertyIncluded || originalStates == null
                    || isChanged(originalStates, p)) {
                if (!isTargetPropertyName(p.getName())) {
                    continue;
                }
                targetPropertyTypes.add(p);
                executable = true;
                sqlExecutionSkipCause = null;
            }
        }
    }

    protected boolean isChanged(E originalStates,
            EntityPropertyType<E, ?> propertyType) {
        EntityPropertyType<E, ?> originalPropertyType = entityType
                .getEntityPropertyType(propertyType.getName());
        if (originalPropertyType == null) {
            return true;
        }
        Wrapper<?> originalWrapper = originalPropertyType
                .getWrapper(originalStates);
        Wrapper<?> wrapper = propertyType.getWrapper(entity);
        return !wrapper.hasEqualValue(originalWrapper.get());
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.UPDATE);
        builder.appendSql("update ");
        builder.appendSql(tableName);
        builder.appendSql(" set ");
        for (EntityPropertyType<E, ?> p : targetPropertyTypes) {
            builder.appendSql(p.getColumnName());
            builder.appendSql(" = ");
            builder.appendWrapper(p.getWrapper(entity));
            if (p.isVersion() && !versionIncluded) {
                builder.appendSql(" + 1");
            }
            builder.appendSql(", ");
        }
        builder.cutBackSql(2);
        if (idPropertyTypes.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyType<E, ?> p : idPropertyTypes) {
                builder.appendSql(p.getColumnName());
                builder.appendSql(" = ");
                builder.appendWrapper(p.getWrapper(entity));
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (versionPropertyType != null && !versionIncluded) {
            if (idPropertyTypes.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            builder.appendSql(versionPropertyType.getColumnName());
            builder.appendSql(" = ");
            builder.appendWrapper(versionPropertyType.getWrapper(entity));
        }
        sql = builder.build();
    }

    @Override
    public void incrementVersion() {
        if (versionIncluded || versionPropertyType == null) {
            return;
        }
        versionPropertyType.increment(entity);
    }

    public void setNullExcluded(boolean nullExcluded) {
        this.nullExcluded = nullExcluded;
    }

    public void setVersionIncluded(boolean versionIncluded) {
        this.versionIncluded = versionIncluded;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
        this.unchangedPropertyIncluded = unchangedPropertyIncluded;
    }

}
