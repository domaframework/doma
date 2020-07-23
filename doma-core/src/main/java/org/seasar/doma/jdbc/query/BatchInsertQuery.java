package org.seasar.doma.jdbc.query;

import java.sql.Statement;

public interface BatchInsertQuery extends BatchModifyQuery {

  boolean isBatchSupported();

  void generateId(Statement statement, int index);
}
