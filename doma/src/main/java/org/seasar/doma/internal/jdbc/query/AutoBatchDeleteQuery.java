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

import java.util.Iterator;

import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;


/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteQuery<I, E extends Entity<I>> extends
        AutoBatchModifyQuery<I, E> implements BatchDeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchDeleteQuery(Class<E> entityClass) {
        super(entityClass);
    }

    public void compile() {
        assertNotNull(config, entities, callerClassName, callerMethodName);
        Iterator<? extends E> it = entities.iterator();
        if (it.hasNext()) {
            executable = true;
            entity = it.next();
            entity.__preDelete();
            prepareTableAndColumnNames();
            prepareIdAndVersionProperties();
            validateIdExistent();
            prepareOptions();
            prepareOptimisticLock();
            prepareSql();
        } else {
            return;
        }
        while (it.hasNext()) {
            idProperties.clear();
            versionProperty = null;
            targetProperties.clear();
            this.entity = it.next();
            entity.__preDelete();
            prepareIdAndVersionProperties();
            prepareSql();
        }
        assertEquals(entities.size(), sqls.size());
    }

    protected void prepareOptimisticLock() {
        if (versionProperty != null && !versionIgnored) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config
                .sqlLogFormattingVisitor());
        builder.appendSql("delete from ");
        builder.appendSql(tableName);
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
        if (versionProperty != null && !versionIgnored) {
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

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored = versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(
            boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

}
