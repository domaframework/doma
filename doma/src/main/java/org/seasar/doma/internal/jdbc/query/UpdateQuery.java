package org.seasar.doma.internal.jdbc.query;


/**
 * @author taedium
 * 
 */
public interface UpdateQuery extends ModifyQuery {

    void incrementVersion();

}
