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
package org.seasar.doma.jdbc.entity;

/**
 * 何も行わない {@link EntityListener} の実装です。
 * 
 * @author taedium
 * 
 */
public class NullEntityListener implements EntityListener<Object> {

    @Override
    public void preInsert(Object entity, PreInsertContext<Object> context) {
    }

    @Override
    public void preUpdate(Object entity, PreUpdateContext<Object> context) {
    }

    @Override
    public void preDelete(Object entity, PreDeleteContext<Object> context) {
    }

    @Override
    public void postInsert(Object entity, PostInsertContext<Object> context) {
    }

    @Override
    public void postUpdate(Object entity, PostUpdateContext<Object> context) {
    }

    @Override
    public void postDelete(Object entity, PostDeleteContext<Object> context) {
    }

}
