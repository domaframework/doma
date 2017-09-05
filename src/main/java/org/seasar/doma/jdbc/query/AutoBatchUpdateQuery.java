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

import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.Property;

public class AutoBatchUpdateQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
        implements BatchUpdateQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    protected BatchUpdateQueryHelper<ENTITY> helper;

    public AutoBatchUpdateQuery(EntityDesc<ENTITY> entityDesc) {
        super(entityDesc);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, entities, sqls);
        int size = entities.size();
        if (size == 0) {
            return;
        }
        executable = true;
        executionSkipCause = null;
        currentEntity = entities.get(0);
        setupHelper();
        preUpdate();
        prepareIdAndVersionPropertyDescs();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareTargetPropertyDescs();
        prepareSql();
        entities.set(0, currentEntity);
        for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext();) {
            currentEntity = it.next();
            preUpdate();
            prepareSql();
            it.set(currentEntity);
        }
        assertEquals(entities.size(), sqls.size());
    }

    protected void setupHelper() {
        helper = new BatchUpdateQueryHelper<>(config, entityDesc, includedPropertyNames,
                excludedPropertyNames, versionIgnored, optimisticLockExceptionSuppressed);
    }

    protected void preUpdate() {
        AutoBatchPreUpdateContext<ENTITY> context = new AutoBatchPreUpdateContext<ENTITY>(
                entityDesc, method, config);
        entityDesc.preUpdate(currentEntity, context);
        if (context.getNewEntity() != null) {
            currentEntity = context.getNewEntity();
        }
    }

    protected void prepareOptimisticLock() {
        if (versionPropertyDesc != null && !versionIgnored) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetPropertyDescs() {
        targetPropertyDescs = helper.getTargetPropertyDescs();
    }

    protected void prepareSql() {
        Naming naming = config.getNaming();
        Dialect dialect = config.getDialect();
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_UPDATE,
                sqlLogType);
        builder.appendSql("update ");
        builder.appendSql(entityDesc.getQualifiedTableName(naming::apply, dialect::applyQuote));
        builder.appendSql(" set ");
        helper.populateValues(currentEntity, targetPropertyDescs, versionPropertyDesc, builder);
        if (idPropertyDescs.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyDesc<ENTITY, ?> propertyDesc : idPropertyDescs) {
                Property<ENTITY, ?> property = propertyDesc.createProperty();
                property.load(currentEntity);
                builder.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
                builder.appendSql(" = ");
                builder.appendParameter(property.asInParameter());
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (versionPropertyDesc != null && !versionIgnored) {
            if (idPropertyDescs.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            Property<ENTITY, ?> property = versionPropertyDesc.createProperty();
            property.load(currentEntity);
            builder.appendSql(
                    versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property.asInParameter());
        }
        PreparedSql sql = builder.build(this::comment);
        sqls.add(sql);
    }

    @Override
    public void incrementVersions() {
        if (versionPropertyDesc != null && !versionIgnored) {
            for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext();) {
                ENTITY newEntity = versionPropertyDesc.increment(entityDesc, it.next());
                it.set(newEntity);
            }
        }
    }

    @Override
    public void complete() {
        for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext();) {
            currentEntity = it.next();
            postUpdate();
            it.set(currentEntity);
        }
    }

    protected void postUpdate() {
        AutoBatchPostUpdateContext<ENTITY> context = new AutoBatchPostUpdateContext<ENTITY>(
                entityDesc, method, config);
        entityDesc.postUpdate(currentEntity, context);
        if (context.getNewEntity() != null) {
            currentEntity = context.getNewEntity();
        }
        entityDesc.saveCurrentStates(currentEntity);
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored |= versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected static class AutoBatchPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

        public AutoBatchPreUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
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

    protected static class AutoBatchPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

        public AutoBatchPostUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
            super(entityDesc, method, config);
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return true;
        }
    }
}
