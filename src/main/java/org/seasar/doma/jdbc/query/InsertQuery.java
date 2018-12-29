package org.seasar.doma.jdbc.query;

import java.sql.Statement;

public interface InsertQuery extends ModifyQuery {

  void generateId(Statement statement);
}
