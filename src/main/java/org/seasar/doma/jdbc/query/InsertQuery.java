package org.seasar.doma.jdbc.query;

import java.sql.Statement;

/**
 * An object that builds SQL INSERT statements.
 */
public interface InsertQuery extends ModifyQuery {

    void generateId(Statement statement);
}
