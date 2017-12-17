package org.seasar.doma.internal.jdbc.entity;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Method;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityPropertyNotDefinedException;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.PostUpdateContext;

/**
 * @author taedium
 * 
 */
public abstract class AbstractPostUpdateContext<E> extends AbstractEntityListenerContext<E>
        implements PostUpdateContext<E> {

    protected AbstractPostUpdateContext(EntityDesc<E> entityDesc, Method method, Config config) {
        super(entityDesc, method, config);
    }

    protected void validatePropertyDefined(String propertyName) {
        assertNotNull(propertyName);
        if (!isPropertyDefinedInternal(propertyName)) {
            throw new EntityPropertyNotDefinedException(entityDesc.getEntityClass().getName(),
                    propertyName);
        }
    }

}
