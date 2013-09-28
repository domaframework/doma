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
public class NoDefaultConstructorEntityListener implements
        EntityListener<NoDefaultConstructorEntityListenerEntity> {

    public NoDefaultConstructorEntityListener(int i) {
    }

    @Override
    public void preDelete(NoDefaultConstructorEntityListenerEntity entity,
            PreDeleteContext<NoDefaultConstructorEntityListenerEntity> context) {
    }

    @Override
    public void preInsert(NoDefaultConstructorEntityListenerEntity entity,
            PreInsertContext<NoDefaultConstructorEntityListenerEntity> context) {
    }

    @Override
    public void preUpdate(NoDefaultConstructorEntityListenerEntity entity,
            PreUpdateContext<NoDefaultConstructorEntityListenerEntity> context) {
    }

    @Override
    public void postInsert(NoDefaultConstructorEntityListenerEntity entity,
            PostInsertContext<NoDefaultConstructorEntityListenerEntity> context) {
    }

    @Override
    public void postUpdate(NoDefaultConstructorEntityListenerEntity entity,
            PostUpdateContext<NoDefaultConstructorEntityListenerEntity> context) {
    }

    @Override
    public void postDelete(NoDefaultConstructorEntityListenerEntity entity,
            PostDeleteContext<NoDefaultConstructorEntityListenerEntity> context) {
    }

}
