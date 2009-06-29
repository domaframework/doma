package org.seasar.doma.internal.jdbc.query;


/**
 * @author taedium
 * 
 */
public interface BatchUpdateQuery extends BatchModifyQuery {

    boolean isOptimisticLockCheckRequired();

    void incrementVersions();

}
