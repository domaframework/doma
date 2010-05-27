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

import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public class SqlFileUpdateQuery extends SqlFileModifyQuery implements
        UpdateQuery {

    protected PreUpdate<?> preUpdate;

    public SqlFileUpdateQuery() {
        super(SqlKind.UPDATE);
    }

    public void prepare() {
        assertNotNull(config, sqlFilePath, callerClassName, callerMethodName);
        executeListener();
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void executeListener() {
        if (preUpdate != null) {
            preUpdate.execute();
        }
    }

    @Override
    public void incrementVersion() {
    }

    public <E> void setEntity(EntityType<E> entityType) {
        preUpdate = new PreUpdate<E>(entityType);
    }

    protected class PreUpdate<E> extends ListenerExecuter<E> {

        protected PreUpdate(EntityType<E> entityType) {
            super(entityType);
        }

        @Override
        protected void doExecute(E entity) {
            entityType.preUpdate(entity);
        }
    }
}
