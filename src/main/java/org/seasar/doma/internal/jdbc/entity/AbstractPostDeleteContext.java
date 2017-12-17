package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.PostDeleteContext;

/**
 * @author taedium
 * 
 */
public class AbstractPostDeleteContext<E> extends AbstractEntityListenerContext<E>
        implements PostDeleteContext<E> {

    protected AbstractPostDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
        super(entityDesc, method, config);
    }
}
