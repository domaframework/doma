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

import java.util.Set;

import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.entity.EntityMetaFactory;
import org.seasar.doma.jdbc.entity.EntityPropertyMeta;

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

    public AutoUpdateQuery(EntityMetaFactory<E> entityMetaFactory) {
        super(entityMetaFactory);
    }

    public void prepare() {
        assertNotNull(config, entityMeta, callerClassName, callerMethodName);
        entityMeta.preUpdate();
        prepareTableAndColumnNames();
        prepareIdAndVersionProperties();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareTargetProperties();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareOptimisticLock() {
        if (versionProperty != null && !versionIncluded) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetProperties() {
        Set<String> dirtyStates = entityMeta.getModifiedProperties();
        for (EntityPropertyMeta<?> p : entityMeta.getPropertyMetas()) {
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
            if (nullExcluded && p.getWrapper().get() == null) {
                continue;
            }
            if (unchangedPropertyIncluded || dirtyStates == null
                    || dirtyStates.contains(p.getName())) {
                if (!isTargetPropertyName(p.getName())) {
                    continue;
                }
                targetProperties.add(p);
                executable = true;
                sqlExecutionSkipCause = null;
            }
        }
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config);
        builder.appendSql("update ");
        builder.appendSql(tableName);
        builder.appendSql(" set ");
        for (EntityPropertyMeta<?> p : targetProperties) {
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
            for (EntityPropertyMeta<?> p : idProperties) {
                builder.appendSql(columnNameMap.get(p.getName()));
                builder.appendSql(" = ");
                builder.appendDomain(p.getWrapper());
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
            builder.appendDomain(versionProperty.getWrapper());
        }
        sql = builder.build();
    }

    @Override
    public void incrementVersion() {
        if (versionIncluded || versionProperty == null) {
            return;
        }
        versionProperty.increment();
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
