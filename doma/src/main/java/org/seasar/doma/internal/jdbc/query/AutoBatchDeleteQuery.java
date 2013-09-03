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
import java.util.Iterator;

import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteQuery<E> extends AutoBatchModifyQuery<E> implements
        BatchDeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchDeleteQuery(EntityType<E> entityType) {
        super(entityType);
    }

    @Override
    public void prepare() {
        assertNotNull(method, config, callerClassName, callerMethodName,
                entities, sqls);
        Iterator<E> it = entities.iterator();
        if (it.hasNext()) {
            executable = true;
            executionSkipCause = null;
            currentEntity = it.next();
            preDelete();
            prepareIdAndVersionPropertyTypes();
            validateIdExistent();
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
        assertEquals(entities.size(), sqls.size());
    }

    protected void preDelete() {
        AutoBatchPreDeleteContext<E> context = new AutoBatchPreDeleteContext<E>(
                entityType, method, config);
        entityType.preDelete(currentEntity, context);
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

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.BATCH_DELETE);
        builder.appendSql("delete from ");
        builder.appendSql(entityType.getQualifiedTableName());
        if (idPropertyTypes.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyType<E, ?> p : idPropertyTypes) {
                builder.appendSql(p.getColumnName());
                builder.appendSql(" = ");
                builder.appendWrapper(p.getWrapper(currentEntity));
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (versionPropertyType != null && !versionIgnored) {
            if (idPropertyTypes.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            builder.appendSql(versionPropertyType.getColumnName());
            builder.appendSql(" = ");
            builder.appendWrapper(versionPropertyType.getWrapper(currentEntity));
        }
        PreparedSql sql = builder.build();
        sqls.add(sql);
    }

    @Override
    public void complete() {
        for (E entity : entities) {
            currentEntity = entity;
            postDelete();
        }
    }

    protected void postDelete() {
        AutoBatchPostDeleteContext<E> context = new AutoBatchPostDeleteContext<E>(
                entityType, method, config);
        entityType.postDelete(currentEntity, context);
        if (entityType.isImmutable() && context.getNewEntity() != null) {
            currentEntity = context.getNewEntity();
        }
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored = versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected static class AutoBatchPreDeleteContext<E> extends
            AbstractPreDeleteContext<E> {

        public AutoBatchPreDeleteContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }

    protected static class AutoBatchPostDeleteContext<E> extends
            AbstractPostDeleteContext<E> {

        public AutoBatchPostDeleteContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }
}
