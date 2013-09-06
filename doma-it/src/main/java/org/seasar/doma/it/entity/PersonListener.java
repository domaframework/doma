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

public class PersonListener implements EntityListener<Person> {

    @Override
    public void preDelete(Person entity, PreDeleteContext<Person> context) {
        context.setNewEntity(newPerson(entity, "_preD"));
    }

    @Override
    public void preInsert(Person entity, PreInsertContext<Person> context) {
        context.setNewEntity(newPerson(entity, "_preI"));
    }

    @Override
    public void preUpdate(Person entity, PreUpdateContext<Person> context) {
        context.setNewEntity(newPerson(entity, "_preU"));
    }

    @Override
    public void postInsert(Person entity, PostInsertContext<Person> context) {
        context.setNewEntity(newPerson(entity, "_postI"));
    }

    @Override
    public void postUpdate(Person entity, PostUpdateContext<Person> context) {
        context.setNewEntity(newPerson(entity, "_postU"));
    }

    @Override
    public void postDelete(Person entity, PostDeleteContext<Person> context) {
        context.setNewEntity(newPerson(entity, "_postD"));
    }

    protected Person newPerson(Person p, String suffix) {
        return new Person(p.employeeId, p.employeeNo, p.employeeName + suffix,
                p.managerId, p.hiredate, p.salary, p.departmentId, p.addressId,
                p.version);
    }
}
