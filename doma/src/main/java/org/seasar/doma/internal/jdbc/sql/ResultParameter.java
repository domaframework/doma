package org.seasar.doma.internal.jdbc.sql;

/**
 * @author taedium
 * 
 */
public interface ResultParameter<R> extends CallableSqlParameter {

    R getResult();
}
