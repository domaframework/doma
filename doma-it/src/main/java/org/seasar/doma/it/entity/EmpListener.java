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

import org.seasar.doma.entity.EntityListener;

public class EmpListener implements EntityListener<Emp> {

    @Override
    public void preDelete(Emp entity) {
    }

    @Override
    public void preInsert(Emp entity) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        entity.insertTimestamp().set(timestamp);
    }

    @Override
    public void preUpdate(Emp entity) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        entity.updateTimestamp().set(timestamp);
    }

}
