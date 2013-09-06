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

import java.lang.reflect.Method;
import java.sql.Statement;

import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.jdbc.Config;
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
        int size = elements.size();
        if (size == 0) {
            return;
        }
        executable = true;
        sqlExecutionSkipCause = null;
        currentEntity = elements.get(0);
        preInsert();
        prepareSqlFile();
        prepareOptions();
        prepareSql();
        elements.set(0, currentEntity);
        for (int i = 1; i < size; i++) {
            currentEntity = elements.get(i);
            preInsert();
            prepareSql();
            elements.set(i, currentEntity);
        }
        assertEquals(size, sqls.size());
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
    public void complete() {
        if (entityHandler != null) {
            for (int i = 0, len = elements.size(); i < len; i++) {
                currentEntity = elements.get(i);
                entityHandler.postInsert();
                elements.set(i, currentEntity);
            }
        }
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
            SqlFileBatchPreInsertContext<E> context = new SqlFileBatchPreInsertContext<E>(
                    entityType, method, config);
            entityType.preInsert(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }

        protected void postInsert() {
            SqlFileBatchPostInsertContext<E> context = new SqlFileBatchPostInsertContext<E>(
                    entityType, method, config);
            entityType.postInsert(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }
    }

    protected static class SqlFileBatchPreInsertContext<E> extends
            AbstractPreInsertContext<E> {

        public SqlFileBatchPreInsertContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }

    protected static class SqlFileBatchPostInsertContext<E> extends
            AbstractPostInsertContext<E> {

        public SqlFileBatchPostInsertContext(EntityType<E> entityType,
                Method method, Config config) {
            super(entityType, method, config);
        }
    }
}
