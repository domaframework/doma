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

import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.Property;

/**
 * @author taedium
 * @param <ENTITY>
 *            エンティティ
 */
public class AutoDeleteQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements
        DeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoDeleteQuery(EntityDesc<ENTITY> entityDesc) {
        super(entityDesc);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, entityDesc);
        executable = true;
        preDelete();
        prepareIdAndVersionPropertyDescs();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareSql();
        assertNotNull(sql);
    }

    protected void preDelete() {
        AutoPreDeleteContext<ENTITY> context = new AutoPreDeleteContext<ENTITY>(
                entityDesc, method, config);
        entityDesc.preDelete(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
    }

    protected void prepareOptimisticLock() {
        if (versionPropertyDesc != null && !versionIgnored) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareSql() {
        Naming naming = config.getNaming();
        Dialect dialect = config.getDialect();
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.DELETE, sqlLogType);
        builder.appendSql("delete from ");
        builder.appendSql(entityDesc.getQualifiedTableName(naming::apply,
                dialect::applyQuote));
        if (idPropertyDescs.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyDesc<ENTITY, ?> propertyDesc : idPropertyDescs) {
                Property<ENTITY, ?> property = propertyDesc.createProperty();
                property.load(entity);
                builder.appendSql(propertyDesc.getColumnName(naming::apply,
                        dialect::applyQuote));
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
            property.load(entity);
            builder.appendSql(versionPropertyDesc.getColumnName(naming::apply,
                    dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property.asInParameter());
        }
        sql = builder.build(this::comment);
    }

    @Override
    public void complete() {
        postDelete();
    }

    protected void postDelete() {
        AutoPostDeleteContext<ENTITY> context = new AutoPostDeleteContext<ENTITY>(
                entityDesc, method, config);
        entityDesc.postDelete(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored = versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected static class AutoPreDeleteContext<E> extends
            AbstractPreDeleteContext<E> {

        public AutoPreDeleteContext(EntityDesc<E> entityDesc, Method method,
                Config config) {
            super(entityDesc, method, config);
        }
    }

    protected static class AutoPostDeleteContext<E> extends
            AbstractPostDeleteContext<E> {

        public AutoPostDeleteContext(EntityDesc<E> entityDesc, Method method,
                Config config) {
            super(entityDesc, method, config);
        }
    }
}
