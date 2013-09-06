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

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class DeptListener implements EntityListener<Dept> {

    @Override
    public void preDelete(Dept entity, PreDeleteContext<Dept> context) {
        Dept newEntity = new Dept(entity.departmentId, entity.departmentNo,
                entity.departmentName + "_preD", entity.location,
                entity.version);
        context.setNewEntity(newEntity);
    }

    @Override
    public void preInsert(Dept entity, PreInsertContext<Dept> context) {
        Dept newEntity = new Dept(entity.departmentId, entity.departmentNo,
                entity.departmentName + "_preI", entity.location,
                entity.version);
        context.setNewEntity(newEntity);
    }

    @Override
    public void preUpdate(Dept entity, PreUpdateContext<Dept> context) {
        Dept newEntity = new Dept(entity.departmentId, entity.departmentNo,
                entity.departmentName + "_preU", entity.location,
                entity.version);
        context.setNewEntity(newEntity);
    }

    @Override
    public void postInsert(Dept entity, PostInsertContext<Dept> context) {
        Dept newEntity = new Dept(entity.departmentId, entity.departmentNo,
                entity.departmentName + "_postI", entity.location,
                entity.version);
        context.setNewEntity(newEntity);
    }

    @Override
    public void postUpdate(Dept entity, PostUpdateContext<Dept> context) {
        Dept newEntity = new Dept(entity.departmentId, entity.departmentNo,
                entity.departmentName + "_postU", entity.location,
                entity.version);
        context.setNewEntity(newEntity);
    }

    @Override
    public void postDelete(Dept entity, PostDeleteContext<Dept> context) {
        Dept newEntity = new Dept(entity.departmentId, entity.departmentNo,
                entity.departmentName + "_postD", entity.location,
                entity.version);
        context.setNewEntity(newEntity);
    }

}
