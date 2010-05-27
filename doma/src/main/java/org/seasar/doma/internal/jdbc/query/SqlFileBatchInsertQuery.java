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

import java.sql.Statement;

import org.seasar.doma.jdbc.SqlKind;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchInsertQuery<E> extends SqlFileBatchModifyQuery<E>
        implements BatchInsertQuery {

    public SqlFileBatchInsertQuery(Class<E> elementClass) {
        super(elementClass, SqlKind.BATCH_INSERT);
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    @Override
    public void generateId(Statement statement, int index) {
    }

    @Override
    protected void executeListener() {
        if (entityType != null) {
            entityType.preInsert(currentEntity);
        }
    }

}
