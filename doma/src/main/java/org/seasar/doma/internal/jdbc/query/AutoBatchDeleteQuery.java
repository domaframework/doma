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

import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.entity.EntityMeta;
import org.seasar.doma.jdbc.entity.EntityMetaFactory;
import org.seasar.doma.jdbc.entity.EntityPropertyMeta;

/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteQuery<E> extends AutoBatchModifyQuery<E> implements
        BatchDeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchDeleteQuery(EntityMetaFactory<E> entityMetaFactory) {
        super(entityMetaFactory);
    }

    public void prepare() {
        assertNotNull(config, callerClassName, callerMethodName);
        Iterator<EntityMeta<E>> it = entityMetas.iterator();
        if (it.hasNext()) {
            executable = true;
            executionSkipCause = null;
            entityMeta = it.next();
            entityMeta.preDelete();
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
            this.entityMeta = it.next();
            entityMeta.preDelete();
            prepareIdAndVersionProperties();
            prepareSql();
        }
        assertEquals(entityMetas.size(), sqls.size());
    }

    protected void prepareOptimisticLock() {
        if (versionProperty != null && !versionIgnored) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config);
        builder.appendSql("delete from ");
        builder.appendSql(tableName);
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
        if (versionProperty != null && !versionIgnored) {
            if (idProperties.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            builder.appendSql(columnNameMap.get(versionProperty.getName()));
            builder.appendSql(" = ");
            builder.appendDomain(versionProperty.getWrapper());
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
