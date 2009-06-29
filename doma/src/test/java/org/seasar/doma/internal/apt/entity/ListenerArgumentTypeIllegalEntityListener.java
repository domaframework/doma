package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.EntityListener;

/**
 * @author taedium
 * 
 */
public class ListenerArgumentTypeIllegalEntityListener implements
        EntityListener<String> {

    @Override
    public void preDelete(String entity) {
    }

    @Override
    public void preInsert(String entity) {
    }

    @Override
    public void preUpdate(String entity) {
    }

}
