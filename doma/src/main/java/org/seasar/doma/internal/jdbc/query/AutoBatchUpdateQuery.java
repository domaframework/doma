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

import java.util.Iterator;

import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;

/**
 * @author taedium
 * 
 */
public class AutoBatchUpdateQuery<E> extends AutoBatchModifyQuery<E> implements
        BatchUpdateQuery {

    protected boolean versionIncluded;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchUpdateQuery(EntityTypeFactory<E> entityTypeFactory) {
        super(entityTypeFactory);
    }

    public void prepare() {
        assertNotNull(config, callerClassName, callerMethodName);
        Iterator<EntityType<E>> it = entityTypes.iterator();
        if (it.hasNext()) {
            executable = true;
            executionSkipCause = null;
            entityType = it.next();
            entityType.preUpdate();
            prepareTableAndColumnNames();
            prepareIdAndVersionProperties();
            validateIdExistent();
            prepareOptions();
            prepareOptimisticLock();
            prepareTargetProperties();
            prepareSql();
        } else {
            return;
        }
        while (it.hasNext()) {
            idProperties.clear();
            versionPropertyType = null;
            targetProperties.clear();
            this.entityType = it.next();
            entityType.preUpdate();
            prepareIdAndVersionProperties();
            prepareTargetProperties();
            prepareSql();
        }
        assertEquals(entityTypes.size(), sqls.size());
    }

    protected void prepareOptimisticLock() {
        if (versionPropertyType != null && !versionIncluded) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetProperties() {
        for (EntityPropertyType<?> p : entityType.getEntityPropertyTypes()) {
            if (!p.isUpdatable()) {
                continue;
            }
            if (p.isId()) {
                continue;
            }
            if (p.isVersion()) {
                targetProperties.add(p);
                continue;
            }
            if (!isTargetPropertyName(p.getName())) {
                continue;
            }
            targetProperties.add(p);
        }
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config);
        builder.appendSql("update ");
        builder.appendSql(tableName);
        builder.appendSql(" set ");
        for (EntityPropertyType<?> p : targetProperties) {
            builder.appendSql(columnNameMap.get(p.getName()));
            builder.appendSql(" = ");
            builder.appendDomain(p.getWrapper());
            if (p.isVersion() && !versionIncluded) {
                builder.appendSql(" + 1");
            }
            builder.appendSql(", ");
        }
        builder.cutBackSql(2);
        if (idProperties.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyType<?> p : idProperties) {
                builder.appendSql(columnNameMap.get(p.getName()));
                builder.appendSql(" = ");
                builder.appendDomain(p.getWrapper());
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (versionPropertyType != null && !versionIncluded) {
            if (idProperties.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            builder.appendSql(columnNameMap.get(versionPropertyType.getName()));
            builder.appendSql(" = ");
            builder.appendDomain(versionPropertyType.getWrapper());
        }
        PreparedSql sql = builder.build();
        sqls.add(sql);
    }

    @Override
    public void incrementVersions() {
        if (versionIncluded) {
            return;
        }
        for (EntityType<E> entityType : entityTypes) {
            VersionPropertyType<?> versionPropertyType = entityType
                    .getVersionPropertyType();
            if (versionPropertyType != null) {
                versionPropertyType.increment();
            }
        }
    }

    public void setVersionIncluded(boolean versionIncluded) {
        this.versionIncluded = versionIncluded;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

}
