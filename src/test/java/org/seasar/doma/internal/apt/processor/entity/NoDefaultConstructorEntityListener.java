package org.seasar.doma.internal.apt.processor.entity;

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
public class NoDefaultConstructorEntityListener
        implements EntityListener<NoDefaultConstructorEntityListenerEntity> {

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
