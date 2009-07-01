package org.seasar.doma.internal.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author taedium
 * 
 */
public interface CreateQuery<R> extends Query {

    R create(Connection connection) throws SQLException;
}
