package org.seasar.doma.internal.jdbc.sql;

/**
 * @author taedium
 * 
 */
public interface ListParameter<E> extends CallableSqlParameter {

    E add();
}
