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

import org.seasar.doma.entity.Entity;
import org.seasar.doma.entity.EntityProperty;
import org.seasar.doma.entity.VersionProperty;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;

/**
 * @author taedium
 * 
 */
public class AutoBatchUpdateQuery<I, E extends Entity<I>> extends
        AutoBatchModifyQuery<I, E> implements BatchUpdateQuery {

    protected boolean versionIncluded;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchUpdateQuery(Class<E> entityClass) {
        super(entityClass);
    }

    public void compile() {
        assertNotNull(config, entities, callerClassName, callerMethodName);
        Iterator<? extends E> it = entities.iterator();
        if (it.hasNext()) {
            executable = true;
            executionSkipCause = null;
            entity = it.next();
            entity.__preUpdate();
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
            versionProperty = null;
            targetProperties.clear();
            this.entity = it.next();
            entity.__preUpdate();
            prepareIdAndVersionProperties();
            prepareTargetProperties();
            prepareSql();
        }
        assertEquals(entities.size(), sqls.size());
    }

    protected void prepareOptimisticLock() {
        if (versionProperty != null && !versionIncluded) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetProperties() {
        for (EntityProperty<?> p : entity.__getEntityProperties()) {
            if (p.isTransient()) {
                continue;
            }
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
            targetProperties.add(p);
        }
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config);
        builder.appendSql("update ");
        builder.appendSql(tableName);
        builder.appendSql(" set ");
        for (EntityProperty<?> p : targetProperties) {
            builder.appendSql(columnNameMap.get(p.getName()));
            builder.appendSql(" = ");
            builder.appendDomain(p.getDomain());
            if (p.isVersion() && !versionIncluded) {
                builder.appendSql(" + 1");
            }
            builder.appendSql(", ");
        }
        builder.cutBackSql(2);
        if (idProperties.size() > 0) {
            builder.appendSql(" where ");
            for (EntityProperty<?> p : idProperties) {
                builder.appendSql(columnNameMap.get(p.getName()));
                builder.appendSql(" = ");
                builder.appendDomain(p.getDomain());
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (versionProperty != null && !versionIncluded) {
            if (idProperties.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            builder.appendSql(columnNameMap.get(versionProperty.getName()));
            builder.appendSql(" = ");
            builder.appendDomain(versionProperty.getDomain());
        }
        PreparedSql sql = builder.build();
        sqls.add(sql);
    }

    @Override
    public void incrementVersions() {
        if (versionIncluded) {
            return;
        }
        for (E entity : entities) {
            VersionProperty<?> versionProperty = entity.__getVersionProperty();
            if (versionProperty != null) {
                versionProperty.increment();
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
