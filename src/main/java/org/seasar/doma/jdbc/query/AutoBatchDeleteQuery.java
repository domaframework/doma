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
import java.util.ListIterator;

import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

/**
 * @author taedium
 * @param <ENTITY>
 *            エンティティ
 */
public class AutoBatchDeleteQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
        implements BatchDeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchDeleteQuery(EntityType<ENTITY> entityType) {
        super(entityType);
    }

    @Override
    public void prepare() {
        assertNotNull(method, config, callerClassName, callerMethodName,
                entities, sqls);
        int size = entities.size();
        if (size == 0) {
            return;
        }
        executable = true;
        executionSkipCause = null;
        currentEntity = entities.get(0);
        preDelete();
        prepareIdAndVersionPropertyTypes();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareSql();
        entities.set(0, currentEntity);
        for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext();) {
            currentEntity = it.next();
            preDelete();
            prepareSql();
            it.set(currentEntity);
        }
        assertEquals(size, sqls.size());
    }

    protected void preDelete() {
        AutoBatchPreDeleteContext<ENTITY> context = new AutoBatchPreDeleteContext<ENTITY>(
                entityType, method, config);
        entityType.preDelete(currentEntity, context);
        if (context.getNewEntity() != null) {
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
        Dialect dialect = config.getDialect();
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.BATCH_DELETE, sqlLogType);
        builder.appendSql("delete from ");
        builder.appendSql(entityType.getQualifiedTableName(dialect::applyQuote));
        if (idPropertyTypes.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyType<ENTITY, ?> propertyType : idPropertyTypes) {
                Property<ENTITY, ?> property = propertyType.createProperty();
                property.load(currentEntity);
                builder.appendSql(propertyType
                        .getColumnName(dialect::applyQuote));
                builder.appendSql(" = ");
                builder.appendParameter(property);
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
            Property<ENTITY, ?> property = versionPropertyType.createProperty();
            property.load(currentEntity);
            builder.appendSql(versionPropertyType
                    .getColumnName(dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property);
        }
        PreparedSql sql = builder.build();
        sqls.add(sql);
    }

    @Override
    public void complete() {
        for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext();) {
            currentEntity = it.next();
            postDelete();
            it.set(currentEntity);
        }
    }

    protected void postDelete() {
        AutoBatchPostDeleteContext<ENTITY> context = new AutoBatchPostDeleteContext<ENTITY>(
                entityType, method, config);
        entityType.postDelete(currentEntity, context);
        if (context.getNewEntity() != null) {
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
