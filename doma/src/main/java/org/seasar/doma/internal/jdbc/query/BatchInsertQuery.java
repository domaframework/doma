package org.seasar.doma.internal.jdbc.query;

import java.sql.Statement;

/**
 * @author taedium
 * 
 */
public interface BatchInsertQuery extends BatchModifyQuery {

    boolean isBatchSupported();

    void generateId(Statement statement, int index);
}
