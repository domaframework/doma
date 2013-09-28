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
package org.seasar.doma.it.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class EmpListener<E extends Emp> implements EntityListener<E> {

    @Override
    public void preDelete(E entity, PreDeleteContext<E> context) {
    }

    @Override
    public void preInsert(E entity, PreInsertContext<E> context) {
        entity.setInsertTimestamp(new Timestamp(new Date().getTime()));
    }

    @Override
    public void preUpdate(E entity, PreUpdateContext<E> context) {
        if (context.isEntityChanged()) {
            entity.setUpdateTimestamp(new Timestamp(new Date().getTime()));
        }
    }

    @Override
    public void postInsert(E entity, PostInsertContext<E> context) {
    }

    @Override
    public void postUpdate(E entity, PostUpdateContext<E> context) {
    }

    @Override
    public void postDelete(E entity, PostDeleteContext<E> context) {
    }

}
