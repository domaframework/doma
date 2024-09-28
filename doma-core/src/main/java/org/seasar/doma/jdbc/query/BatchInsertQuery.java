package org.seasar.doma.jdbc.query;

import java.sql.Statement;

public interface BatchInsertQuery extends BatchModifyQuery {

  boolean isBatchSupported();

  void generateId(Statement statement, int index);

  /**
   * Generates IDs for the batch.
   * 
   * @param statement the statement
   * @param position the position of the first element in the batch
   * @param size the size of the executed batch
   */
  default void generateIds(Statement statement, int position, int size) {}
}
