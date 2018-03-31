package org.seasar.doma.jdbc.query;

import java.sql.Statement;

/** An object used for building a batch of SQL INSERT statements. */
public interface BatchInsertQuery extends BatchModifyQuery {

  boolean isBatchSupported();

  void generateId(Statement statement, int index);
}
