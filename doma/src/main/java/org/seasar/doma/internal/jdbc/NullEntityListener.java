package org.seasar.doma.internal.jdbc;

import org.seasar.doma.jdbc.EntityListener;

/**
 * @author taedium
 * 
 */
public class NullEntityListener implements EntityListener<Object> {

    @Override
    public void preInsert(Object entity) {
    }

    @Override
    public void preUpdate(Object entity) {
    }

    @Override
    public void preDelete(Object entity) {
    }

}
