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
package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/**
 * @author taedium
 * 
 */
public class ListenerArgumentTypeIllegalEntityListener implements
        EntityListener<String> {

    @Override
    public void preDelete(String entity, PreDeleteContext<String> context) {
    }

    @Override
    public void preInsert(String entity, PreInsertContext<String> context) {
    }

    @Override
    public void preUpdate(String entity, PreUpdateContext<String> context) {
    }

    @Override
    public void postInsert(String entity, PostInsertContext<String> context) {
    }

    @Override
    public void postUpdate(String entity, PostUpdateContext<String> context) {
    }

    @Override
    public void postDelete(String entity, PostDeleteContext<String> context) {
    }

}
