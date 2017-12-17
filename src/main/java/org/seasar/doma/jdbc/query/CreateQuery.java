package org.seasar.doma.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An object used for preparing to create a JDBC object.
 * 
 * @param <RESULT>
 *            the result type
 */
public interface CreateQuery<RESULT> extends Query {

    RESULT create(Connection connection) throws SQLException;
}
