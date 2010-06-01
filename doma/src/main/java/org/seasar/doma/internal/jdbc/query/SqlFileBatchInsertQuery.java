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

import java.sql.Statement;
import java.util.Iterator;

import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchInsertQuery<E> extends SqlFileBatchModifyQuery<E>
        implements BatchInsertQuery {

    protected EntityHandler entityHandler;

    public SqlFileBatchInsertQuery(Class<E> elementClass) {
        super(elementClass, SqlKind.BATCH_INSERT);
    }

    @Override
    public void prepare() {
        super.prepare();
        Iterator<E> it = elements.iterator();
        if (it.hasNext()) {
            executable = true;
            sqlExecutionSkipCause = null;
            currentEntity = it.next();
            preInsert();
            prepareSqlFile();
            prepareOptions();
            prepareSql();
        } else {
            return;
        }
        while (it.hasNext()) {
            currentEntity = it.next();
            preInsert();
            prepareSql();
        }
        assertEquals(elements.size(), sqls.size());
    }

    protected void preInsert() {
        if (entityHandler != null) {
            entityHandler.preInsert();
        }
    }

    @Override
    public void generateId(Statement statement, int index) {
    }

    @Override
    public void setEntityType(EntityType<E> entityType) {
        entityHandler = new EntityHandler(entityType);
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    protected class EntityHandler {

        protected EntityType<E> entityType;

        protected EntityHandler(EntityType<E> entityType) {
            assertNotNull(entityType);
            this.entityType = entityType;
        }

        protected void preInsert() {
            entityType.preInsert(currentEntity);
        }

    }
}
