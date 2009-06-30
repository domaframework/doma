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

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;

/**
 * @author taedium
 * 
 */
public class AutoUpdateQuery<I, E extends Entity<I>> extends
        AutoModifyQuery<I, E> implements UpdateQuery {

    protected boolean nullExcluded;

    protected boolean versionIncluded;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoUpdateQuery(Class<E> entityClass) {
        super(entityClass);
    }

    public void compile() {
        assertNotNull(config, entity, callerClassName, callerMethodName);
        entity.__preUpdate();
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
        for (Property<?> p : entity.__getProperties()) {
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
            if (nullExcluded && p.getDomain().isNull()) {
                continue;
            }
            if (p.getDomain().isChanged()) {
                targetProperties.add(p);
                executable = true;
            }
        }
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config
                .sqlLogFormattingVisitor());
        builder.appendSql("update ");
        builder.appendSql(tableName);
        builder.appendSql(" set ");
        for (Property<?> p : targetProperties) {
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
            for (Property<?> p : idProperties) {
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

}
