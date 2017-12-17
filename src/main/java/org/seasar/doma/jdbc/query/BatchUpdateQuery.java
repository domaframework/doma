package org.seasar.doma.jdbc.query;

/**
 * An object used for building a batch of SQL UPDATE statements.
 */
public interface BatchUpdateQuery extends BatchModifyQuery {

    void incrementVersions();

}
