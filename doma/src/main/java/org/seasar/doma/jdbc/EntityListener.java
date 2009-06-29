package org.seasar.doma.jdbc;

/**
 * @author taedium
 * 
 */
public interface EntityListener<E> {

    void preInsert(E entity);

    void preUpdate(E entity);

    void preDelete(E entity);
}
