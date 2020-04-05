package org.seasar.doma.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;

public interface CreateQuery<RESULT> extends Query {

  RESULT create(Connection connection) throws SQLException;
}
